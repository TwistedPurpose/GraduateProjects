package com.example.twistedpurpose.finalproject;

import android.content.Context;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
 * Primary fragment for the application
 * Manages rolling, adding characters, editing characters, highing the next
 * character
 */
public class InitiativeListFragment extends Fragment {

    //Database cursor for application
    private InitiativeTrackerDBHelper.CharacterCursor mCursor;

    //Adaptor for list view of characters
    private CharacterCursorAdapter adapter;

    //For communicating intent to edit/add activity
    private OnCharacterListListener mListener;

    // Sensor and manager for phone shaking to roll initiative
    private SensorEventListener mSensorListener;
    private SensorManager mSensorManager;

    //Shake information, be sure to shake violently
    private static final float SHAKE_THRESHOLD = 50f; // m/S**2
    private static final int MIN_TIME_BETWEEN_SHAKES_MILLISECS = 1000;
    private long mLastShakeTime;

    public InitiativeListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * InitiativeListFragment using the provided parameters.
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
            //Update dataset when fragment is created
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_initiative_list, container, false);

        // Sets up the character list in the view
        setUpCharacterList(v);

        // Sensor checks for violent shaking, just don't throw your device
        mSensorListener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    long curTime = System.currentTimeMillis();
                    if ((curTime - mLastShakeTime) > MIN_TIME_BETWEEN_SHAKES_MILLISECS) {

                        float x = event.values[0];
                        float y = event.values[1];
                        float z = event.values[2];

                        double acceleration = Math.sqrt(Math.pow(x, 2) +
                                Math.pow(y, 2) +
                                Math.pow(z, 2)) - SensorManager.GRAVITY_EARTH;

                        if (acceleration > SHAKE_THRESHOLD) {
                            mLastShakeTime = curTime;
                            rollInitiative();
                        }
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        //Sets sensor and sensor manager for shaking
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        ListView characterListView = (ListView) v.findViewById(R.id.character_listView);

        characterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null) {
                    mListener.onUpdateCharacter(id);
                }
            }
        });

        // When the Roll button is pressed, roll initiative for all characters
        // And update the list
        Button rollButton = (Button) v.findViewById(R.id.rollBtn);
        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollInitiative();
            }
        });

        //When add is pressed, switch to new character activity
        Button addButton = (Button) v.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null) {
                    mListener.onAddCharacter();
                }
            }
        });

        //When next button is pressed, update in db the highlighted character
        //And update the UI with the next highlighted character below the current one
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

    /**
     * A private helper method for rolling initiative on all the characters
     * and sorting
     */
    private void rollInitiative(){
        InitiativeTrackerDBHelper dbHelper = new InitiativeTrackerDBHelper(getContext());
        //Get all characters
        List<Character> characterList = dbHelper.getCharacters();

        //Roll initiative for all the characters (d20 roll)
        InitiativeRoller.rollInitiative(characterList);

        //Update each character in the list
        for (Character c : characterList) {
            dbHelper.updateCharacter(c);
        }

        //Set the top character in initiative as highlighted
        dbHelper.updateNextCharacter(true);

        //Refresh the screen
        updateInitiativeList();

        Toast.makeText(getContext(), "Roll initiative!", Toast.LENGTH_SHORT).show();
    }

    public void updateInitiativeList(){
        if(mCursor != null && adapter != null){
            //When coming back from edit/add/delete update the list
            updateCharacterAdapter();
        }
    }

    //Helper method for better code readability and reduces
    //Sets up character adapter when a change has been made
    private void updateCharacterAdapter() {
        setUpCharacterList(null);
    }

    /**
     * Helper function for setting up the character ListView with characters
     * @param view - If not null, first time setup, otherwise update adapter
     */
    private void setUpCharacterList(View view){
        ListView characterListView;

        Context context = getActivity();

        // 1. Create a new InitiativeTrackerDBHelper
        InitiativeTrackerDBHelper dbHelper = new InitiativeTrackerDBHelper(context);

        // 2. Query the characters and obtain a cursor (store in mCursor).
        mCursor = dbHelper.getCharacterCursor();

        // 3. Find ListView to populate
        if (view != null) {
            characterListView = (ListView) view.findViewById(R.id.character_listView);
        } else {
            characterListView = (ListView) getActivity().findViewById(R.id.character_listView);
        }

        // 4. Setup cursor adapter using cursor from last step
        adapter = new CharacterCursorAdapter(context, mCursor);

        // 5. Attach cursor adapter to the ListView
        characterListView.setAdapter(adapter);
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
     * Interface for sending intent messages to edit
     * or add a character
     */
    public interface OnCharacterListListener {
        public void onAddCharacter();
        public void onUpdateCharacter(long id);
    }

    @Override
    public void onResume() {
        super.onResume();

        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
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
            //Get textviews to be updated
            TextView characterName = (TextView) view.findViewById(R.id.name);
            TextView characterInitRoll = (TextView) view.findViewById(R.id.initRoll);
            TextView characterMod = (TextView) view.findViewById(R.id.mod);
            TextView characterInit = (TextView) view.findViewById(R.id.init);

            //For setting color if character has spotlight
            TableRow row = (TableRow) view.findViewById(R.id.row);

            //Get all the character to show data
            Character character = mCharacterCursor.getCharacter();

            //Set text values in textviews
            characterName.setText(character.getName());
            characterInitRoll.setText(character.getInitiativeAsString());
            characterMod.setText(character.getModifierAsString());
            characterInit.setText(character.getTotalInitiativeAsString());

            // If character has spotlight, set to gray
            // Else set to white
            if (character.isInSpotlight()){
                row.setBackgroundResource(android.R.color.darker_gray);
            } else {
                row.setBackgroundResource(android.R.color.white);
            }
        }
    }
}
