package edu.seattleu.elarson.moviedatabase;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class MovieDetailFragment extends Fragment {

    public static final String EXTRA_ID = "edu.seattleu.elarson.moviedatabase.id";

    private Movie mMovie;
    private MovieDatabaseHelper mHelper;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id Id of movie to access.
     * @return A new instance of fragment MovieDetailFragment.
     */
    public static MovieDetailFragment newInstance(long id) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putLong(EXTRA_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long id = 0;
        if (getArguments() != null) {
            id = getArguments().getLong(EXTRA_ID, -1);
        }

        mHelper = new MovieDatabaseHelper(getActivity());
        if (id == -1) {
            mMovie = mHelper.insertMovie();
        } else {
            mMovie = mHelper.getMovie(id);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        TextView idTextView = (TextView) v.findViewById(R.id.idTextView);
        idTextView.setText(String.valueOf(mMovie.getId()));

        EditText movieEditText = (EditText) v.findViewById(R.id.titleEditText);
        movieEditText.setText(mMovie.getTitle());
        movieEditText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mMovie.setTitle(c.toString());
            }

            public void beforeTextChanged(CharSequence c, int start, int before, int after) {
            }

            public void afterTextChanged(Editable c) {
            }
        });

        EditText genreEditText = (EditText) v.findViewById(R.id.genreEditText);
        genreEditText.setText(mMovie.getGenre());
        genreEditText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mMovie.setGenre(c.toString());
            }

            public void beforeTextChanged(CharSequence c, int start, int before, int after) {
            }

            public void afterTextChanged(Editable c) {
            }
        });

        EditText urlEditText = (EditText) v.findViewById(R.id.urlEditText);
        urlEditText.setText(mMovie.getUrl());
        urlEditText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mMovie.setUrl(c.toString());
            }

            public void beforeTextChanged(CharSequence c, int start, int before, int after) {
            }

            public void afterTextChanged(Editable c) {
            }
        });

        CheckBox watchedCheckBox = (CheckBox) v.findViewById(R.id.watchedCheckBox);
        watchedCheckBox.setChecked(mMovie.isWatched());
        watchedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mMovie.setWatched(isChecked);
            }
        });

        RatingBar ratingRatingBar = (RatingBar) v.findViewById(R.id.ratingRatingBar);
        ratingRatingBar.setRating(mMovie.getRating());
        ratingRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mMovie.setRating(rating);
            }
        });

        Button viewWebButton = (Button) v.findViewById(R.id.viewWebButton);
        viewWebButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(getActivity(), MovieWebActivity.class);
                intent.putExtra(MovieWebFragment.EXTRA_URL, mMovie.getUrl());
                startActivity(intent);
            }
        });

        Button saveButton = (Button) v.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mHelper.updateMovie(mMovie);
                Toast.makeText(getActivity(), "Update complete!", Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }
}
