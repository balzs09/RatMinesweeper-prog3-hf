package com.example;

import java.io.Serializable;

/**
 * @brief Egy játékos egyetlen eredményét tárolja a ranglistában.
 *
 * Tartalmazza:
 * - a játékos nevét,
 * - az elért időt másodpercben.
 */
public class HighscoreEntry implements Serializable {
  private int time;
  private String name;
  public HighscoreEntry(int time, String name){
    this.time=time;
    this.name=name;
  }
  public int getTime(){
    return time;
  }
  public String getName(){
    return name;
  }
}
