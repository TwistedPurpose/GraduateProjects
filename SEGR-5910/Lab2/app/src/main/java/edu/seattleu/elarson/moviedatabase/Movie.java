package edu.seattleu.elarson.moviedatabase;

/**
 * A movie object that contains a movie's title and genre
 */

class Movie {
    private String mTitle;
    private String mGenre;

    // Constructor that takes a title and genre
    Movie(String title, String genre){
        mTitle = title;
        mGenre = genre;
    }

    String getTitle(){
        return mTitle;
    }

    String getGenre(){
        return mGenre;
    }
}
