package edu.seattleu.elarson.moviedatabase;

public class Movie {

    private long mId;
    private String mTitle;
    private String mGenre;
    private String mUrl;
    private float mRating;
    private boolean mWatched;

    public Movie() {
        mRating = 0.0f;
        mWatched = false;
    }

    public Movie(String title, String genre, String url) {
        mTitle = title;
        mGenre = genre;
        mUrl = url;
        mRating = 0.0f;
        mWatched = false;
    }

    public long getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getGenre() {
        return mGenre;
    }

    public String getUrl() {
        return mUrl;
    }

    public float getRating() {
        return mRating;
    }

    public boolean isWatched() {
        return mWatched;
    }

    public void setId(long id) {
        mId = id;
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
