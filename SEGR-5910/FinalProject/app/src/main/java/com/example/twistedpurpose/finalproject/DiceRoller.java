package com.example.twistedpurpose.finalproject;


/**
 * Mechanism for rolling dice
 * There is a function for every standard polyhedron
 * for d20 games.
 *
 * Most of these are unused since there was no plan in implementing them
 * They are simply here by the request of my professor.
 */
public class DiceRoller {
    public static int rollD4(){
        return (int)(Math.random() * 4 + 1);
    }

    public static int rollD6(){
        return (int)(Math.random() * 6 + 1);
    }

    public static int rollD8(){
        return (int)(Math.random() * 8 + 1);
    }

    public static int rollD10(){
        return (int)(Math.random() * 10 + 1);
    }

    public static int rollD12(){
        return (int)(Math.random() * 12 + 1);
    }

    public static int rollD20(){
        return (int)(Math.random() * 20 + 1);
    }
}
