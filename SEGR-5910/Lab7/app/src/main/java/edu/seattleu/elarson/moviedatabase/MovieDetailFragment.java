package edu.seattleu.elarson.moviedatabase;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
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

/**
 * View and edit movie details.  Will update movie title on movie list frame.
 */
public class MovieDetailFragment extends Fragment
implements PickUrlDialogFragment.OnPickUrlDialogListener{

    public static final String EXTRA_ID = "edu.seattleu.elarson.moviedatabase.id";

    private OnMovieDetailListener mListener;
    private Movie mMovie;
    private MovieDatabaseHelper mHelper;
    private EditText urlEditText;

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

    /**
     * Sets the url in movie details when the user selects a url from the dialog
     * @param url - url to be set in movie details from dialog
     */
    @Override
    public void selectUrl(String url) {
        if (urlEditText != null) {
            // Set the text view for the url in movie details
            urlEditText.setText(url);
        }
    }

    /**
     * Interface to allow updates of the movie list on a seperate
     * pane
     */
    public interface OnMovieDetailListener {
        void onMovieUpdate();
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

    /**
     * Set the mListener in case a movie is edited
     * @param context context of the window
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMovieDetailListener) {
            mListener = (OnMovieDetailListener) context;
        }
    }

    // Movie detail listener cleanup
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

        urlEditText = (EditText) v.findViewById(R.id.urlEditText);
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

                // If the listener is not null, update the movie on the movie list pane
                if (mListener != null)
                {
                    mListener.onMovieUpdate();
                }
            }
        });


        /**
         * When pick url button is selected, a dialog will appear with url options.
         * When one is selected, the url text view will show the new URL
         */
        Button urlPickButton = (Button) v.findViewById(R.id.pickUrlButton);
        urlPickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Setup for a new fragment
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                // Create a new instance of a url picker dialog
                PickUrlDialogFragment dialog = PickUrlDialogFragment.newInstance();

                // Show the new dialog on the screen
                dialog.show(ft, "dialog");
            }
        });

        return v;
    }
}
