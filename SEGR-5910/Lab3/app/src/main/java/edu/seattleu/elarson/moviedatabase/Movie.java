package edu.seattleu.elarson.moviedatabase;

/**
 * A movie object that contains a movie's title, genre, url
 */

class Movie {
    private String mTitle;
    private String mGenre;
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
