package com.example.twistedpurpose.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.MatrixCursor;
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

    // Column headers for Characters table
    private static final class Characters implements BaseColumns {
        private Characters() {}
        public static final String TABLE_NAME = "characters";
        public static final String NAME = "name";
        public static final String MODIFIER = "modifier";
        public static final String INITIATIVE = "initiative";
        public static final String HAS_SPOTLIGHT = "spotlight";
    }

    public InitiativeTrackerDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Set up initial db
     * @param db - character database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Characters.TABLE_NAME + " ("
                + Characters._ID + " integer primary key autoincrement, "
                + Characters.NAME + " text, "
                + Characters.MODIFIER + " integer, "
                + Characters.INITIATIVE + " integer, "
                + Characters.HAS_SPOTLIGHT + " integer "
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not used but required to implement (SQLiteOpenHelper)
    }

    // Inserts a blank new character into the database, returns the character with
    // the id number set.
    public Character addCharacter() {

        //Create a new Character object
        Character character = new Character();

        // If there are no other characters, set this one to be
        // in the spotlight
        if(getCharacters().size() == 0) {
            character.setInSpotlight(true);
        }

        // Gets the associated row with the character
        ContentValues row = getCharacterRow(character);

        // Fetch the database to insert to
        SQLiteDatabase db = getWritableDatabase();

        // Inserts in thew new character
        long id = db.insert(Characters.TABLE_NAME, null, row);
        character.setId(id);

        db.close();

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

    // Returns a cursor that has a list for all characters.
    public CharacterCursor getCharacterCursor() {

        // Column headers for db to be added to matrix cursor for character list
        String[] columns = new String[] { Characters._ID, Characters.NAME, Characters.MODIFIER,
                Characters.INITIATIVE, Characters.HAS_SPOTLIGHT };

        //Initial setup for cursor for list view
        MatrixCursor matrixCursor = new MatrixCursor(columns);

        //Get all characters
        List<Character> list = getCharacters();

        for(Character c : list){
            int spotlight = 0;

            // if the character has spotlight, set spotlight on the cursor character
            if (c.isInSpotlight()) {
                spotlight = 1;
            }

            // Add the character to the cursor
            matrixCursor.addRow(new Object[] {c.getId(), c.getName(), c.getModifier(),
                    c.getInitiative(), spotlight});
        }

        // return the cursor for the all movies query
        return new CharacterCursor(matrixCursor);
    }

    // Queries the database for the character with the corresponding id.  Returns the character
    // or null if the character does not exist in the database.
    public List<Character> getCharacters() {
        List<Character> characterList = new ArrayList<>();

        // Fetch a list of all characters
        Cursor cursor = getReadableDatabase().query(
                Characters.TABLE_NAME, // table name
                null, // columns all
                null, // where all
                null, // whereArgs
                null, // group by
                null, // having
                null, // order by
                null);

        // Pass the cursor into a character cursor
        CharacterCursor characterCursor =
                new CharacterCursor(cursor);

        // Set the cursor to the searched for character
        characterCursor.moveToFirst();

        while (!characterCursor.isAfterLast()) {

            // Fetch the character from the location of the cursor
            characterList.add(characterCursor.getCharacter());
            characterCursor.moveToNext();
        }

        // Close out the cursor for cleanup
        characterCursor.close();

        InitiativeRoller.sortInInitiativeOrder(characterList);

        // Send back character to be consumed.
        return characterList;
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
                Characters._ID + "=" + id, // where character_id = id
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
        cv.put(Characters.HAS_SPOTLIGHT, character.isInSpotlight());

        return cv;
    }

    /**
     * Deletes a character from the database based on character id
     * @param character - Character to be delted from the database
     */
    public void deleteCharacter(Character character) {
        //If the character is in the spot light, set it to the next
        //Character in the list
        if (character.isInSpotlight()) {
            updateNextCharacter(false);
        }

        // Fetch the database to insert to
        // Set the where clause to id of character to be updated
        String whereClause = Characters._ID + "= ?";
        String[] whereArgs = {String.valueOf(character.getId())};

        // Update the database with the character object
        getWritableDatabase().delete(Characters.TABLE_NAME, whereClause, whereArgs);
    }

    /**
     * Set the highlight of the next character in combat,
     * if character is the last in the list, set it to the top of the list
     * @param setAsFirst - If the caller wishes to set the top of the list
     *                      as highlighted, set to true, else false
     */
    public void updateNextCharacter(boolean setAsFirst) {
        //Get all the characters
        List<Character> characters = getCharacters();

        // If the size is zero, don't do anything
        if(characters.size() > 0) {
            // Set next to the top if the caller
            // wishes to set to top, or the list has wrapped
            Character current = null;
            Character next = characters.get(0);

            for (Character c: characters) {
                //Set the current spotlighted character
                if (c.isInSpotlight()) {
                    current = c;
                } else if (current != null && !setAsFirst) {
                    //If current is found, set the next one and break
                    next = c;
                    break;
                }
            }

            // If current and next are found
            // Switch off spotlight on current, and turn on
            // for next
            if (current != null && next != null) {
                current.setInSpotlight(false);
                next.setInSpotlight(true);

                updateCharacter(current);
                updateCharacter(next);
            }
        }
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
            character.setInSpotlight(getInt(getColumnIndex(Characters.HAS_SPOTLIGHT)) != 0);

            return character;
        }
    }

}
