package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FieldTest {
    @Test
    public void testFieldConstructor() {
        Field f = new Field(false);
        assertFalse(f.getIsMine());
        assertEquals(0, f.getMineNumber());
        assertEquals(0, f.getNumberOfNeighbors());
        assertFalse(f.getFlagged());
        assertFalse(f.getRevealed());
        assertEquals(0, f.getFlags());
    }

    @Test
    public void testSetMineNumber() {
        Field f = new Field(false);
        f.setMineNumber(2);
        assertTrue(f.getIsMine());
        assertEquals(2, f.getMineNumber());
    }

    @Test
    public void testSetNumberOfNeighbors() {
        Field f = new Field(false);
        f.setNumberOfNeighbors(4);
        assertEquals(4, f.getNumberOfNeighbors());
    }

    @Test
    public void testSetRevealed() {
        Field f = new Field(false);
        assertFalse(f.getRevealed());
        f.setRevealed(true);
        assertTrue(f.getRevealed());
    }

    @Test
    public void testSetFlagged() {
        Field f = new Field(false);
        assertFalse(f.getFlagged());
        f.setFlagged(true);
        assertTrue(f.getFlagged());
    }

    @Test
    public void testIncrementFlagsDefaultMode() {
        Field f = new Field(false);
        f.incrementFlags(GameModes.DEFAULT);
        assertTrue(f.getFlagged());
        assertEquals(1, f.getFlags());
        f.incrementFlags(GameModes.DEFAULT);
        assertFalse(f.getFlagged());
        assertEquals(0, f.getFlags());
    }

    @Test
    void testIncrementFlagsRatMode() {
        Field f = new Field(false);
        f.incrementFlags(GameModes.RAT);
        assertEquals(1, f.getFlags());
        f.incrementFlags(GameModes.RAT);
        assertEquals(2, f.getFlags());
        f.incrementFlags(GameModes.RAT);
        assertEquals(3, f.getFlags());
        f.incrementFlags(GameModes.RAT);
        assertEquals(0, f.getFlags());
        assertFalse(f.getFlagged());
    }

    @Test
    void testResetFlags() {
        Field f = new Field(false);
        f.incrementFlags(GameModes.RAT);
        f.incrementFlags(GameModes.RAT);
        f.resetFlags();
        assertEquals(0, f.getFlags());
        assertFalse(f.getFlagged());
    }
}
