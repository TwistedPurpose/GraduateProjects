package com.example.twistedpurpose.finalproject;


/**
 * A class for characters
 * Contains a character name, initiative modifier,
 * and rolled initiative along with
 * helper functions for calculating total initiative
 * converting various
 */
public class Character {

    //Database ID
    private long mId;

    //Name of the character
    private String mName;

    //Initiative modifier for characters
    private int mModifier;

    //d20 roll for initiative
    private int mInitiative;

    //Check for if a character is currently acting in combat
    private boolean mInSpotlight;

    public Character() {
        mName = "";
        mModifier = 0;
        mInitiative = 0;
    }

    public Character(String name, int mod) {
        mName = name;
        mModifier = mod;
        mInitiative = 0;
    }

    //Fetches the modifier as a string... duh
    public String getModifierAsString() {
        return Integer.toString(getModifier());
    }

    //Gets the sum initiative as a string also
    public String getTotalInitiativeAsString() {
        return Integer.toString(getTotalInitiative());
    }

    //Get the total initiative as a string
    public String getInitiativeAsString() {
        return Integer.toString(getInitiative());
    }

    public long getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public int getModifier() {
        return mModifier;
    }

    public int getInitiative() {
        return mInitiative;
    }

    public boolean isInSpotlight() {
        return mInSpotlight;
    }

    public int getTotalInitiative() {
        return mInitiative + mModifier;
    }

    public void setId(long id) {
        mId = id;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setModifier(int modifier) {
        mModifier = modifier;
    }

    public void setInitiative(int initiative) {
        mInitiative = initiative;
    }

    public void setInSpotlight(boolean inSpotlight) {
        mInSpotlight = inSpotlight;
    }
}
