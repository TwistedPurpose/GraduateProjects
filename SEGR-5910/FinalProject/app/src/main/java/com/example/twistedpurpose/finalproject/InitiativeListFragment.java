package com.example.twistedpurpose.finalproject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
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
        InitiativeListFragment fragment = new InitiativeListFragment();
        return fragment;
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

        dbHelper.addCharacter(new Character("Mike",2));
        dbHelper.addCharacter(new Character("Soren",4));
        dbHelper.addCharacter(new Character("Stravis",-1));
        dbHelper.addCharacter(new Character("Dragon",6));

        // 2. Query the characters and obtain a cursor (store in mCursor).
        mCursor = dbHelper.queryCharacters();

        // Find ListView to populate
        ListView characterListView = (ListView) v.findViewById(R.id.character_listView);
        // Setup cursor adapter using cursor from last step
        adapter = new CharacterCursorAdapter(context, mCursor);
        // Attach cursor adapter to the ListView
        characterListView.setAdapter(adapter);

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

                mCursor.requery();
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Roll initiative!", Toast.LENGTH_SHORT).show();
            }
        });

        Button addButton = (Button) v.findViewById(R.id.addBtn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null) {
                    mListener.onAddCharacter();
                }
            }
        });

        return v;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnCharacterListListener {
        void onAddCharacter();
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
            TextView characterMod = (TextView) view.findViewById(R.id.mod);
            TextView characterInit = (TextView) view.findViewById(R.id.init);

            characterName.setText(mCharacterCursor.getCharacter().getName());
            characterMod.setText(Integer.toString(mCharacterCursor.getCharacter().getModifier()));
            characterInit.setText(Integer.toString(mCharacterCursor.getCharacter().getTotalInitiative()));
        }
    }
}
