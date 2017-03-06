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
            // Add function to roll dice
            // Add class to roll different sided dice
            int init = (int)(Math.random() * 20 + 1);
            c.setInitiative(init);
        }

        // Break out to add tie buisness logic
        Collections.sort(characterList, new Comparator<Character>() {
            public int compare(Character character1, Character character2) {
                return character2.getTotalInitiative()- character1.getTotalInitiative();
            }
        });

        return characterList;
    }
}
