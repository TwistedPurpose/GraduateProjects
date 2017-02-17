package com.example.twistedpurpose.finalproject;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class InitiativeTrackerActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return InitiativeListFragment.newInstance();
    }

}
