package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DefaultTableTest {
  @Test
  public void testDefaultTableConstructor() {
    DefaultTable dt = new DefaultTable(5, 9, 8);
    assertEquals(5, dt.getRows());
    assertEquals(9, dt.getColumns());
    assertEquals(8, dt.getAllMines());
    assertEquals(8, dt.getOneMineFields());
  }

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

  @Test
  public void testGetNeighborPositions() {
    DefaultTable dt = new DefaultTable(3, 3, 0);
    assertEquals(3, dt.getNeighborPositions(new Position(0, 0)).size());
    assertEquals(5, dt.getNeighborPositions(new Position(1, 0)).size());
    assertEquals(8, dt.getNeighborPositions(new Position(1, 1)).size());
  }

  @Test
  public void testGetFieldByPosition() {
    DefaultTable dt = new DefaultTable(4, 4, 0);
    Field f1 = dt.getFieldByPosition(new Position(0, 0));
    Field f2 = dt.getFieldByPosition(new Position(0, 1));
    assertNotSame(f1, f2);
  }

  @Test
  public void testGetPositionByField() {
    DefaultTable dt = new DefaultTable(3, 3, 0);
    Field f = dt.getFieldByPosition(new Position(1, 2));
    Position p = dt.getPositionByField(f);
    assertEquals(1, p.getRow());
    assertEquals(2, p.getColumn());
  }

  @Test
  public void testRevealNeighborsOfEmptyFields() {
    DefaultTable dt = new DefaultTable(3, 3, 0);
    dt.checkNeighbors();
    Field start = dt.getFieldByPosition(new Position(1, 1));
    Set<Position> visited = new HashSet<>();
    dt.RevealNeighborsOfEmptyFields(start, visited);
    for (Field f : dt.fields) {
      assertTrue(f.getRevealed());
    }
  }

}
