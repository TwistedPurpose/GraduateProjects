package com.example.twistedpurpose.movieviewer;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

public class MovieDetailActivity extends MovieSelectorActivity {

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
        int index = intent.getIntExtra(MovieDetailFragment.MOVIE_INDEX, 0);
        return MovieDetailFragment.newInstance(index);
    }
}
