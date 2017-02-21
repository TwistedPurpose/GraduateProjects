package com.example.twistedpurpose.finalproject;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AddEditCharacterActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return AddEditCharacterFragment.newInstance();
    }
}
