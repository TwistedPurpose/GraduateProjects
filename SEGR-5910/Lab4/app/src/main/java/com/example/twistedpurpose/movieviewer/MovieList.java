package com.example.twistedpurpose.movieviewer;

import java.util.ArrayList;

/**
 * An object for holding multiple movie objects
 */

class MovieList {

    // A singleton of the movie list object
    private static MovieList sMovieList = null;

    // An array of all the movies
    private ArrayList<Movie> mMovies;

    // Private constructor for a list of preset movies
    private MovieList() {
        mMovies = new ArrayList<>();
        mMovies.add(new Movie("Frozen", "animated","http://frozen.disney.com/"));
        mMovies.add(new Movie("Star Wars", "sci-fi", "http://www.starwars.com/"));
        mMovies.add(new Movie("The Sound of Music", "musical", "http://www.imdb.com/title/tt0059742/"));
        mMovies.add(new Movie("Back to the Future", "comedy","http://www.imdb.com/title/tt0088763/"));
        mMovies.add(new Movie("The Shining", "horror", "http://www.imdb.com/title/tt0081505/"));
    }

    // Gets the list of movies from singleton movie list
    static MovieList get(){
        // If the movie list doesn't exist, create it
        if (sMovieList == null){
            sMovieList = new MovieList();
        }
        return sMovieList;
    }

    // An array list of all the movies
    ArrayList<Movie> getMovies(){
        return mMovies;
    }
}
