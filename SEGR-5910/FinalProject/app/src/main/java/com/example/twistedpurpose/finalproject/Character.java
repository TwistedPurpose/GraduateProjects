package com.example.twistedpurpose.finalproject;

/**
 * Created by Twisted Purpose on 2/16/2017.
 */

public class Character {
    private long mId;
    private String mName;
    private int mModifier;
    private int mInitiative;

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
