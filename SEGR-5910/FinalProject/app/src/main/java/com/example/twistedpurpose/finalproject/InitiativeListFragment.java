package com.example.twistedpurpose.finalproject;

import android.content.Context;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
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

    private CharacterCursorAdapter adapter;

    private OnCharacterListListener mListener;

    private SensorEventListener mSensorListener;
    private SensorManager mSensorManager;

    private static final float SHAKE_THRESHOLD = 50f; // m/S**2
    private static final int MIN_TIME_BETWEEN_SHAKES_MILLISECS = 1000;
    private long mLastShakeTime;

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

        setUpCharacterList(v);

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

        Button rollButton = (Button) v.findViewById(R.id.rollBtn);
        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollInitiative();
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

    private void rollInitiative(){
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

    public void updateInitiativeList(){
        if(mCursor != null && adapter != null){
            updateCharacterAdapter();
        }
    }

    private void updateCharacterAdapter() {
        setUpCharacterList(null);
    }

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
            TextView characterName = (TextView) view.findViewById(R.id.name);
            TextView characterInitRoll = (TextView) view.findViewById(R.id.initRoll);
            TextView characterMod = (TextView) view.findViewById(R.id.mod);
            TextView characterInit = (TextView) view.findViewById(R.id.init);

            Character character = mCharacterCursor.getCharacter();

            //Put into Character logic to get integer values of initiative and modifier
            characterName.setText(character.getName());
            characterInitRoll.setText(character.getInitativeAsString());
            characterMod.setText(character.getModifierAsString());
            characterInit.setText(character.getTotalInitiativeAsString());

            TableRow row = (TableRow) view.findViewById(R.id.row);
            if (character.isInSpotlight()){
                row.setBackgroundResource(android.R.color.darker_gray);
            } else {
                row.setBackgroundResource(android.R.color.white);
            }
        }
    }
}
