package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @brief A Rat osztály működését ellenőrző JUnit tesztosztály.
 * 
 * Teszteli a Rat konstruktorát és a pozíciók kezelését.  
 * Ellenőrzi, hogy a kezdőpozíció helyesen kerül beállításra, 
 * és hogy a currentPosition és goalPosition pozíciók helye megfelelően 
 * módosítható a setter metódusok segítségével.
 */
public class RatTest {

    /**
     * Teszteli, hogy a Rat konstruktora helyesen állítja be
     * a kezdő és a cél pozíciót azonos értékre.
     */
    @Test
    public void testRatConstructorInitialValues() {
        Position currentPos = new Position(0, 0);
        Rat rat = new Rat(currentPos);
        assertEquals(rat.getCurrentPosition(), currentPos);
        assertEquals(rat.getGoalPosition(), currentPos);
    }

    /**
     * Teszteli, hogy a Rat objektum current és goal pozíciói
     * helyesen módosíthatók a setter metódusokkal.
     */
    @Test
    public void testSetCurrentAndGoal() {
        Position currentPos = new Position(1, 1);
        Position goalPos = new Position(2, 3);
        Rat rat = new Rat(new Position(0, 0));
        rat.setCurrentPosition(currentPos);
        rat.setGoal(goalPos);
        assertEquals(rat.getCurrentPosition(), currentPos);
        assertEquals(rat.getGoalPosition(), goalPos);

    }
}
