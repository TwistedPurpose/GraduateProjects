package edu.seattleu.elarson.moviedatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class MovieDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    private static final class Movies implements BaseColumns {
        private Movies() {}
        public static final String TABLE_NAME = "movies";
        public static final String TITLE = "title";
        public static final String GENRE = "genre";
        public static final String URL = "url";
        public static final String RATING = "rating";
        public static final String WATCHED = "watched";
    }

    public MovieDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Sets up initial db
     * @param db Database to be filled with Movie table
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Movies.TABLE_NAME + " ("
                + Movies._ID + " integer primary key autoincrement, "
                + Movies.TITLE + " text, "
                + Movies.GENRE + " text, "
                + Movies.URL + " text, "
                + Movies.RATING + " real, "
                + Movies.WATCHED + " integer"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not used but required to implement (SQLiteOpenHelper)
    }

    // Inserts a blank new movie into the database, returns the movie with
    // the id number set.
    public Movie insertMovie() {

        //Create a new movie object
        Movie movie = new Movie();

        // Gets the associated row with the movie
        ContentValues row = getMovieRow(movie);

        // Fetch the database to insert to
        SQLiteDatabase db = getWritableDatabase();

        // Inserts in thew new movie
        long id = db.insert(Movies.TABLE_NAME, null, row);
        movie.setId(id);

        return movie;
    }

    // Updates the movie in the database.
    public void updateMovie(Movie movie) {

        // Fetch content row specific to the movie being passed in
        ContentValues row = getMovieRow(movie);

        // Set the where clause to id of movie to be updated
        String whereClause = Movies._ID + "=" +
                String.valueOf(movie.getId());

        // Update the database with the movie object
        getWritableDatabase().update(
                Movies.TABLE_NAME,
                row, whereClause, null);
    }

    // Returns a cursor that has a list for all movies.
    public MovieCursor queryMovies() {

        // Setup a query to fetch all movies in table
        Cursor cursor = getReadableDatabase().query(
                Movies.TABLE_NAME, // table name
                null, // columns (all)
                null, // where (all rows)
                null, // whereArgs
                null, // group by
                null, // having
                Movies.TITLE + " asc", // order by
                null);

        // return the cursor for the all movies query
        return new MovieCursor(cursor);
    }

    // Queries the database for the movie with the corresponding id.  Returns the movie
    // or null if the movie does not exist in the database.
    public Movie getMovie(long id) {
        Movie movie;

        // Fetch a movie equal to the id passed into the function
        // to get a single movie
        Cursor cursor = getReadableDatabase().query(
                Movies.TABLE_NAME, // table name
                null, // columns (all)
                Movies._ID + "=" + id, // where movie_id = id
                null, // whereArgs
                null, // group by
                null, // having
                Movies.TITLE + " asc", // order by
                null);

        // Pass the cursor into a movie cursor
        MovieCursor movieCursor =
                new MovieCursor(cursor);

        // Set the cursor to the searched for movie
        movieCursor.moveToFirst();

        // Fetch the movie from the location of the cursor
        movie = movieCursor.getMovie();

        // Close out the cursor for cleanup
        movieCursor.close();

        // Send back movie to be consumed.
        return movie;
    }

    /**
     * Sets up a DB row for movie updates and modification
     * @param movie Movie to be filled into ContentsValue
     * @return Row of the movie to be modified
     */
    private ContentValues getMovieRow(Movie movie) {
        ContentValues cv = new ContentValues();

        cv.put(Movies.TITLE, movie.getTitle());
        cv.put(Movies.GENRE, movie.getGenre());
        cv.put(Movies.URL, movie.getUrl());
        cv.put(Movies.RATING, movie.getRating());
        cv.put(Movies.WATCHED, movie.isWatched() ? 1 : 0);

        return cv;
    }

    /**
     * A cursor wrapper specifically for movies
     * Allows for abstraction of fetching movies at a row
     */
    public static class MovieCursor extends CursorWrapper {

        public MovieCursor(Cursor cursor) {
            super(cursor);
        }

        // Returns the movie at the current cursor location.  Returns null
        // if the cursor is before the first record or after the last record.
        public Movie getMovie() {

            // If cursor is out of bounds, return null
            if (isBeforeFirst() || isAfterLast()) return null;

            Movie movie = new Movie();

            //Get values of movie
            movie.setId(getLong(getColumnIndex(Movies._ID)));
            movie.setTitle(getString(getColumnIndex(Movies.TITLE)));
            movie.setGenre(getString(getColumnIndex(Movies.GENRE)));
            movie.setUrl(getString(getColumnIndex(Movies.URL)));
            movie.setRating(getFloat(getColumnIndex(Movies.RATING)));
            movie.setWatched(getInt(getColumnIndex(Movies.WATCHED)) != 0);

            return movie;
        }
    }

}
