package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class RatTableTest {
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

    @Test
    public void testSetRat() {
        RatTable rt = new RatTable(5, 9, 8);
        Position pos = new Position(2, 4);
        rt.setRat(pos);
        assertEquals(pos, rt.getRat().getCurrentPosition());
        assertEquals(pos, rt.getRat().getGoalPosition());
    }

    @Test
    public void testGetNeighborPositions() {
        RatTable rt = new RatTable(5, 5, 0);
        assertEquals(8, rt.getNeighborPositions(new Position(0, 0)).size());
        assertEquals(8, rt.getNeighborPositions(new Position(2, 0)).size());
        assertEquals(8, rt.getNeighborPositions(new Position(1, 1)).size());
    }

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

   @Test
   public void testSelectingMines() {
    RatTable rt = new RatTable(9, 9, 8);
    Position selected = new Position(2, 2);
    ArrayList<Field> available = rt.getavailableFields(selected);
    rt.selectingMines(available);
    int mineNumber = 0;
    int mineFieldNumber=0;
    for (Field field : available)
      if (field.getIsMine()){
        mineFieldNumber++;
        mineNumber+=field.getMineNumber();
      }
        
    assertEquals(1, available.get(0).getMineNumber());
    assertEquals(2, available.get(5).getMineNumber());
    assertEquals(3, available.get(6).getMineNumber());
    assertEquals(8, mineFieldNumber);
    assertEquals(14, mineNumber);
  }

}
