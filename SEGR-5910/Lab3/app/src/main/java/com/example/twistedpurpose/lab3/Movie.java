package com.example.twistedpurpose.lab3;

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

    // Constructor that takes a title, genre, url
    Movie(String title, String genre, String url){
        mTitle = title;
        mGenre = genre;
        mUrl = url;
    }

    String getTitle(){
        return mTitle;
    }

    String getGenre(){
        return mGenre;
    }

    String getUrl() { return mUrl; }
}
