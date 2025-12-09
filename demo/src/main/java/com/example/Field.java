package com.example;

/**
 * @brief A Field osztály a játék egy mezőjét reprezentálja.
 *
 * Egy mező tárolja, hogy tartalmaz-e bombát, hány szomszédja tartalmaz bombát,
 * hány zászlóval van megjelölve, illetve hogy fel van-e fedve.
 * A mező kezeli a zászlók számának növelését, visszaállítását,
 * valamint a bomba- és szomszédinformációkat.
 *
 * A Field objektumok a játéktábla elemi egységei, amelyeken keresztül
 * a játékos jelölhet, felfedhet és információt kaphat a környező mezőkről.
 */
public class Field {
  private boolean isMineField = false;
  private int numberOfNeighbors = 0;
  private int mineNumber = 0;
  private boolean flagged = false;
  private boolean revealed = false;
  private int flags = 0;

  /**
   * A Field osztály konstruktora.
   * 
   * @param isMine megadja, hogy a mező bomba-e, vagy sem.
   */
  public Field(boolean isMine) {
    isMineField = isMine;
  }

  /**
   * Ez a metódus növeli a mezőhöz tartozó zászlók számát.
   * Abban az esetben ha eléri a zászlók maximum lehetséges számát,
   * visszatéríti a mezőt az eredeti állapotba.
   * 
   * @param mode A játékmód amelyben történik a zászló növelés, ez azért
   *             szükséges mert függ a játékmódtól a zászlók maximum száma.
   */
  public void incrementFlags(GameModes mode) {
    flagged = true;
    flags++;
    if (flags > maxFlag(mode))
      resetFlags();
  }

  /**
   * A függvény a mezőket visszatéríti eredeti állapotába, a zászlók száma 0 lesz,
   * és a flagged változó hamis lesz.
   */
  public void resetFlags() {
    flagged = false;
    flags = 0;
  }

  /**
   * A függvény a játékmódhoz tartozó maximum zászló számmal tér vissza.
   * 
   * @param mode A játékmód
   * @return 1, ha a mode default; 3, ha a mód rat.
   */
  public int maxFlag(GameModes mode) {
    if (mode == GameModes.DEFAULT)
      return 1;
    else
      return 3;
  }

  /**
   * A függvény beállítja a mezőhöz tartozó bombák számát
   * Az isMineField változó értéke igaz lesz.
   * 
   * @param number- Bombák száma.
   */
  public void setMineNumber(int number) {
    isMineField = true;
    mineNumber = number;
  }

  /**
   * A függvény a bombák számát téríti vissza.
   * 
   * @return bombák száma
   */
  public int getMineNumber() {
    return mineNumber;
  }

  /**
   * Beállítja a mező numberofNeighbors változóját.
   * 
   * @param number A mezővel szomszédos, bombát tartalmazó mezők száma.
   */
  public void setNumberOfNeighbors(int number) {
    numberOfNeighbors = number;
  }

  /**
   * Visszatér a mezővel szomszédos, bombát tartalmazó mezők számával.
   * 
   * @return bombát tartlmazó szomszédok száma
   */
  public int getNumberOfNeighbors() {
    return numberOfNeighbors;
  }

  /**
   * Visszatér azzal a logikai értékkel, hogy a mező tartalmaz-e bombát vagy se.
   * 
   * @return bomba-e
   */
  public boolean getIsMine() {
    return isMineField;
  }

  /**
   * Visszatér a mezőhöz tartozó zászlók számával.
   * 
   * @return zászlók száma.
   */
  public int getFlags() {
    return flags;
  }

  /**
   * Visszatér azzal a logikai értékkel, hogy a mező be van e jelölve zászlóként.
   * 
   * @return bejelölt-e
   */
  public boolean getFlagged() {
    return flagged;
  }

  /**
   * Visszatér azzal a logikai értékkel, hogy a mező fel van e fedve már.
   * 
   * @return felfedve-e
   */
  public boolean getRevealed() {
    return revealed;
  }

  /**
   * A függvény a mező felfedettségét állítja be.
   * 
   * @param revealed- A mező fel van-e fedve.
   */
  public void setRevealed(boolean revealed) {
    this.revealed = revealed;
  }

  /**
   * A függvény a mező zászlóként beállítottságát változatja meg.
   * 
   * @param flagged A mező zászlóként van-e bejelölve.
   */
  public void setFlagged(boolean flagged) {
    this.flagged = flagged;
  }

}
