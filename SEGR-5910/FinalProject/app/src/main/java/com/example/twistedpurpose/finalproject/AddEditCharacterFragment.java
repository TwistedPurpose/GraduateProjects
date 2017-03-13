package com.example.twistedpurpose.finalproject;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Fragment for modifying/deleting/adding character name and modifier
 */
public class AddEditCharacterFragment extends Fragment {

    // String for setting and fetching id of character
    // Used for communication from InitiativeListActivity
    public final static String CHARACTER_ROW_ID = "id";

    // DB helper for setting and fetching character info
    private InitiativeTrackerDBHelper mHelper;

    // TextEdit field for setting a character's initiative modifier
    private EditText modifierPicker;

    // Object for currently being created and edited
    private Character mCharacter;

    // Interface for communicating back to the list activity
    private OnCharacterSave mListener;

    public AddEditCharacterFragment() {
        // Required empty public constructor
    }

    /**
     * Starts a new instance of AddEditCharacterFragment
     * @param id - optional parameter, if not -1 edit, otherwise add
     * @return - The new fragment to perform CRUD on a character
     */
    public static AddEditCharacterFragment newInstance(long id) {
        AddEditCharacterFragment fragment = new AddEditCharacterFragment();
        Bundle args = new Bundle();

        //Get the character ID from intent
        args.putLong(CHARACTER_ROW_ID, id);

        fragment.setArguments(args);
        return fragment;
    }

    /**
     * On create, setup the DB helper, create a new character or
     * fetch an existing character from the database to set for this
     * fragment
     * @param savedInstanceState - Saved state of the application
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long id = 0;

        // Fetch the character id if there is one
        if (getArguments() != null) {
            id = getArguments().getLong(CHARACTER_ROW_ID, -1);
        }

        // Setup database helper to perform transactions on character info
        mHelper = new InitiativeTrackerDBHelper(getActivity());

        // If no id was given, make a new character
        if (id == -1) {
            mCharacter = mHelper.addCharacter();
        } else {
            // else get the character's information
            mCharacter = mHelper.getCharacter(id);
        }

    }

    /**
     * For setting up the view and the listeners for the page
     * Implements some logic for preventing bad input
     * @param inflater - Inflates the view
     * @param container - Used to help inflate view
     * @param savedInstanceState - saved instance of view
     * @return - Completed view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_edit_character, container, false);

        // Get the text edit view to be populated if there is character name
        EditText characterNameEditText = (EditText) v.findViewById(R.id.character_name_text_edit);

        // Set character name for
        characterNameEditText.setText(mCharacter.getName());

        characterNameEditText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                // Set the name of the character when text is changed
                mCharacter.setName(c.toString());
            }

            public void beforeTextChanged(CharSequence c, int start, int before, int after) {
            }

            public void afterTextChanged(Editable c) {
            }
        });

        modifierPicker =
                (EditText) v.findViewById(R.id.modEditText);

        // If modifier is 0, set as empty string otherweise get the character mod as
        // a string
        String modifierText = "";
        if (mCharacter.getModifier() != 0) {
            modifierText = mCharacter.getModifierAsString();
        }

        modifierPicker.setText(modifierText);

        modifierPicker.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {

            }

            public void beforeTextChanged(CharSequence c, int start, int before, int after) {
            }

            public void afterTextChanged(Editable c) {
                int modNumber;

                try {
                    //Set the new mod number after user inputs
                    modNumber = Integer.parseInt(c.toString());
                    mCharacter.setModifier(modNumber);

                } catch (NumberFormatException e) {
                    //If what the user input was not a number,
                    // Set modifier to 0 in interface and character obj
                    mCharacter.setModifier(0);
                    modifierPicker.setText("0");
                }
            }
        });

        Button saveButton = (Button) v.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHelper != null) {
                    if (mCharacter.getName() != null
                            && !mCharacter.getName().equals("")
                            && mCharacter.getName().length() <= 30
                            && mCharacter.getModifier() <= 10
                            && mCharacter.getModifier() >= -10) {

                        //If character name is not null or empty string
                        //And character mod is between 10 and -10 inclusive
                        //Set character info

                        mHelper.updateCharacter(mCharacter);

                        Toast.makeText(getActivity(),
                                "Update complete!", Toast.LENGTH_LONG).show();

                        mListener.onCharacterSave();
                    } else {
                        // If character name is empty string, throw an
                        // error message to user
                        if (mCharacter.getName().equals("")
                                ) {
                            Toast.makeText(getActivity(),
                                    "Invalid Name", Toast.LENGTH_LONG).show();
                        } else if (mCharacter.getName().length() > 30) {
                            Toast.makeText(getActivity(),
                                    "Name is too long", Toast.LENGTH_LONG).show();
                        }
                        // Throw an error message if modifier is greater than 10
                        if (mCharacter.getModifier() > 10) {
                            Toast.makeText(getActivity(),
                                    "Modifier must be less than 10", Toast.LENGTH_LONG).show();
                        }

                        // Or less than 10
                        if (mCharacter.getModifier() < 10) {
                            Toast.makeText(getActivity(),
                                    "Modifier must be more than -10", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

        Button deleteButton = (Button) v.findViewById(R.id.deleteButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHelper != null) {
                    //Delete character from the database
                    mHelper.deleteCharacter(mCharacter);

                    Toast.makeText(getActivity(), "Deleted character!", Toast.LENGTH_LONG).show();

                    // Send user back to list view activity
                    mListener.onCharacterSave();
                }

            }
        });

        return v;
    }

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

    /**
     * Interface for switching back to the list activity
     */
    public interface OnCharacterSave {
        public void onCharacterSave();
    }
}
