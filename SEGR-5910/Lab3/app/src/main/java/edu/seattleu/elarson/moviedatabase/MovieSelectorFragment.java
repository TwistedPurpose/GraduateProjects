package edu.seattleu.elarson.moviedatabase;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MovieSelectorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieSelectorFragment extends Fragment {

    //private OnFragmentInteractionListener mListener;

    // Marker tracks where in the array
    private int mCurrentIndex = 0;

    private String mTextMovieTitle;
    private String mTextMovieGenre;
    private List<Movie> mMovies;

    // Move int iterator forward for movie list
    private final View.OnClickListener btnNext = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCurrentIndex += 1;
        }
    };

    // Move int iterator backward
    private final View.OnClickListener btnPrev = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCurrentIndex -= 1;
        }
    };

    public MovieSelectorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MovieSelectorFragment.
     */
    public static MovieSelectorFragment newInstance() {
        return new MovieSelectorFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movie_selector, container, false);

        mMovies = MovieList.get().getMovies();

        Button nextButton = (Button)v.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(btnNext);

        Button prevButton = (Button)v.findViewById(R.id.prevButton);
        prevButton.setOnClickListener(btnPrev);

        // Set the starter info
        setMovieInfo(v);

        return v;
    }

    // Set movie info on the view
    private void setMovieInfo(View v) {
        Movie movie = mMovies.get(Math.abs(mCurrentIndex % (mMovies.size())));

        TextView movieTitle = (TextView)v.findViewById(R.id.textMovieTitle);
        TextView genre = (TextView)v.findViewById(R.id.textMovieGenre);

        movieTitle.setText(movie.getTitle());
        genre.setText(movie.getGenre());
    }

    /*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
