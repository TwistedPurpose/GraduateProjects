package com.example.twistedpurpose.finalproject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class InitiativeTrackerActivity extends SingleFragmentActivity
    implements InitiativeListFragment.OnCharacterListListener, AddEditCharacterFragment.OnCharacterSave {

    @Override
    protected Fragment createFragment() {
        return InitiativeListFragment.newInstance();
    }

    @Override
    public void onAddCharacter() {
        Intent intent = new Intent(this, AddEditCharacterActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCharacterSave() {
        FragmentManager fm = getFragmentManager();

        // Get the container for the character list
        InitiativeListFragment initiativeListFragment = (InitiativeListFragment)
                fm.findFragmentById(R.id.fragmentContainer);

        // Update the UI
        initiativeListFragment.updateInitiativeList();
    }
}
