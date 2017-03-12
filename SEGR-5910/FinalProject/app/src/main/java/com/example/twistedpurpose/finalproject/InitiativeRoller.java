package com.example.twistedpurpose.finalproject;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Helper class for rolling initiative and sorting characters by ininitative
 */
public class InitiativeRoller {

    /**
     * Rolls initiative for characters.
     * @param characterList - List of characters to have initiative rolled for
     * @return - The list of characters in sorted order
     */
    public static List<Character> rollInitiative(List<Character> characterList){
        for(Character c : characterList) {
            c.setInitiative(DiceRoller.rollD20());
        }

        return characterList;
    }

    public static void sortInInitiativeOrder(List<Character> list) {

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
    }
}
