package edu.seattleu.elarson.moviedatabase;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;

import edu.seattleu.elarson.moviedatabase.MovieListFragment.OnMovieListListener;

public class MovieListActivity extends SingleFragmentActivity
        implements OnMovieListListener, MovieDetailFragment.OnMovieDetailListener {

    protected int getLayoutId() {
        return R.layout.activity_master;
    }

    @Override
    protected Fragment createFragment() {
        return MovieListFragment.newInstance();
    }

    @Override
    public void onMovieInsert(){
        if (findViewById(R.id.detailFragmentContainer) != null) {

            startFragment(-1);

        } else {

            Intent i = new Intent(this, MovieDetailActivity.class);
            startActivityForResult(i, MovieListFragment.INSERT_NEW_MOVIE);
        }
    }

    /**
     * When a movie is selected from the list, determine if the view
     * should be split or a new window displayed if tablet or phone
     * @param id Id of movie to be updated
     */
    @Override
    public void onMovieEdit(long id){

        // If the fragment container is not null or rather screen is a tablet
        if (findViewById(R.id.detailFragmentContainer) != null) {
            startFragment(id);
            // Start the split screen fragment
        } else {
            // Else start a normal movie detail fragment to edit
            Intent i = new Intent(this, MovieDetailActivity.class);

            i.putExtra(MovieDetailFragment.EXTRA_ID, id);
            startActivityForResult(i, MovieListFragment.INSERT_NEW_MOVIE);
        }

    }

    /**
     * Called when save is pressed on the movie detail screen to update
     * the movie list
     */
    @Override
    public void onMovieUpdate() {
        FragmentManager fm = getFragmentManager();

        // Get the container for the movie list
        MovieListFragment movieListFragment = (MovieListFragment)
                fm.findFragmentById(R.id.fragmentContainer);

        // Update the UI
        movieListFragment.updateUI();
    }

    /**
     * Start a separate pane for movie editing
     * @param id Id of movie to start a detail fragment for
     */
    private void startFragment(long id) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        // Get the old fragment and the new fragment.
        Fragment oldFragment =
                fm.findFragmentById(R.id.detailFragmentContainer);
        Fragment newFragment = MovieDetailFragment.newInstance(id);

        // Remove the old fragment if it exists
        if (oldFragment != null) ft.remove(oldFragment);

        // Add the new fragment with the fragment container
        ft.add(R.id.detailFragmentContainer, newFragment);
        // Commit the new binding
        ft.commit();
    }
}
