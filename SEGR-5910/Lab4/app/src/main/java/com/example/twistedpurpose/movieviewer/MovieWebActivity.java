package com.example.twistedpurpose.movieviewer;

import android.app.Fragment;
import android.content.Intent;

import android.os.Bundle;

/**
 * Simple class that extends MovieSelectorActivity
 * Used mostly for initiating movie web viewing
 */
public class MovieWebActivity extends MovieSelectorActivity {

    // On creation, make the movie web fragment
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createFragment();
    }

    // Initiate an intent to contact MovieWebFragment to do a web view of movie content
    @Override
    protected Fragment createFragment() {

        Intent intent = getIntent();
        String url = intent.getStringExtra(MovieWebFragment.URL_KEY);
        return MovieWebFragment.newInstance(url);
    }
}
