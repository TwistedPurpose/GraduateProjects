package com.example.twistedpurpose.finalproject;

/**
 * Created by Twisted Purpose on 3/3/2017.
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
