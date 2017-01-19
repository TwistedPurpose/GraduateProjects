package edu.seattleu.elarson.moviedatabase;

import java.util.ArrayList;

/**
 * An object for holding multiple movie objects
 */

public class MovieList {

    private static MovieList sMovieList = null;

    private ArrayList<Movie> mMovies;

    // Private constructor for a list of preset movies
    private MovieList() {
        mMovies = new ArrayList<Movie>();
        mMovies.add(new Movie("Frozen", "animated","http://frozen.disney.com/"));
        mMovies.add(new Movie("Star Wars", "sci-fi", "http://www.starwars.com/"));
        mMovies.add(new Movie("The Sound of Music", "musical", "http://www.imdb.com/title/tt0059742/"));
        mMovies.add(new Movie("Back to the Future", "comedy","http://www.imdb.com/title/tt0088763/"));
        mMovies.add(new Movie("The Shining", "horror", "http://www.imdb.com/title/tt0081505/"));
    }

    // Gets the list of movies from singlton movie list
    public static MovieList get(){
        // If the movie list doesn't exist, create it
        if (sMovieList == null){
            sMovieList = new MovieList();
        }
        return sMovieList;
    }

    public ArrayList<Movie> getMovies (){
        return mMovies;
    }
}
