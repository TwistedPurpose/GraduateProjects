package com.example.twistedpurpose.movieviewer;

/**
 * A movie object that contains a movie's title, genre, and url.
 */

class Movie {
    // Title of the movie
    private String mTitle;

    // Genre of the movie
    private String mGenre;

    // String of url for where to find more information about the movie
    private String mUrl;

    // A floating point number that represents the rating of the movie
    private float mRating;

    // If true, it means the movie is watched, else the movie hasn't been
    // watched yet
    private boolean mWatched;

    // Constructor that takes a title, genre, url
    Movie(String title, String genre, String url){
        mTitle = title;
        mGenre = genre;
        mUrl = url;
        mRating = 0.0f;
        mWatched = false;
    }

    String getTitle(){
        return mTitle;
    }

    String getGenre(){
        return mGenre;
    }

    String getUrl() {
        return mUrl;
    }

    float getRating() {
        return mRating;
    }

    public boolean isWatched() {
        return mWatched;
    }
    public void setTitle(String title) {
        mTitle = title;
    }

    public void setGenre(String genre) {
        mGenre = genre;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public void setRating(float rating) {
        mRating = rating;
    }

    public void setWatched(boolean watched) {
        mWatched = watched;
    }
}
