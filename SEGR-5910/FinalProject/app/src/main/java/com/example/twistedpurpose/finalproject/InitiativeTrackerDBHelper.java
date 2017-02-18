package com.example.twistedpurpose.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Twisted Purpose on 2/17/2017.
 */

public class InitiativeTrackerDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "characters.db";
    private static final int DATABASE_VERSION = 1;

    private static final class Characters implements BaseColumns {
        private Characters() {}
        public static final String TABLE_NAME = "characters";
        public static final String NAME = "name";
        public static final String MODIFIER = "modifier";
        public static final String INITIATIVE = "initiative";
    }

    public InitiativeTrackerDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Characters.TABLE_NAME + " ("
                + Characters._ID + " integer primary key autoincrement, "
                + Characters.NAME + " text, "
                + Characters.MODIFIER + " integer, "
                + Characters.INITIATIVE + " integer "
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not used but required to implement (SQLiteOpenHelper)
    }

    // Inserts a blank new character into the database, returns the character with
    // the id number set.
    public void addCharacter(Character c) {

        //Create a new Character object
        Character character = c;

        // Gets the associated row with the character
        ContentValues row = getCharacterRow(character);

        // Fetch the database to insert to
        SQLiteDatabase db = getWritableDatabase();

        // Inserts in thew new character
        long id = db.insert(Characters.TABLE_NAME, null, row);
        character.setId(id);
    }

    // Inserts a blank new character into the database, returns the character with
    // the id number set.
    public Character addCharacter() {

        //Create a new Character object
        Character character = new Character();

        // Gets the associated row with the character
        ContentValues row = getCharacterRow(character);

        // Fetch the database to insert to
        SQLiteDatabase db = getWritableDatabase();

        // Inserts in thew new character
        long id = db.insert(Characters.TABLE_NAME, null, row);
        character.setId(id);

        return character;
    }

    // Updates the character in the database.
    public void updateCharacter(Character character) {

        // Fetch content row specific to the character being passed in
        ContentValues row = getCharacterRow(character);

        // Set the where clause to id of character to be updated
        String whereClause = Characters._ID + "=" +
                String.valueOf(character.getId());

        // Update the database with the character object
        getWritableDatabase().update(
                Characters.TABLE_NAME,
                row, whereClause, null);
    }

    // Updates the character in the database.
    public void updateCharacters(List<Character> characters) {

        for(Character character : characters){
            // Fetch content row specific to the character being passed in
            ContentValues row = getCharacterRow(character);

            // Set the where clause to id of character to be updated
            String whereClause = Characters._ID + "=" +
                    String.valueOf(character.getId());

            // Update the database with the character object
            getWritableDatabase().update(
                    Characters.TABLE_NAME,
                    row, whereClause, null);
        }
    }

    // Returns a cursor that has a list for all characters.
    public CharacterCursor queryCharacters() {

        // Setup a query to fetch all movies in table
        Cursor cursor = getReadableDatabase().query(
                Characters.TABLE_NAME, // table name
                new String[] {Characters._ID, Characters.NAME,
                        Characters.MODIFIER,Characters.INITIATIVE,
                        "("+ Characters.MODIFIER + "+" +Characters.INITIATIVE+") as TotalInit",
                } , // columns (all)
                null, // where (all rows)
                null, // whereArgs
                null, // group by
                null, // having
                "TotalInit desc", // order by
                null);

        // return the cursor for the all movies query
        return new CharacterCursor(cursor);
    }

    // Queries the database for the character with the corresponding id.  Returns the character
    // or null if the character does not exist in the database.
    public List<Character> getCharacters() {
        Character character;

        // Fetch a character equal to the id passed into the function
        // to get a single character
        Cursor cursor = getReadableDatabase().query(
                Characters.TABLE_NAME, // table name
                new String[] {Characters._ID, Characters.NAME,
                        Characters.MODIFIER,Characters.INITIATIVE,
                        "("+ Characters.MODIFIER + "+" +Characters.INITIATIVE+") as TotalInit",
                } , // columns
                null, // where all
                null, // whereArgs
                null, // group by
                null, // having
                "TotalInit desc", // order by
                null);

        // Pass the cursor into a character cursor
        CharacterCursor characterCursor =
                new CharacterCursor(cursor);


        // Set the cursor to the searched for character
        characterCursor.moveToFirst();

        List<Character> characterList = new ArrayList<>();

        while (!characterCursor.isAfterLast()) {
            character = characterCursor.getCharacter();
        }
        // Fetch the character from the location of the cursor


        // Close out the cursor for cleanup
        characterCursor.close();

        // Send back character to be consumed.
        return character;
    }

    // Queries the database for the character with the corresponding id.  Returns the character
    // or null if the character does not exist in the database.
    public Character getCharacter(long id) {
        Character character;

        // Fetch a character equal to the id passed into the function
        // to get a single character
        Cursor cursor = getReadableDatabase().query(
                Characters.TABLE_NAME, // table name
                null, // columns (all)
                null, // where all
                null, // whereArgs
                null, // group by
                null, // having
                Characters.NAME + " asc", // order by
                null);

        // Pass the cursor into a character cursor
        CharacterCursor characterCursor =
                new CharacterCursor(cursor);

        // Set the cursor to the searched for character
        characterCursor.moveToFirst();

        // Fetch the character from the location of the cursor
        character = characterCursor.getCharacter();

        // Close out the cursor for cleanup
        characterCursor.close();

        // Send back character to be consumed.
        return character;
    }



    /**
     * Sets up a DB row for character updates and modification
     * @param character Movie to be filled into ContentsValue
     * @return Row of the character to be modified
     */
    private ContentValues getCharacterRow(Character character) {
        ContentValues cv = new ContentValues();

        cv.put(Characters.NAME, character.getName());
        cv.put(Characters.MODIFIER, character.getModifier());
        cv.put(Characters.INITIATIVE, character.getInitiative());

        return cv;
    }

    /**
     * A cursor wrapper specifically for characters
     * Allows for abstraction of fetching characters at a row
     */
    public static class CharacterCursor extends CursorWrapper {

        public CharacterCursor(Cursor cursor) {
            super(cursor);
        }

        // Returns the character at the current cursor location.  Returns null
        // if the cursor is before the first record or after the last record.
        public Character getCharacter() {

            // If cursor is out of bounds, return null
            if (isBeforeFirst() || isAfterLast()) return null;

            Character character = new Character();

            //Get values of character
            character.setId(getLong(getColumnIndex(Characters._ID)));
            character.setName(getString(getColumnIndex(Characters.NAME)));
            character.setModifier(getInt(getColumnIndex(Characters.MODIFIER)));
            character.setInitiative(getInt(getColumnIndex(Characters.INITIATIVE)));

            return character;
        }
    }

}
