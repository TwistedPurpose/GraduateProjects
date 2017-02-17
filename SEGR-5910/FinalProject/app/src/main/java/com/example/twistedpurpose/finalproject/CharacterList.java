package com.example.twistedpurpose.finalproject;

/**
 * Created by Twisted Purpose on 2/16/2017.
 */

import java.util.ArrayList;

/**
 * An object for holding a list of characters
 */

class CharacterList {

    // A singleton of the character list object
    private static CharacterList sCharacterList = null;

    // An array of all the characters
    private ArrayList<Character> mCharacters;

    // Private constructor for a list of preset characters
    private CharacterList() {
        mCharacters = new ArrayList<>();
        mCharacters.add(new Character("Mike", 2));
        mCharacters.add(new Character("Otyog", -2));
        mCharacters.add(new Character("Soren", 1));
        mCharacters.add(new Character("Stravis", 5));
    }

    // Gets the list of characters from singleton character list
    static CharacterList get(){
        // If the character list doesn't exist, create it
        if (sCharacterList == null){
            sCharacterList = new CharacterList();
        }
        return sCharacterList;
    }

    // An array list of all the characters
    ArrayList<Character> getCharacters(){
        return mCharacters;
    }
}
