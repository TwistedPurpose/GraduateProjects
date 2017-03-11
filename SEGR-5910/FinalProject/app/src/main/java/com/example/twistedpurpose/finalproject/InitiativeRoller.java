package com.example.twistedpurpose.finalproject;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Twisted Purpose on 2/17/2017.
 */

public class InitiativeRoller {

    public static List<Character> rollInitiative(List<Character> characterList){
        for(Character c : characterList) {
            c.setInitiative(DiceRoller.rollD20());
        }

        return characterList;
    }
}
