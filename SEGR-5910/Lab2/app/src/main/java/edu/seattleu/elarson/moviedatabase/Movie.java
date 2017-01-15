package edu.seattleu.elarson.moviedatabase;

/**
 * Created by Twisted Purpose on 1/12/2017.
 */

class Movie {
    private String mTitle;
    private String mGenre;

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
