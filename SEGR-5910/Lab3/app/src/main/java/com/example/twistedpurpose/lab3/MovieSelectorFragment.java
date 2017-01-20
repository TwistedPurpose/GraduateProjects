package com.example.twistedpurpose.lab3;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * A fragment for selecting movies and intitiating views of movie information
 */
public class MovieSelectorFragment extends Fragment {

    // Marker tracks where in the array
    private int mCurrentIndex = 0;

    // Listerner for View is called, will initiate bringing up
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
     *
     * @return A new instance of fragment MovieSelectorFragment.
     */
    public static MovieSelectorFragment newInstance() {
        return new MovieSelectorFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Saves the instance so data doesn't reset on rotation
        setRetainInstance(true);
    }

    // On creation of the view, setup listeners and data
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movie_selector, container, false);

        // Gets the list of movies and sets it to private variable
        mMovies = MovieList.get().getMovies();

        // Gets the TextView objects for title and genre for later modifications
        mMovieTitle = (TextView)v.findViewById(R.id.textMovieTitle);
        mGenre = (TextView)v.findViewById(R.id.textMovieGenre);

        // Setup up the Next button listener on the interface
        Button nextButton = (Button)v.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex += 1;
                setMovieInfo(v);
            }
        });

        // Setup up the Previous button listener on the interface
        Button prevButton = (Button)v.findViewById(R.id.prevButton);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex -= 1;
                setMovieInfo(v);
            }
        });

        // Setup up the View button listener on the interface
        Button viewButton = (Button)v.findViewById(R.id.viewButton);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onMovieView(mMovies.get(getMovieIndex()).getUrl());
                }
            }
        });

        // Set the starter info
        setMovieInfo(v);

        return v;
    }

    // Gets the rolling index of the currently viewed movie
    private int getMovieIndex(){
        return Math.abs(mCurrentIndex % (mMovies.size()));
    }


    // Set movie info on the view
    private void setMovieInfo(View v) {
        Movie movie = mMovies.get(getMovieIndex());

        mMovieTitle.setText(movie.getTitle());
        mGenre.setText(movie.getGenre());
    }

    // Sets the listener for movie views
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMovieViewListener) {
            mListener = (OnMovieViewListener) context;
        }
    }

    // Movie view listerner cleanup
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // When the movie listerner is called, application attempts to view movie information
    public interface OnMovieViewListener {
        public void onMovieView(String url);
    }
}
