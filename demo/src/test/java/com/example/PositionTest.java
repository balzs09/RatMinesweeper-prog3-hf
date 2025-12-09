package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @brief A Position osztály működését ellenőrző JUnit tesztosztály.
 * 
 * Teszteli a Position konstruktorát, az equals metódust és a hashCode metódust.
 * Ellenőrzi, hogy a sor- és oszlopszámok helyesen kerülnek beállításra,
 * azonos koordinátájú pozíciók egyenlőek legyenek, és a hashCode
 * értékek megfeleljenek az equals metódus logikájának.
 */
public class PositionTest {

    /**
     * Teszteli, hogy a Position konstruktor a megadott
     * sor- és oszlopszámot helyesen állítja be.
     */
    @Test
    public void testPositionConstructorInitialValues() {
        Position pos = new Position(0, 0);
        assertEquals(0, pos.getRow());
        assertEquals(0, pos.getColumn());
    }

    /**
     * Teszteli a Position equals metódusát.
     * Azonos koordinátákkal rendelkező pozíciók legyenek egyenlőek,
     * eltérők pedig ne.
     */
    @Test
    public void testEquals() {
        Position pos1 = new Position(1, 1);
        Position pos2 = new Position(1, 2);
        Position pos3 = new Position(1, 2);
        assertTrue(pos2.equals(pos3));
        assertFalse(pos1.equals(pos2));
    }

    /**
     * Teszteli, hogy azonos pozíciók azonos hashCode értéket kapnak,
     * míg különböző pozíciók különbözőt.
     */
    @Test
    public void testHashCode() {
        Position pos1 = new Position(1, 1);
        Position pos2 = new Position(1, 2);
        Position pos3 = new Position(1, 2);
        assertEquals(pos2.hashCode(), pos3.hashCode());
        assertNotEquals(pos1.hashCode(), pos3.hashCode());
    }
}
