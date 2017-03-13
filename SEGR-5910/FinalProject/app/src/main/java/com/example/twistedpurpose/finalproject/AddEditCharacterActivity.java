package com.example.twistedpurpose.finalproject;

import android.app.Fragment;
import android.content.Intent;

/**
 * Activity for adding/editing/deleting characters
 */
public class AddEditCharacterActivity extends SingleFragmentActivity
        implements AddEditCharacterFragment.OnCharacterSave {

    @Override
    protected Fragment createFragment() {
        Intent intent = getIntent();

        //Set the character id if there is one
        long id = intent.getLongExtra(AddEditCharacterFragment.CHARACTER_ROW_ID, -1);
        return AddEditCharacterFragment.newInstance(id);
    }

    /**
     * Switch back to view of list of characters
     */
    @Override
    public void onCharacterSave() {
        Intent intent = new Intent(this,InitiativeTrackerActivity.class);
        startActivity(intent);
    }
}
