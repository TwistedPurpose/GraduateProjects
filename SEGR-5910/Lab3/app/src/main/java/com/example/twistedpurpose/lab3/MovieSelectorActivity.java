package com.example.twistedpurpose.lab3;


import android.app.Fragment;
import android.content.Intent;

/**
 * By Soren Ludwig
 * An object that creates a MovieSelectorFragment and initiates movie view
 * actions when View button is clicked.
 */
public class MovieSelectorActivity extends SingleFragmentActivity implements MovieSelectorFragment.OnMovieViewListener {

    // Creates the fragment for movie selection
    @Override
    protected Fragment createFragment() {
        return MovieSelectorFragment.newInstance();
    }

    // Creates an intent with url of movie to be viewed
    // Initiates intent for MovieWebFragment to bring up website of movie
    @Override
    public void onMovieView(String url) {
        Intent intent = new Intent();
        intent.setClass(this, MovieWebActivity.class);
        intent.putExtra(MovieWebFragment.URL_KEY, url);
        startActivity(intent);
    }
}
