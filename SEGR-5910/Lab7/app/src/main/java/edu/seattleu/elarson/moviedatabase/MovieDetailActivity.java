package edu.seattleu.elarson.moviedatabase;

import android.app.Fragment;
import android.content.Intent;


public class MovieDetailActivity extends SingleFragmentActivity {

    /**
     * Creates a new fragment and
     * sets up to create a new movie
     */
    @Override
    protected Fragment createFragment() {
        Intent intent = getIntent();

        long id = intent.getLongExtra(MovieDetailFragment.EXTRA_ID, -1);
        return MovieDetailFragment.newInstance(id);
    }

}
