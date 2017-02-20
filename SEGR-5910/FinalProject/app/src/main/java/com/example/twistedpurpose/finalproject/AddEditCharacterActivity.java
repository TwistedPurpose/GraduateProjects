package com.example.twistedpurpose.finalproject;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AddEditCharacterActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        Intent intent = getIntent();
        //int id = intent.getIntExtra(AddEditCharacterFragment.ARG_PARAM1, -1);
        return AddEditCharacterFragment.newInstance();
    }
}
