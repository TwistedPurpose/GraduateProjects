package com.example.twistedpurpose.movieviewer;


import android.app.Fragment;
import android.content.Intent;
import android.view.Menu;

/**
 * By Soren Ludwig
 * An object that creates a MovieSelectorFragment and initiates movie view
 * actions when View button is clicked.
 */
public class MovieSelectorActivity extends SingleFragmentActivity
        implements MovieSelectorFragment.OnMovieViewListener {

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

        // Set intent class and movie url
        intent.setClass(this, MovieWebActivity.class);
        intent.putExtra(MovieWebFragment.URL_KEY, url);

        startActivity(intent);
    }

    /**
     * Presents the edit screen to user with current movie data
     * @param index - Index of movie being viewed
     */
    public void onMovieEdit(int index) {
        Intent intent = new Intent();

        // Set movie detail class and current movie index
        intent.setClass(this, MovieDetailActivity.class);
        intent.putExtra(MovieDetailFragment.MOVIE_INDEX, index);

        startActivity(intent);
    }

    /**
     * Function for setting up menus
     * @param menu - sets up menu
     * @return Just do it
     */
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        //getMenuInflater().inflate(R.menu.movie_menu, menu);
        return true;
    }
}
