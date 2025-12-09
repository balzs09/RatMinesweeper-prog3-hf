package com.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.io.File;

/**
 * @brief A GameController osztály kezeli a játék logikáját és a játéktábla
 *        állapotát.
 * 
 *        Az osztály felelős a játék indításáért, a tábla létrehozásáért, az
 *        egér
 *        mozgásáért (RAT mód), a mezők felfedéséért és a bombák kezeléséért.
 *        Kezeli a győzelem és veszteség logikáját, a bejelölt és be nem jelölt
 *        bombákat,
 *        valamint frissíti a játékablak felületét a változásoknak megfelelően.
 * 
 *        Fő funkciók:
 *        - Játéktábla létrehozása a nehézségi szintnek megfelelő paraméterekkel
 *        - Üres mezők automatikus felfedése
 *        - Egér lépése RAT módban
 *        - Bejelölt bombák és helyes/hibás zászlók kezelése
 *        - Győzelem és veszteség logika, ranglista frissítés
 *        - Interakciók a felhasználóval (bal/jobb egérgomb)
 *
 *        Kapcsolódó osztályok és típusok:
 *        - GameBoard, GameWindow: a felület és a vizuális megjelenítés kezelése
 *        - Table, DefaultTable, RatTable: a játék logikai táblája
 *        - Field: egy mező a táblán
 *        - Rat, Position: egér pozíciójának kezelése RAT módban
 *        - GameModes, Difficulties: a játékmód és nehézségi szint beállítása
 */
public class GameController {
  private Difficulties difficulty;
  private GameModes gameMode;
  private Table gameTable;
  private GameBoard board;
  private GameWindow window;
  private boolean bombActivated;
  private boolean tableGenerated;
  private boolean winner;
  private int remainingMines;
  // Hány bomba maradt bejelöletlenül, ha a bejelölés helyes volt:
  private int unflaggedOneMines;
  private int unflaggedTwoMines;
  private int unflaggedThreeMines;

  /**
   * A GameController osztály konstruktora, beállítja a megfelelő tagváltozókat
   * a paraméterként kapott értékekre.
   * 
   * @param mode   a játékmód
   * @param dif    a játék nehézségi szintje
   * @param board  a játékhoz tartozó játéktábla panel
   * @param window az ablak amelyen megjelenik a játék
   */
  public GameController(GameModes mode, Difficulties dif, GameBoard board, GameWindow window) {
    gameMode = mode;
    difficulty = dif;
    this.board = board;
    this.window = window;
  }

  /**
   * Ez a metódus visszatérít egy 3 elemű tömböt.
   * Ezekben szerepel a tábla sorainak és oszlopainak száma valamint a bombák
   * száma.
   * Ezek az értékek a játék nehézségétől függnek
   * 
   * @return sorok, oszlopok és bombák száma, 3 elemű tömbként
   */
  private int[] getTableParametersForDifficulty() {
    if (difficulty == Difficulties.EASY)
      return new int[] { 10, 10, 12 };
    else if (difficulty == Difficulties.MEDIUM)
      return new int[] { 16, 16, 40 };
    else
      return new int[] { 20, 20, 100 };
  }

  /**
   * Ez a metódus elindit egy új játékot .
   * A játéktábla megkapja a játék nehézségéhez tartozó sor, oszlop és
   * bombaszámot.
   * Minden játék elején az aktivált bomba, nyertes és legenerált tábla logikai
   * változók hamis értékűek lesznek.
   * Default játékmód esetén a játék kezdetén a bejelöletlen bomba mezők száma
   * egyenlő lesz az összes bomba számával.
   * Rat játékmód esetén külön jelenik meg a bejelöletlen mezők amelyek egy, kettő
   * és három bombát tartalmaznak.
   * Mindegyik azt kapja meg értékként, hogy eredetileg külön-külön mennyi van
   * belőlük a táblában.
   * 
   */
  public void setGameTable() {
    bombActivated = false;
    winner = false;
    tableGenerated = false;
    int parameters[] = getTableParametersForDifficulty();
    int rows = parameters[0];
    int columns = parameters[1];
    int mineNumber = parameters[2];
    remainingMines = mineNumber;
    if (gameMode == GameModes.DEFAULT) {
      gameTable = new DefaultTable(rows, columns, mineNumber);
      unflaggedOneMines = gameTable.getAllMines();
    } else {
      gameTable = new RatTable(rows, columns, mineNumber);
      unflaggedOneMines = gameTable.getOneMineFields();
      unflaggedTwoMines = ((RatTable) gameTable).getTwoMineFields();
      unflaggedThreeMines = ((RatTable) gameTable).getThreeMineFields();
    }
  }

  /**
   * Ez a metódus felfedi az üres mezővel szomszédos mezőket.
   * Abban az esetben, ha a szomszédos mező már be van jelölve zászlóként,
   * a mező nem lesz felfedve.
   * Ha a szomszédok közül is van egy mező amely üres,
   * annak szomszédait is felfedi.
   * A visited set-ben tárolja a megtalált üres mezőket.
   * Ez azért szükséges, hogy ugyanarra a mezőre ne legyen többször elvégezve a
   * metódus, végtelen ciklust generálva.
   * 
   * @param f       kiválasztott üres mező
   * @param visited azon üres mezők, amelyekre a függvény már meg volt hívva.
   */
  public void revealNeighborsOfEmptyFields(Field f, Set<Field> visited) {
    if (visited.contains(f))
      return;
    visited.add(f);
    List<Position> neighborPositions = gameTable.getNeighbors(gameTable.getPositionByField(f));
    for (Position neighbor : neighborPositions) {
      Field neighborField = gameTable.getFieldByPosition(neighbor);
      if (!neighborField.getFlagged()) {
        neighborField.setRevealed(true);
        board.rePaintCell(neighbor);
      }
      if (neighborField.getNumberOfNeighbors() == 0)
        revealNeighborsOfEmptyFields(neighborField, visited);
    }
  }

  /**
   * A metódus által a táblán az egér lép egyet a paraméterként megadott úton.
   * Ha olyan mezőre lép, amely nem tartalmaz bombát és nem volt még felfedve, se
   * bejelölve azt felfedi.
   * Ha üres mezőre lép az üres mezőnek szomszédait is felfedi
   * Ha olyan mezőre lép amely tartalmaz bombát, de nem volt még bejelölve, a
   * mezőn található bombák számával bejelöli.
   * A bejelöletlen mezők száma is csökken ha ilyen mezőre lép az egér.
   * A pozíció, amelyre lépett az egér ki lesz törölve az út listából.
   * 
   * @param path az út pozícióinak listája, amelyen kell lépkedjen az egér
   */
  public void moveByOne(List<Position> path) {

    if (path.size() > 1) {
      Rat rat = ((RatTable) gameTable).getRat();
      rat.setCurrentPosition(path.get(1));
      Field field = gameTable.getFieldByPosition(rat.getCurrentPosition());
      if (field.getIsMine() && !field.getFlagged()) {
        field.setFlagged(true);
        for (int i = 0; i < field.getMineNumber(); i++) {
          field.incrementFlags(GameModes.RAT);
          ratMineChoser(field);
        }
      }
      if (!field.getIsMine() && !field.getRevealed()) {
        field.setRevealed(true);
        if (field.getNumberOfNeighbors() == 0)
          revealNeighborsOfEmptyFields(field, new HashSet<>());
      }
      path.removeFirst();
    }
  }

  /**
   * Ez a metódus a mouse bal gombnyomása utáni műveleteket végzi el.
   * Ha a mező nem volt még felfedve, se bejelölve, felfedi a mező tartalmát.
   * Ha a mező nem bomba, felfedi a mezőhöz tartozó számot, ha ez nulla a
   * szomszédjait is.
   * RAT játékmód esetén az egér előre megy egyet a célhoz vezető úton.
   * Ha a kiválasztott mező bomba, feljegyzi, hogy a bomba már aktiválva volt.
   * A mezőn történt változások a játéktábla paneljára is felkerülnek.
   * 
   * @param selectedField a kiválasztott mező
   */
  public void numberChoser(Field selectedField) {
    if (selectedField.getFlagged())
      return;
    if (selectedField.getRevealed() == false) {
      selectedField.setRevealed(true);
      if (selectedField.getIsMine() == false) {
        if (selectedField.getNumberOfNeighbors() == 0)
          revealNeighborsOfEmptyFields(selectedField, new HashSet<>());
        if (gameMode == GameModes.RAT) {
          Position initialRatPosition = ((RatTable) gameTable).getRat().getCurrentPosition();
          board.rePaintCell(initialRatPosition);
          List<Position> path = ((RatTable) gameTable).getShortestPath();
          moveByOne(path);
          Position currentRatPosition = ((RatTable) gameTable).getRat().getCurrentPosition();
          board.rePaintCell(currentRatPosition);
        }
      } else
        bombActivated = true;
      board.rePaintCell(gameTable.getPositionByField(selectedField));
    }
  }

  /**
   * Ez a metódus csökkenti a bejelöletlen mezők számát, ha a mező bejelölt.
   * Ha ez a mező bomba is, akkor a megmaradt bombák száma is csökken.
   * Ha a mező nem bejelölt, a bejelöletlen mezők száma nő, mivel előtte bejelölt
   * volt.
   * Ha ez a mező bomba is, akkor a megmaradt bombák száma is nő.
   * A bejelöletlen bombák száma a játék ablakában is megjelenik.
   * 
   * @param selectedField a kiválasztott mező
   */
  public void defaultMineChoser(Field selectedField) {
    if (selectedField.getFlagged()) {
      unflaggedOneMines--;
      if (selectedField.getIsMine())
        remainingMines--;
    } else {
      unflaggedOneMines++;
      if (selectedField.getIsMine())
        remainingMines++;
    }
    window.updateMines(unflaggedOneMines, 1);
  }

  /**
   * Ez a metódus csökkenti a mező bejelölt zászlóinak megfelelő számlálót.
   * Az eggyel kisebb számú bombát tartalmazó mezők számát növeli.
   * Ha a mezőhöz egy bomba van bejelölve nem növel csak csökkent.
   * Ha nincs bejelölve, csak növel, nem csökkent.
   * Ha a helyes számú bomba van jelölve, a megmaradt bombamezők száma csökken.
   * Ha a helyes számú bomba túl van lépve, a megmaradt bombamezők száma
   * növekszik.
   * A bejelöletlen bombák száma a játék ablakában is megjelenik.
   * 
   * @param selectedField a kiválasztott mező
   */
  public void ratMineChoser(Field selectedField) {
    if (selectedField.getFlags() == 1)
      unflaggedOneMines--;
    else if (selectedField.getFlags() == 2) {
      unflaggedTwoMines--;
      unflaggedOneMines++;
    } else if (selectedField.getFlags() == 3) {
      unflaggedThreeMines--;
      unflaggedTwoMines++;
    } else
      unflaggedThreeMines++;
    if (selectedField.getMineNumber() == selectedField.getFlags())
      remainingMines--;
    else if (selectedField.getMineNumber() == selectedField.getFlags() - 1 ||
        selectedField.getMineNumber() == 3 && selectedField.getFlags() == 0)
      remainingMines++;
    window.updateMines(unflaggedOneMines, 1);
    window.updateMines(unflaggedTwoMines, 2);
    window.updateMines(unflaggedThreeMines, 3);
  }

  /**
   * Ez a metódus a mouse jobb gombnyomása utáni műveleteket végzi el.
   * Ha a kiválasztott mező nem volt felfedve, növeli a mezőhöz tartozó zászlók
   * számát.
   * Az incrementflags metódus értelmében ha zászlók száma nagyobb már mint a
   * maximum, a zászlók száma nulla lesz .
   * Ezután ha a játékmód DEFAULT a defaultMineChoser, ha RAT, akkor a
   * ratMineChoser metódust hívja meg a kiválasztott mezőre.
   * Ha a kiválasztott mező már fel van fedve, és a játékmód Rat, a kiválasztott
   * mező lesz az egér célpozíciója.
   * A legrövidebb útnak az egér jelenlegi és célpozíció közötti legrövidebb út
   * lesz beállítva.
   * A mezőn történt változások a játéktábla paneljára is felkerülnek.
   * 
   * @param selectedField a kiválasztott mező
   */
  public void mineChoser(Field selectedField) {
    Position currentPosition = gameTable.getPositionByField(selectedField);
    if (selectedField.getRevealed() == false) {
      selectedField.incrementFlags(gameMode);
      if (gameMode == GameModes.DEFAULT)
        defaultMineChoser(selectedField);
      else
        ratMineChoser(selectedField);
    } else if (gameMode == GameModes.RAT) {
      Position initialCheesePosition = ((RatTable) gameTable).getRat().getGoalPosition();
      board.rePaintCell(initialCheesePosition);
      ((RatTable) gameTable).getRat().setGoal(gameTable.getPositionByField(selectedField));
      ((RatTable) gameTable).setShortestPath();
    }
    board.rePaintCell(currentPosition);
  }

  /**
   * A metódus kiválasztja a táblában a bombák helyét.
   * Ezután a megszámolja, hogy hány bomba szomszédos a többi mezővel.
   * Ha a játékmód RAT, az egér kezdő és célpozícióját a kiválasztott pozícióra
   * állítja.
   * 
   * @param selectedPosition a kiválasztott pozíció
   */
  public void generateTable(Position selectedPosition) {
    tableGenerated = true;
    ArrayList<Field> availableFields = gameTable.getavailableFields(selectedPosition);
    gameTable.selectingMines(availableFields);
    gameTable.checkNeighbors();
    if (gameMode == GameModes.RAT) {
      ((RatTable) gameTable).setRat(selectedPosition);
      ((RatTable) gameTable).setShortestPath();
    }

  }

  /**
   * Ez a metódus berajzolja azokat a bombákat amelyek nem voltak kijelölve.
   * Emellett azon mezőknek kinézetét is megváltoztatja amelyek helytelenül voltak
   * bejelölve.
   */
  public void repaintMissingBombsAndWrongFlags() {
    for (int row = 0; row < gameTable.getRows(); row++) {
      for (int column = 0; column < gameTable.getColumns(); column++) {
        Position currentPosition = new Position(row, column);
        Field currentField = gameTable.getFieldByPosition(currentPosition);
        if ((!currentField.getFlagged() && currentField.getIsMine())
            || (currentField.getFlags() != currentField.getMineNumber()))
          board.rePaintCell(currentPosition);
      }
    }
  }

  /**
   * Ez a metódus ellenőrzi, hogy nem maradt olyan mező, amely nincs se felfedve,
   * se bejelölve.
   * 
   * @return true, ha nincs ki nem választott mező
   *         false, ha találtunk olyan mezőt, ami még nincs kiválasztva
   */
  public boolean isEveryFieldSelected() {
    for (int row = 0; row < gameTable.getRows(); row++) {
      for (int column = 0; column < gameTable.getColumns(); column++) {
        Field currentField = gameTable.getFieldByPosition(new Position(row, column));
        if (!currentField.getRevealed() && !currentField.getFlagged())
          return false;
      }
    }
    return true;
  }

  /**
   * A metódus leállítja az idő számlálását a játék ablakában, ha véget ért a játék.
   * Abban az esetben ha a bomba már aktiválva van, a metódus a nyerést hamisra
   * állítja.
   * A ki nem rajzolt bombákat és rosszul bejelölt mezőket újrarajzolja.
   * Emellett kiír egy üzenetet az ablakra.
   * Ha a játékos nyert, a nyerés igaz lesz.
   * A játékos idejét feljegyzi és bekerül a játékos neve a ranglistába,
   * ha benne van az idő a top 10 leggyorsabban.
   * 
   */
  public void setWinner() {
    if (bombActivated == true) {
      window.stopTimer();
      repaintMissingBombsAndWrongFlags();
      winner = false;
      window.writeMessageAfterLosing();
      
      return;
    }
    if (remainingMines == 0 && isEveryFieldSelected()) {
      window.stopTimer();
      winner = true;
      window.refreshHighscoreArea();
      File outputFile = window.getHighscoreManager().getTextFileByModeAndDifficulty(gameMode, difficulty);
      window.saveHighscoreAfterWin(window.getTimePassed(), outputFile);
    }
  }

  /**
   * Ez a metódus elvégzi a mouse által kapott gomb műveletét, ha a játék még nem
   * ért véget.
   * Bal gomb esetén, ha a tábla még nincs létrehozva előbb a táblát létrehozza.
   * Minden gombnyomásra ellenőrzi, hogy vége-e a játéknak.
   * 
   * @param click        gombnyomás típusa(jobb vagy bal)
   * @param slectedField kibálasztott mező
   */
  public void mouseChoser(MouseClicks click, Field slectedField) {
    if (bombActivated || winner)
      return;
    else {
      if (click == MouseClicks.LEFT) {
        if (!getTableGenerated())
          generateTable(gameTable.getPositionByField(slectedField));
        numberChoser(slectedField);
      } else if (click == MouseClicks.RIGHT)
        mineChoser(slectedField);
      setWinner();
    }
  }

  /**
   * Ez a metódus visszatér a győzelem logikai értékkel.
   * 
   * @return győzelem logikai értéke
   */
  public boolean getWinner() {
    return winner;
  }

  /**
   * Ez a metódus visszatér a létrehozott tábla logikai értékkel.
   * 
   * @return létrehozott tábla logikai értéke.(true, ha létre van hozva)
   */
  public boolean getTableGenerated() {
    return tableGenerated;
  }

  /**
   * Ez a metódus visszatér az aktivált bomba logikai értékkel.
   * 
   * @return aktivált bomba
   */
  public boolean getBombActivated() {
    return bombActivated;
  }

  /**
   * Ez a metódus visszatér a játékhoz tartozó táblával.
   * 
   * @return játékhoz tartozó tábla
   */
  public Table getTable() {
    return gameTable;
  }

  /**
   * Ez a metódus visszatér a be nem jelölt egyetlen bombát tartalmazó mezők
   * számával
   * 
   * @return jelöletlen egyetlen bombát tartalmazó mezők száma
   */
  public int getUnflaggedOneMines() {
    return unflaggedOneMines;
  }

  /**
   * Ez a metódus visszatér a be nem jelölt két bombát tartalmazó mezők számával
   * 
   * @return jelöletlen két bombát tartalmazó mezők száma
   */
  public int getUnflaggedTwoMines() {
    return unflaggedTwoMines;
  }

  /**
   * Ez a metódus visszatér a be nem jelölt három bombát tartalmazó mezők számával
   * 
   * @return három egyetlen bombát tartalmazó mezők száma
   */
  public int getUnflaggedThreeMines() {
    return unflaggedThreeMines;
  }

  /**
   * Ez a metódus visszatér a játékhoz tartozó játékmóddal.
   * 
   * @return játékmód
   */
  public GameModes getGameMode() {
    return gameMode;
  }
}
