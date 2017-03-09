package com.example.twistedpurpose.finalproject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AddEditCharacterActivity extends SingleFragmentActivity
        implements AddEditCharacterFragment.OnCharacterSave {

    @Override
    protected Fragment createFragment() {
        Intent intent = getIntent();

        long id = intent.getLongExtra(AddEditCharacterFragment.CHARACTER_ROW_ID, -1);
        return AddEditCharacterFragment.newInstance(id);
    }

    @Override
    public void onCharacterSave() {
        Intent intent = new Intent(this,InitiativeTrackerActivity.class);
        startActivity(intent);
    }
}
