package edu.seattleu.elarson.moviedatabase;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * By Soren Ludwig
 * Main controller for the app
 */
public class MainActivity extends ActionBarActivity {

    // Marker tracks where in the array
    private int mMarker = 0;

    // Move int iterator forward for movie list
    private final View.OnClickListener btnNext = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMarker += 1;
            setMovieInfo();
        }
    };

    // Move int iterator backward
    private final View.OnClickListener btnPrev = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMarker -= 1;
            setMovieInfo();
        }
    };

    // Set movie info on the view
    private void setMovieInfo() {
        ArrayList<Movie> movieList = MovieList.get().getMovies();
        Movie movie = movieList.get(Math.abs(mMarker % (movieList.size())));

        TextView movieTitle = (TextView)findViewById(R.id.textMovieTitle);
        TextView genre = (TextView)findViewById(R.id.textMovieGenre);

        movieTitle.setText(movie.getTitle());
        genre.setText(movie.getGenre());
    }

    // Saves the state when phone is rotated
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putInt(getResources().getString(R.string.movie_state), mMarker);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Restores state if rotated
        if (savedInstanceState != null) {
            mMarker = savedInstanceState.getInt(getResources().getString(R.string.movie_state));
        } else {
            mMarker = 0;
        }

        Button nextButton = (Button)findViewById(R.id.nextButton);
        nextButton.setOnClickListener(btnNext);

        Button prevButton = (Button)findViewById(R.id.prevButton);
        prevButton.setOnClickListener(btnPrev);

        // Set the starter info
        setMovieInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
