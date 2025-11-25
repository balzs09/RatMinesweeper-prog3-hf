package com.example;

public class Field {
  private boolean isMineField = false;
  private int numberOfNeighbors = 0;
  private int mineNumber = 0;
  private boolean flagged=false;
  private boolean revealed=false;
  private int flags=0;

   public Field(boolean isMine) {
    isMineField = isMine;
  }
  public void incrementFlags(GameModes mode){
    flagged=true;
    flags++;
    if(flags>maxFlag(mode))
      resetFlags();;
  }
  public void resetFlags(){
    flagged=false;
    flags=0;
  }
  public int maxFlag(GameModes mode){
    if(mode==GameModes.DEFAULT) return 1;
    else   return 3;
    
  }

  public void setMineNumber(int number) {
    isMineField=true;
    mineNumber = number;
  }

  public int getMineNumber() {
    return mineNumber;
  }

  public void setNumberOfNeighbors(int number) {
    numberOfNeighbors = number;
  }

  public int getNumberOfNeighbors() {
    return numberOfNeighbors;
  }

  public boolean getIsMine() {
    return isMineField;
  }
  public int getFlags(){
    return flags;
  }
  public boolean getFlagged(){
    return flagged;
  }
  public boolean getRevealed(){
    return revealed;
  }
  public void setRevealed(boolean revealed){
    this.revealed=revealed;
  }
  public void setFlagged(boolean flagged){
    this.flagged=flagged;
  }

}
