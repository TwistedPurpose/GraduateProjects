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

    public static List<Character> sortInInitiativeOrder(List<Character> list) {

        Collections.sort(list, new Comparator<Character>() {

            @Override
            public int compare(Character o1, Character o2) {
                int difference = 0;

                if (o2.getTotalInitiative() - o1.getTotalInitiative() != 0) {
                    difference =  o2.getTotalInitiative() - o1.getTotalInitiative();
                } else if (o2.getModifier() - o1.getModifier() != 0){
                    difference = o2.getModifier() - o1.getModifier();
                } else {
                    int characterOneRoll = 0;
                    int characterTwoRoll = 0;

                    while (characterOneRoll == characterTwoRoll) {
                        characterOneRoll = DiceRoller.rollD20();
                        characterTwoRoll = DiceRoller.rollD20();
                    }

                    difference = characterTwoRoll - characterOneRoll;
                }

                return difference;
            }
        });

        return list;
    }
}
