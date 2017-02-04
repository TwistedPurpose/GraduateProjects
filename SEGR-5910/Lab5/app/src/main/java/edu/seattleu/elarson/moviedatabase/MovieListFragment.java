package edu.seattleu.elarson.moviedatabase;


import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import static edu.seattleu.elarson.moviedatabase.MovieDatabaseHelper.MovieCursor;

public class MovieListFragment extends ListFragment {
    private static final int INSERT_NEW_MOVIE = 0;

    private MovieCursor mCursor;

    public static MovieListFragment newInstance() {
        return new MovieListFragment();
    }

    /**
     *  On create of the fragment,
     *  This will create a db helper, query for a list of movies,
     *  create an adaptor using the cursor provided by the helper,
     *  And set the list adaptor
     * @param savedInstanceState Saved state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        // NOTE: If you create your database table incorrectly or want a fresh one,
        // uncomment the following line (and don't use the Up button):
        // getActivity().deleteDatabase("movies.db")

        Context context = getActivity();

        // 1. Create a new MovieDatabaseHelper
        MovieDatabaseHelper dbHelper = new MovieDatabaseHelper(context);

        // 2. Query the movies and obtain a cursor (store in mCursor).
        mCursor = dbHelper.queryMovies();

        // 3. Create a new MovieCursorAdapter using the cursor.
        MovieCursorAdapter adapter = new MovieCursorAdapter(context, mCursor);

        // 4. Set the list adapter using the MovieCursorAdapter.
        setListAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        mCursor.close();
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_movie_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_item_add:
                Intent i = new Intent(getActivity(), MovieDetailActivity.class);
                startActivityForResult(i, INSERT_NEW_MOVIE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // The id argument will be the movie ID; CursorAdapter gives us this for free
        Intent i = new Intent(getActivity(), MovieDetailActivity.class);
        i.putExtra(MovieDetailFragment.EXTRA_ID, id);
        startActivityForResult(i, INSERT_NEW_MOVIE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INSERT_NEW_MOVIE) {
            mCursor.requery();
            ((MovieCursorAdapter)getListAdapter()).notifyDataSetChanged();
        }
    }

    /**
     * A movie cursor adaptor for adding movie titles
     * to a list
     */
    private static class MovieCursorAdapter extends CursorAdapter {

        private MovieCursor mMovieCursor;

        public MovieCursorAdapter(Context context, MovieCursor cursor) {
            super(context, cursor, 0);
            mMovieCursor = cursor;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            // Use a layout inflater to get a row view
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        // The android.R.layout.simple_list_item_1 layout consists of a single text view
        // so the view passed into this function is a textView.  Cast the view into a
        // TextView and set the text to the movie title.  Get the title of the movie
        // using the mMovieCursor member.
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // View will always be a text view for the list
            // So cast it to TextView
            TextView simpleList = (TextView) view;

            // Set the text to be the title of the movie
            simpleList.setText(mMovieCursor.getMovie().getTitle());
        }
    }

}
