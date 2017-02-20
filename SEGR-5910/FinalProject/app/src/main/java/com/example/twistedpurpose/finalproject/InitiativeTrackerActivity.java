package com.example.twistedpurpose.finalproject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class InitiativeTrackerActivity extends SingleFragmentActivity
    implements InitiativeListFragment.OnCharacterListListener {

    @Override
    protected Fragment createFragment() {
        return InitiativeListFragment.newInstance();
    }

    @Override
    public void onAddCharacter() {
        Intent intent = new Intent();

        intent.setClass(this, AddEditCharacterActivity.class);
        startActivity(intent);
    }
}
