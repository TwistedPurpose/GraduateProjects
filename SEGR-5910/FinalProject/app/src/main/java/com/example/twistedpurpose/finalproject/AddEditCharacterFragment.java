package com.example.twistedpurpose.finalproject;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AddEditCharacterFragment extends Fragment {

    public final static String CHARACTER_ROW_ID = "id";

    private InitiativeTrackerDBHelper mHelper;

    private Character mCharacter;

    public AddEditCharacterFragment() {
        // Required empty public constructor
    }

    public static AddEditCharacterFragment newInstance(long id) {
        AddEditCharacterFragment fragment = new AddEditCharacterFragment();
        Bundle args = new Bundle();
        args.putLong(CHARACTER_ROW_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long id = 0;
        if (getArguments() != null) {
            id = getArguments().getLong(CHARACTER_ROW_ID, -1);
        }

        mHelper = new InitiativeTrackerDBHelper(getActivity());
        if (id == -1) {
            mCharacter = mHelper.addCharacter();
        } else {
            mCharacter = mHelper.getCharacter(id);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_edit_character, container, false);

        //mHelper = new InitiativeTrackerDBHelper(getActivity());

        //mCharacter = mHelper.addCharacter();

        EditText characterNameEditText = (EditText) v.findViewById(R.id.character_name_text_edit);
        characterNameEditText.setText(mCharacter.getName());
        characterNameEditText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mCharacter.setName(c.toString());
            }

            public void beforeTextChanged(CharSequence c, int start, int before, int after) {
            }

            public void afterTextChanged(Editable c) {
            }
        });

        EditText modifierPicker =
                (EditText) v.findViewById(R.id.modEditText);

        //Break out into function that gets modifier from string
        //Inside of Character

        String modifierText = "";
        if (mCharacter.getModifier() != 0)
        {
            modifierText = Integer.toString(mCharacter.getModifier());
        }

        modifierPicker.setText(modifierText);

        modifierPicker.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                int mod = 0;
                try {
                    mod = Integer.parseInt(c.toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Invalid init mod!", Toast.LENGTH_SHORT).show();
                }
                mCharacter.setModifier(mod);
            }

            public void beforeTextChanged(CharSequence c, int start, int before, int after) {
            }

            public void afterTextChanged(Editable c) {
            }
        });

        Button saveButton = (Button) v.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mHelper != null)
                {
                    mHelper.updateCharacter(mCharacter);
                    Toast.makeText(getActivity(), "Update complete!", Toast.LENGTH_LONG).show();
                    mListener.onCharacterSave();
                }

            }
        });

        Button deleteButton = (Button) v.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mHelper != null)
                {
                    //mHelper.deleteCharacter(mCharacter);
                    Toast.makeText(getActivity(), "Deleted character!", Toast.LENGTH_LONG).show();
                    mListener.onCharacterSave();
                }

            }
        });

        return v;
    }

    private OnCharacterSave mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCharacterSave) {
            mListener = (OnCharacterSave) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnCharacterSave {
        public void onCharacterSave();
    }
}
