package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {
    @Test
    public void testPositionConstructorInitialValues() {
        Position pos = new Position(0, 0);
        assertEquals(0, pos.getRow());
        assertEquals(0, pos.getColumn());
    }

    @Test
    public void testEquals() {
        Position pos1 = new Position(1, 1);
        Position pos2 = new Position(1, 2);
        Position pos3 = new Position(1, 2);
        assertTrue(pos2.equals(pos3));
        assertFalse(pos1.equals(pos2));
    }

    @Test
    public void testHashCode() {
        Position pos1 = new Position(1, 1);
        Position pos2 = new Position(1, 2);
        Position pos3 = new Position(1, 2);
        assertEquals(pos2.hashCode(), pos3.hashCode());
        assertNotEquals(pos1.hashCode(), pos3.hashCode());
    }
}
