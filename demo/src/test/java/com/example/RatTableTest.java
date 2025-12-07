package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class RatTableTest {
  /**
   * Teszteli, hogy a RatTable konstruktora helyesen állítja be
   * a sorok, oszlopok, összes bánya és a különböző típusú
   * aknamezők számát.
   */
  @Test
  public void testRatTableConstructorInitialValues() {
    RatTable rt = new RatTable(5, 9, 8);
    assertEquals(5, rt.getRows());
    assertEquals(9, rt.getColumns());
    assertEquals(8, rt.getAllMines());
    assertEquals(4, rt.getOneMineFields());
    assertEquals(2, rt.getTwoMineFields());
    assertEquals(2, rt.getThreeMineFields());
  }

  /**
   * Teszteli, hogy a setRat metódus megfelelően beállítja
   * az egér aktuális és célpozícióját.
   */
  @Test
  public void testSetRat() {
    RatTable rt = new RatTable(5, 9, 8);
    Position pos = new Position(2, 4);
    rt.setRat(pos);
    assertEquals(pos, rt.getRat().getCurrentPosition());
    assertEquals(pos, rt.getRat().getGoalPosition());
  }

  /**
   * Teszteli, hogy a RatTable konstruktora helyesen állítja be
   * a sorok, oszlopok, összes bánya és a különböző típusú
   * aknamezők számát.
   */
  @Test
  public void testGetNeighborPositions() {
    RatTable rt = new RatTable(5, 5, 0);
    assertEquals(8, rt.getNeighborPositions(new Position(0, 0)).size());
    assertEquals(8, rt.getNeighborPositions(new Position(2, 0)).size());
    assertEquals(8, rt.getNeighborPositions(new Position(1, 1)).size());
  }

  /**
   * Teszteli, hogy a checkNeighbors helyesen számolja ki
   * a szomszédos mezők aknaszámát a beállított bombák alapján.
   */
  @Test
  public void testCheckNeighbors() {
    RatTable dt = new RatTable(6, 6, 2);
    dt.getFieldByPosition(new Position(0, 0)).setMineNumber(1);
    dt.getFieldByPosition(new Position(1, 1)).setMineNumber(3);
    dt.checkNeighbors();
    assertEquals(4, dt.getFieldByPosition(new Position(0, 1)).getNumberOfNeighbors());
    assertEquals(3, dt.getFieldByPosition(new Position(2, 2)).getNumberOfNeighbors());
    assertEquals(1, dt.getFieldByPosition(new Position(5, 5)).getNumberOfNeighbors());
  }

  /**
   * Teszteli, hogy a bombák kiválasztása megfelelően történik:
   * a mezők aknaszámai és a mezők száma helyesen alakul.
   */
  @Test
  public void testSelectingMines() {
    RatTable rt = new RatTable(9, 9, 8);
    Position selected = new Position(2, 2);
    ArrayList<Field> available = rt.getavailableFields(selected);
    rt.selectingMines(available);
    int mineNumber = 0;
    int mineFieldNumber = 0;
    for (Field field : available)
      if (field.getIsMine()) {
        mineFieldNumber++;
        mineNumber += field.getMineNumber();
      }

    assertEquals(1, available.get(0).getMineNumber());
    assertEquals(2, available.get(5).getMineNumber());
    assertEquals(3, available.get(6).getMineNumber());
    assertEquals(8, mineFieldNumber);
    assertEquals(14, mineNumber);
  }

  /**
   * Teszteli, hogy a bányák kiválasztása megfelelően történik:
   * a mezők aknaszámai és a mezők száma helyesen alakul.
   */
  @Test
  public void testGetRightDirectionNeighbors() {
    RatTable rt = new RatTable(5, 5, 0);
    Position start = new Position(2, 2);
    Position goal = new Position(4, 4);
    rt.setRat(start);
    rt.getRat().setGoal(goal);
    List<Position> neighbors = rt.getRightDirectionNeighbors(start);
    for (Position pos : neighbors) {
      assertTrue(Math.abs(pos.getRow() - goal.getRow()) <= Math.abs(start.getRow() - goal.getRow()));
      assertTrue(Math.abs(pos.getColumn() - goal.getColumn()) <= Math.abs(start.getColumn() - goal.getColumn()));
    }
  }

  /**
   * Teszteli, hogy a bányák kiválasztása megfelelően történik:
   * a mezők aknaszámai és a mezők száma helyesen alakul.
   */
  @Test
  public void testRatMovementSimple() {
    RatTable rt = new RatTable(3, 3, 0);
    Position start = new Position(0, 0);
    Position goal = new Position(2, 2);
    rt.setRat(start);
    rt.getRat().setGoal(goal);
    rt.setShortestPath();
    List<Position> path = rt.getShortestPath();
    assertNotNull(path);
    assertFalse(path.isEmpty());
    assertEquals(start, path.get(0));
    assertEquals(goal, path.get(path.size() - 1));
    assertEquals(3, path.size());
  }

}
