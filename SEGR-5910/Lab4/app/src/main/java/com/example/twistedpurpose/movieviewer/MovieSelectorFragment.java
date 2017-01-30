package com.example.twistedpurpose.movieviewer;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.view.Menu;

import java.util.List;

/**
 * A fragment for selecting movies to display title and genre
 * and starts views of movie information
 * in the web
 */
public class MovieSelectorFragment extends Fragment {

    // Marker tracks where in the array
    private int mCurrentIndex = 0;

    // Listener for View is called, will initiate bringing up
    // a web interface
    private OnMovieViewListener mListener;

    // List of movies to browse
    private List<Movie> mMovies;

    // Objects on the interface that will show title and genre
    private TextView mMovieTitle;
    private TextView mGenre;

    public MovieSelectorFragment() {
        // Required empty public constructor
    }

    /**
     * Creates a new MovieSelector Fragment
     * Used to view movie titles, genres, and act as a gateway for viewing
     * online content for a movie
     *
     * @return A new instance of fragment MovieSelectorFragment.
     */
    public static MovieSelectorFragment newInstance() {
        return new MovieSelectorFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // Saves the instance so data doesn't reset on rotation
        setRetainInstance(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.movie_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_edit:
                if (mListener != null) {
                    mListener.onMovieEdit(getMovieIndex());
                }
                return true;
            case R.id.menu_item_web:
                if (mListener != null) {
                    mListener.onMovieView(mMovies.get(getMovieIndex()).getUrl());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * When the view is created, set the buttons, listeners
     * and movie info on the panel
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movie_selector, container, false);

        // Gets the list of movies and sets it to private variable
        mMovies = MovieList.get().getMovies();

        // Gets the TextView objects for title and genre for later modifications
        mMovieTitle = (TextView) v.findViewById(R.id.textMovieTitle);
        mGenre = (TextView) v.findViewById(R.id.textMovieGenre);

        // Setup up the Next button listener on the interface
        Button nextButton = (Button) v.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increase the current index to move right
                mCurrentIndex += 1;
                // Display the new movie title and genre
                setMovieInfo();
            }
        });

        // Setup up the Previous button listener on the interface
        Button prevButton = (Button) v.findViewById(R.id.prevButton);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Decrease the current index to move left
                mCurrentIndex -= 1;
                // Display the new movie title and genre
                setMovieInfo();
            }
        });

        // Setup up the View button listener on the interface
        Button viewButton = (Button) v.findViewById(R.id.viewButton);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onMovieView(mMovies.get(getMovieIndex()).getUrl());
            }
            }
        });

        // Set the starter info
        setMovieInfo();

        return v;
    }

    /*
        Gets the index of the currently viewed
     */
    private int getMovieIndex() {
        return Math.abs(mCurrentIndex % (mMovies.size()));
    }


    /*
    Set movie info on the view
    This will display the current movie title and genre.
    It loads the current movie, and displays the Title and Genre field the current values.
    Call this each time to display the movie information.
    */
    private void setMovieInfo() {
        Movie movie = mMovies.get(getMovieIndex());

        mMovieTitle.setText(movie.getTitle());
        mGenre.setText(movie.getGenre());
    }

    /*
        When this fragment is attached the OnMovieListener will be initialized
        This is used to create a view of the movie url resolution
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMovieViewListener) {
            mListener = (OnMovieViewListener) context;
        }
    }

    // Movie view listener cleanup
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * Interface for when user views the movie and edits
     */
    public interface OnMovieViewListener {
        void onMovieView(String url);
        void onMovieEdit(int index);
    }

    /**
     * When the activity resumes, refresh the screen by setting the movie
     * Information to the current info
     */
    @Override
    public void onResume() {
        super.onResume();
        setMovieInfo();
    }
}
