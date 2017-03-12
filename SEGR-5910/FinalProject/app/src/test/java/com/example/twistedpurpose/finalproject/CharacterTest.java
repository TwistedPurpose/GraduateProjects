package com.example.twistedpurpose.finalproject;

import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterTest {

    @Test
    public void Character_Character_EmptyConstructor() throws Exception {
        //Arrange
        Character c = null;

        //Act
        c = new Character();

        //Assert
        assertTrue(c.getName().equals(""));
        assertTrue(c.getInitiative() == 0);
        assertTrue(c.getTotalInitiative() == 0);
        assertTrue(c.getModifier() == 0);
    }

    @Test
    public void Character_Character_BasicConstructor() throws Exception {
        //Arrange
        Character c = null;

        //Act
        c = new Character("Soren",1);

        //Assert
        assertTrue(c.getName().equals("Soren"));
        assertTrue(c.getInitiative() == 0);
        assertTrue(c.getTotalInitiative() == 1);
        assertTrue(c.getModifier() == 1);
    }

    @Test
    public void Character_Character_NumberToString() throws Exception {
        //Arrange
        Character c = null;

        //Act
        c = new Character("Soren",1);
        c.setInitiative(10);

        //Assert
        assertTrue(c.getInitiativeAsString().equals("10"));
        assertTrue(c.getTotalInitiativeAsString().equals("11"));
        assertTrue(c.getModifierAsString().equals("1"));
    }
}
