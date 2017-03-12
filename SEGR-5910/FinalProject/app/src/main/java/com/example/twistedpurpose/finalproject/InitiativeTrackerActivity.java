package com.example.twistedpurpose.finalproject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Activity for the main part of the application
 * Shows a list of characters, dice roller, next button
 * and add button
 */
public class InitiativeTrackerActivity extends SingleFragmentActivity
    implements InitiativeListFragment.OnCharacterListListener {

    @Override
    protected Fragment createFragment() {
        return InitiativeListFragment.newInstance();
    }

    /**
     * Sends user to add a new character screen
     */
    @Override
    public void onAddCharacter() {
        Intent intent = new Intent(this, AddEditCharacterActivity.class);
        startActivity(intent);
    }

    /**
     * Sends user to edit screen
     * @param id - ID of character to be edited
     */
    @Override
    public void onUpdateCharacter(long id) {
        Intent intent = new Intent(this, AddEditCharacterActivity.class);

        intent.putExtra(AddEditCharacterFragment.CHARACTER_ROW_ID,id);

        startActivity(intent);
    }
}
