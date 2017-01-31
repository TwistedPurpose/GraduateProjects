package com.example.twistedpurpose.movieviewer;

import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RatingBar;

/**
 * Fragment for editing movie information
 * Updates movie object when a field is modified
 */
public class MovieDetailFragment extends Fragment {
    public static final String MOVIE_INDEX = "movie_index";

    // Current movie index for the movie list
    private int movieIndex;

    // Current movie object, holds all the data about a movie
    private Movie movie;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param index Index of movie to be edited
     * @return A new instance of fragment MovieDetailFragment.
     */
    public static MovieDetailFragment newInstance(int index) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();

        args.putInt(MOVIE_INDEX, index);
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * Required function by android for on creation of fragment
     * @param savedInstanceState - Saved instance data
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            movieIndex = getArguments().getInt(MOVIE_INDEX);
        }
    }

    /**
     * Sets all initial data and listeners for movie details fragment
     * @param inflater Inflates the view of the details page
     * @param container - Container for view
     * @param savedInstanceState - Saved instance data
     * @return - View to be modified
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment\
        View v = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        // Fetch the singleton movie list and the current index to edit
        movie = MovieList.get().getMovies().get(movieIndex);

        // Get all the editable fields by ID
        EditText titleEditText = (EditText) v.findViewById(R.id.editTextMovieTitle);
        EditText genreEditText = (EditText) v.findViewById(R.id.editTextMovieGenre);
        EditText urlEditText = (EditText) v.findViewById(R.id.editTextMovieURL);
        CheckBox watchedCheckBox = (CheckBox) v.findViewById(R.id.checkBoxWatchedMovie);
        RatingBar movieRating = (RatingBar) v.findViewById(R.id.ratingBarMovieRating);

        // Initialize
        titleEditText.setText(movie.getTitle());
        genreEditText.setText(movie.getGenre());
        urlEditText.setText(movie.getUrl());
        watchedCheckBox.setChecked(movie.isWatched());
        movieRating.setRating(movie.getRating());

        // Setting of listeners
        // For every field, if there is an update made
        // update the current movie object with new data
        titleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals("")) {
                    movie.setTitle(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        genreEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals("")) {
                    movie.setGenre(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        urlEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals("")) {
                    movie.setUrl(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        watchedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                movie.setWatched(isChecked);
            }
        });

        movieRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                movie.setRating(rating);
            }
        });

        return v;
    }

}
