package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class DefaultTableTest {

  /**
   * Teszteli, hogy a DefaultTable konstruktora helyesen állítja be
   * a sorok, oszlopok, összes bánya és az egyaknás mezők számát.
   */
  @Test
  public void testDefaultTableConstructor() {
    DefaultTable dt = new DefaultTable(5, 9, 8);
    assertEquals(5, dt.getRows());
    assertEquals(9, dt.getColumns());
    assertEquals(8, dt.getAllMines());
    assertEquals(8, dt.getOneMineFields());
  }

  /**
   * Teszteli, hogy a getAvailableFields nem ad vissza olyan mezőt,
   * amely a kiválasztott pozícióban van, vagy annak szomszédja.
   */
  @Test
  public void testGetAvailableFields() {
    DefaultTable dt = new DefaultTable(5, 5, 10);
    Position selected = new Position(2, 2);
    ArrayList<Field> available = dt.getavailableFields(selected);
    List<Position> forbidden = dt.getNeighborPositions(selected);
    forbidden.add(selected);
    for (Position p : forbidden) {
      assertFalse(available.contains(dt.getFieldByPosition(p)));
    }
  }

  /**
   * Teszteli, hogy a selectingMines a megadott elérhető mezők közül
   * pontosan a kívánt számú mezőt jelöl ki bombaként.
   */
  @Test
  public void testSelectingMines() {
    DefaultTable dt = new DefaultTable(5, 5, 5);
    Position selected = new Position(2, 2);
    ArrayList<Field> available = dt.getavailableFields(selected);
    dt.selectingMines(available);
    int mineNumber = 0;
    for (Field field : available)
      if (field.getIsMine())
        mineNumber++;

    assertEquals(5, mineNumber);
  }

  /**
   * Teszteli, hogy a selectingMines a megadott elérhető mezők közül
   * pontosan a kívánt számú mezőt jelöl ki bányaként.
   */
  @Test
  public void testCheckNeighbors() {
    DefaultTable dt = new DefaultTable(6, 6, 2);
    dt.getFieldByPosition(new Position(0, 0)).setMineNumber(1);
    dt.getFieldByPosition(new Position(1, 1)).setMineNumber(1);
    dt.checkNeighbors();
    assertEquals(2, dt.getFieldByPosition(new Position(0, 1)).getNumberOfNeighbors());
    assertEquals(0, dt.getFieldByPosition(new Position(4, 4)).getNumberOfNeighbors());
    assertEquals(1, dt.getFieldByPosition(new Position(0, 2)).getNumberOfNeighbors());
  }

  /**
   * Teszteli, hogy a getNeighborPositions megfelelő számú
   * szomszédos pozíciót ad vissza sarok-, szél- és belső mezőre.
   */
  @Test
  public void testGetNeighborPositions() {
    DefaultTable dt = new DefaultTable(3, 3, 0);
    assertEquals(3, dt.getNeighborPositions(new Position(0, 0)).size());
    assertEquals(5, dt.getNeighborPositions(new Position(1, 0)).size());
    assertEquals(8, dt.getNeighborPositions(new Position(1, 1)).size());
  }

  /**
   * Teszteli, hogy különböző pozíciókhoz külön Field objektum
   * tartozik, vagyis nem ugyanazt az példányt adja vissza.
   */
  @Test
  public void testGetFieldByPosition() {
    DefaultTable dt = new DefaultTable(4, 4, 0);
    Field f1 = dt.getFieldByPosition(new Position(0, 0));
    Field f2 = dt.getFieldByPosition(new Position(0, 1));
    assertNotSame(f1, f2);
  }

  /**
   * Teszteli, hogy különböző pozíciókhoz külön Field objektum
   * tartozik, vagyis nem ugyanazt az instance-t adja vissza.
   */
  @Test
  public void testGetPositionByField() {
    DefaultTable dt = new DefaultTable(3, 3, 0);
    Field f = dt.getFieldByPosition(new Position(1, 2));
    Position p = dt.getPositionByField(f);
    assertEquals(1, p.getRow());
    assertEquals(2, p.getColumn());
  }

}
