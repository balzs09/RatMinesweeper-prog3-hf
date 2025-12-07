package com.example;

public class Position {
  private final int  row;
  private final int column;

  /**
   * A pozíció osztály konstruktora.
   * A pozíció sora és oszlopa kerül általa beállításra.
   * @param row a poz@ció sora
   * @param column a pozíció oszlopa
   */
  public Position(int row, int column ){
    this.row=row;
    this.column=column;
  }
  /**
   * 
   * @return a pozíció sorát téríti vissza
   */
  public int getRow(){
    return row;
  }
  /**
   * 
   * @return a pozíció oszlopát téríti vissza 
   */
  public int getColumn(){
    return column;
  }
  /**
   * Felülírja az equals metódust.
   * 
   * @param obj a hasonlított objektum
   * @return true, ha midnkét objektum a Position osztály egy példánya és a sorok és oszlopok megegyeznek;false más esetben
   */
  @Override
  public boolean equals(Object obj){
    if(this==obj ) return true;
    if(obj==null||getClass()!=obj.getClass()) return false;
    Position other= (Position) obj;
    return row==other.row && column==other.column;

  }
  /**
   * Felülírja a hashCode metódust.
   * 
   * @return a pozícióhoz tartozü hash érték
   */
  @Override
  public int hashCode(){
    return 31*row+column;
  }
}
