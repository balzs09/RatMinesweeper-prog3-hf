package com.example;

public class Rat {
    private Position currentPosition;
    private Position goalPosition;
    public Rat(Position currentPosition){
        this.currentPosition=currentPosition;
        goalPosition=currentPosition;
    }
    public void  setGoal(Position goalPosition){
      this.goalPosition=goalPosition;
    }
    public void setCurrentPosition(Position currentPosition){
        this.currentPosition=currentPosition;
    }
    public Position getCurrentPosition(){
        return currentPosition;
    }
    public Position getGoalPosition(){
        return goalPosition;
    }


}
