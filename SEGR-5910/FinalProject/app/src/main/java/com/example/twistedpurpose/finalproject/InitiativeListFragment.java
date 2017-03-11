package com.example.twistedpurpose.finalproject;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InitiativeListFragment.OnCharacterListListener} interface
 * to handle interaction events.
 * Use the {@link InitiativeListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InitiativeListFragment extends Fragment {

    private InitiativeTrackerDBHelper.CharacterCursor mCursor;

    private int currentlyActingCharacter = 0;

    private CharacterCursorAdapter adapter;

    private OnCharacterListListener mListener;

    public InitiativeListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InitiativeListFragment.
     */
    public static InitiativeListFragment newInstance() {
        return new InitiativeListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_initiative_list, container, false);

        //getActivity().deleteDatabase("characters.db");

        Context context = getActivity();

        // 1. Create a new InitiativeTrackerDBHelper
        InitiativeTrackerDBHelper dbHelper = new InitiativeTrackerDBHelper(context);

        // 2. Query the characters and obtain a cursor (store in mCursor).
        mCursor = dbHelper.queryCharacters();

        // 3. Find ListView to populate
        ListView characterListView = (ListView) v.findViewById(R.id.character_listView);

        // 4. Setup cursor adapter using cursor from last step
        adapter = new CharacterCursorAdapter(context, mCursor);

        // 5. Attach cursor adapter to the ListView
        characterListView.setAdapter(adapter);

        characterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null) {
                    mListener.onUpdateCharacter(id);
                }
            }
        });

        Button rollButton = (Button) v.findViewById(R.id.rollBtn);
        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InitiativeTrackerDBHelper dbHelper = new InitiativeTrackerDBHelper(getContext());
                List<Character> characterList = dbHelper.getCharacters();

                InitiativeRoller.rollInitiative(characterList);

                for (Character c : characterList) {
                    dbHelper.updateCharacter(c);
                }

                dbHelper.updateNextCharacter(true);

                updateInitiativeList();

                Toast.makeText(getContext(), "Roll initiative!", Toast.LENGTH_SHORT).show();
            }
        });

        Button addButton = (Button) v.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null) {
                    mListener.onAddCharacter();
                }
            }
        });

        Button nextButton = (Button) v.findViewById(R.id.nextBtn);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InitiativeTrackerDBHelper dbHelper = new InitiativeTrackerDBHelper(getContext());
                dbHelper.updateNextCharacter(false);
                updateInitiativeList();
            }
        });

        return v;
    }

    public void updateInitiativeList(){
        if(mCursor != null && adapter != null){
            mCursor.requery();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCharacterListListener) {
            mListener = (OnCharacterListListener) context;
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

    public interface OnCharacterListListener {
        public void onAddCharacter();
        public void onUpdateCharacter(long id);
    }

    /**
     * A character cursor adaptor for adding characters
     * to a list
     */
    private static class CharacterCursorAdapter extends CursorAdapter {

        private InitiativeTrackerDBHelper.CharacterCursor mCharacterCursor;

        public CharacterCursorAdapter(Context context, InitiativeTrackerDBHelper.CharacterCursor cursor) {
            super(context, cursor, 0);
            mCharacterCursor = cursor;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            // Use a layout inflater to get a row view
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(R.layout.character_listview, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView characterName = (TextView) view.findViewById(R.id.name);
            TextView initRoll = (TextView) view.findViewById(R.id.initRoll);
            TextView characterMod = (TextView) view.findViewById(R.id.mod);
            TextView characterInit = (TextView) view.findViewById(R.id.init);

            Character character = mCharacterCursor.getCharacter();

            //Put into Character logic to get integer values of initiative and modifier
            characterName.setText(character.getName());
            initRoll.setText(Integer.toString(character.getInitiative()));
            characterMod.setText(Integer.toString(character.getModifier()));
            characterInit.setText(Integer.toString(character.getTotalInitiative()));

            TableRow row = (TableRow) view.findViewById(R.id.row);
            if (character.isInSpotlight()){
                row.setBackgroundResource(android.R.color.darker_gray);
            } else {
                row.setBackgroundResource(android.R.color.white);
            }
        }
    }
}
