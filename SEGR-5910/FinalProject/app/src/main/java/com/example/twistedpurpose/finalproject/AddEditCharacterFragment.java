package com.example.twistedpurpose.finalproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;
import android.widget.Toolbar;


public class AddEditCharacterFragment extends Fragment {

    public static final String ARG_PARAM1 = "param1";

    private InitiativeTrackerDBHelper mHelper;

    private String mParam1;

    private Character mCharacter;

    public AddEditCharacterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddEditCharacterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddEditCharacterFragment newInstance() {
        AddEditCharacterFragment fragment = new AddEditCharacterFragment();
        Bundle args = new Bundle();
        //args.putInt(ARG_PARAM1, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_character, container, false);

        mHelper = new InitiativeTrackerDBHelper(getActivity());

        mCharacter = mHelper.addCharacter();

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
        modifierPicker.setText(Integer.toString(mCharacter.getModifier()));

        modifierPicker.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mCharacter.setModifier(Integer.parseInt(c.toString()));
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
