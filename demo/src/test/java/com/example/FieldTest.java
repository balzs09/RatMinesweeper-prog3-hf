package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
/**
 * @brief A Field osztály metódusainak tesztelésért felelős osztály.
 * 
 * Ez az osztály a Field osztály metódusainak helyes működését vizsgálja,
 * beleértve a zászlókezelést, a szomszédos bombák számát és a mezők állapotát
 * meghatározó logikát.
 */
public class FieldTest {
    /**
     * Teszteli a Field konstruktorát alapértelmezett értékekkel.
     * Ellenőrzi, hogy az aknamentes mező megfelelő kezdőállapotot kap.
     */
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

    /**
     * Teszteli a Field konstruktorát alapértelmezett értékekkel.
     * Ellenőrzi, hogy az aknamentes mező megfelelő kezdőállapotot kap.
     */
    @Test
    public void testSetMineNumber() {
        Field f = new Field(false);
        f.setMineNumber(2);
        assertTrue(f.getIsMine());
        assertEquals(2, f.getMineNumber());
    }

    /**
     * Teszteli, hogy a setNumberOfNeighbors helyesen állítja be
     * a szomszédos mezők számát.
     */
    @Test
    public void testSetNumberOfNeighbors() {
        Field f = new Field(false);
        f.setNumberOfNeighbors(4);
        assertEquals(4, f.getNumberOfNeighbors());
    }

    /**
     * Teszteli a mező felfedettségének beállítását.
     */
    @Test
    public void testSetRevealed() {
        Field f = new Field(false);
        assertFalse(f.getRevealed());
        f.setRevealed(true);
        assertTrue(f.getRevealed());
    }

    /**
     * Teszteli, hogy a zászlóként kijelölés állapota helyesen frissül.
     */
    @Test
    public void testSetFlagged() {
        Field f = new Field(false);
        assertFalse(f.getFlagged());
        f.setFlagged(true);
        assertTrue(f.getFlagged());
    }

    /**
     * Teszteli a zászlózás logikáját alapértelmezett játékmódban:
     * 1 -> 0 ciklust
     */
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

    /**
     * *
     * Teszteli a RAT mód több szintű zászlózását:
     * 1 -> 2 -> 3 -> 0 ciklust.
     */
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

    /**
     * Teszteli, hogy a resetFlags visszaállítja a zászlózást 0-ra
     * és eltávolítja a jelölést a mezőről.
     */
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
