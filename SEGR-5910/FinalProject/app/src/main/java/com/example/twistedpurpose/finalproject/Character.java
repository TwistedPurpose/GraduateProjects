package com.example.twistedpurpose.finalproject;

/**
 * Created by Twisted Purpose on 2/16/2017.
 */

public class Character {
    private String mName;
    private int mModifier;
    private int mInitiative;

    public Character(String name, int mod) {
        mName = name;
        mModifier = mod;
        mInitiative = 0;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getModifier() {
        return mModifier;
    }

    public void setModifier(int modifier) {
        mModifier = modifier;
    }

    public int getInitiative() {
        return mInitiative;
    }

    public void setInitiative(int initiative) {
        mInitiative = initiative;
    }
}
