package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class RatTest {
   @Test
    public void testRatConstructorInitialValues() {
        Position currentPos = new Position(0, 0);
        Rat rat= new Rat(currentPos);
        assertEquals(rat.getCurrentPosition(), currentPos);
        assertEquals(rat.getGoalPosition(), currentPos);
    }
    @Test
    public void testSetCurrentAndGoal(){
        Position currentPos = new Position(1, 1); 
        Position goalPos = new Position(2, 3);
        Rat rat= new Rat(new Position(0, 0));
        rat.setCurrentPosition(currentPos);
        rat.setGoal(goalPos);
        assertEquals(rat.getCurrentPosition(), currentPos);
        assertEquals(rat.getGoalPosition(), goalPos);

    }
}
