package com.example.twistedpurpose.finalproject;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class InitiativeRollerTest {

    @Test
    public void InitiativeRollerTest_rollInitiative_NullTest() throws Exception {
        //Arrange
        List<Character> characterList = null;

        //Act
        InitiativeRoller.rollInitiative(characterList);

        //Assert
        //Will throw exception if fails
    }

    @Test
    public void InitiativeRollerTest_rollInitiative_EmptyList() throws Exception {
        //Arrange
        List<Character> characterList = new ArrayList<>();

        //Act
        InitiativeRoller.rollInitiative(characterList);

        //Assert
        assertTrue(characterList.size() == 0);
    }

    @Test
    public void InitiativeRollerTest_rollInitiative_OneCharacter() throws Exception {
        //Arrange
        List<Character> characterList = new ArrayList<>();

        characterList.add(new Character("Mike",-5));

        //Act
        InitiativeRoller.rollInitiative(characterList);

        //Assert
        assertTrue(characterList.size() == 1);
        assertTrue(characterList.get(0).getInitiative() >= 1);
        assertTrue(characterList.get(0).getInitiative() <= 20);
    }

    @Test
    public void InitiativeRollerTest_rollInitiative_MultipleCharacters() throws Exception {
        //Arrange
        List<Character> characterList = new ArrayList<>();

        characterList.add(new Character("Mike",-5));
        characterList.add(new Character("Soren",10));
        characterList.add(new Character("Dragon",10));

        //Act
        InitiativeRoller.rollInitiative(characterList);

        //Assert
        assertTrue(characterList.size() > 1);
        for(Character c : characterList){

            assertTrue(characterList.get(0).getInitiative() >= 1);
            assertTrue(characterList.get(0).getInitiative() <= 20);

        }
    }

    @Test
    public void InitiativeRollerTest_sortInInitiativeOrder_EmptyList() throws Exception {
        //Arrange
        List<Character> characterList = new ArrayList<>();

        //Act
        InitiativeRoller.sortInInitiativeOrder(characterList);

        //Assert
        assertTrue(characterList.size() == 0);
    }

    @Test
    public void InitiativeRollerTest_sortInInitiativeOrder_NullTest() throws Exception {
        //Arrange
        List<Character> characterList = null;

        //Act
        InitiativeRoller.sortInInitiativeOrder(characterList);

        //Assert
        //Will throw exception if fails
    }

    @Test
    public void InitiativeRollerTest_sortInInitiativeOrder_OneCharacter() throws Exception {
        //Arrange
        List<Character> characterList = new ArrayList<>();

        characterList.add(new Character("Mike",-5));

        //Act
        InitiativeRoller.sortInInitiativeOrder(characterList);

        //Assert
        assertTrue(characterList.size() == 1);
        assertTrue(characterList.get(0).getName().equals("Mike"));
    }

    @Test
    public void InitiativeRollerTest_sortInInitiativeOrder_DifferentInitiative() throws Exception {
        //Arrange
        List<Character> characterList = new ArrayList<>();

        Character c1 = new Character("Mike",-5);
        Character c2 = new Character("Soren",10);

        c1.setInitiative(1);
        c2.setInitiative(5);

        characterList.add(c1);
        characterList.add(c2);

        //Act
        InitiativeRoller.sortInInitiativeOrder(characterList);

        //Assert
        assertTrue(characterList.get(0).getName().equals("Soren"));
        assertTrue(characterList.get(1).getName().equals("Mike"));
    }

    @Test
    public void InitiativeRollerTest_sortInInitiativeOrder_DifferentModifier() throws Exception {
        //Arrange
        List<Character> characterList = new ArrayList<>();

        Character c1 = new Character("Mike",-5);
        Character c2 = new Character("Soren",10);

        c1.setInitiative(20);
        c2.setInitiative(5);

        characterList.add(c1);
        characterList.add(c2);

        //Act
        InitiativeRoller.sortInInitiativeOrder(characterList);

        //Assert
        assertTrue(characterList.get(0).getName().equals("Soren"));
        assertTrue(characterList.get(1).getName().equals("Mike"));
    }

    @Test
    public void InitiativeRollerTest_sortInInitiativeOrder_RollOff() throws Exception {
        //Arrange
        List<Character> characterList = new ArrayList<>();

        Character c1 = new Character("Mike",10);
        Character c2 = new Character("Soren",10);

        c1.setInitiative(10);
        c2.setInitiative(10);

        characterList.add(c1);
        characterList.add(c2);

        //Act
        InitiativeRoller.sortInInitiativeOrder(characterList);

        //Assert
        if(characterList.get(0).getName().equals("Soren")) {
            assertTrue(characterList.get(1).getName().equals("Mike"));
        } else {
            assertTrue(characterList.get(1).getName().equals("Soren"));
        }
    }
}
