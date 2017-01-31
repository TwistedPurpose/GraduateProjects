package com.example.twistedpurpose.movieviewer;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class MovieDetailActivity extends MovieSelectorActivity {

    // On creation, make the movie web fragment
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        createFragment();
    }

    // Initiate an intent to contact MovieWebFragment to do a web view of movie content
    @Override
    protected Fragment createFragment() {

        Intent intent = getIntent();
        int index = intent.getIntExtra(MovieDetailFragment.MOVIE_INDEX, 0);
        return MovieDetailFragment.newInstance(index);
    }


    //End the current activity if the back button is pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
