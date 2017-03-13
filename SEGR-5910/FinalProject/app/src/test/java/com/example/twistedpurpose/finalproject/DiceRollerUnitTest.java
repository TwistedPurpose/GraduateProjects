package com.example.twistedpurpose.finalproject;

import org.junit.Test;

import static org.junit.Assert.*;

public class DiceRollerUnitTest {
    @Test
    public void DiceRoller_rollD4_AverageTest() {
        //Arrange
        int roll;

        for (int i = 0; i < 100; i++){
            //Act
            roll = DiceRoller.rollD4();

            //Assert
            assertTrue(roll <= 4);
            assertTrue(roll >= 1);
        }
    }

    @Test
    public void DiceRoller_rollD6_AverageTest() {
        //Arrange
        int roll;

        for (int i = 0; i < 100; i++){
            //Act
            roll = DiceRoller.rollD6();

            //Assert
            assertTrue(roll <= 6);
            assertTrue(roll >= 1);
        }
    }

    @Test
    public void DiceRoller_rollD8_AverageTest() {
        //Arrange
        int roll;

        for (int i = 0; i < 100; i++){
            //Act
            roll = DiceRoller.rollD8();

            //Assert
            assertTrue(roll <= 8);
            assertTrue(roll >= 1);
        }
    }

    @Test
    public void DiceRoller_rollD10_AverageTest() {
        //Arrange
        int roll;

        for (int i = 0; i < 100; i++){
            //Act
            roll = DiceRoller.rollD10();

            //Assert
            assertTrue(roll <= 10);
            assertTrue(roll >= 1);
        }
    }

    @Test
    public void DiceRoller_rollD12_AverageTest() {
        //Arrange
        int roll;

        for (int i = 0; i < 100; i++){
            //Act
            roll = DiceRoller.rollD12();

            //Assert
            assertTrue(roll <= 12);
            assertTrue(roll >= 1);
        }
    }

    @Test
    public void DiceRoller_rollD20_AverageTest() {
        //Arrange
        int roll;

        for (int i = 0; i < 100; i++){
            //Act
            roll = DiceRoller.rollD20();

            //Assert
            assertTrue(roll <= 20);
            assertTrue(roll >= 1);
        }
    }
}