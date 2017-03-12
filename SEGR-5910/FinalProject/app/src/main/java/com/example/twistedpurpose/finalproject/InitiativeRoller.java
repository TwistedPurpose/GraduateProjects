package com.example.twistedpurpose.finalproject;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Helper class for rolling initiative and sorting characters by initiative
 */
public class InitiativeRoller {

    /**
     * Rolls initiative for characters.
     *
     * @param characterList - List of characters to have initiative rolled for
     * @return - The list of characters in sorted order
     */
    public static List<Character> rollInitiative(List<Character> characterList) {

        if (characterList != null) {
            // Roll init for all characters
            for (Character c : characterList) {
                c.setInitiative(DiceRoller.rollD20());
            }

        }

        return characterList;
    }

    /**
     * Sorts a character list based on total initiative
     *
     * @param list - List to be sorted
     */
    public static void sortInInitiativeOrder(List<Character> list) {

        if (list != null) {
            //Do a sort!
            Collections.sort(list, new Comparator<Character>() {

                @Override
                public int compare(Character o1, Character o2) {
                    int difference = 0;

                    //If they total inits are different, get the difference
                    if (o2.getTotalInitiative() - o1.getTotalInitiative() != 0) {
                        difference = o2.getTotalInitiative() - o1.getTotalInitiative();
                    } else if (o2.getModifier() - o1.getModifier() != 0) {
                        // If the total init is the same, use the modifier
                        difference = o2.getModifier() - o1.getModifier();
                    } else {
                        //If the total init and the modifier are the same
                        //Do a roll off, the higher roller wins!
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
}
