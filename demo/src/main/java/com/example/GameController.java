package com.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameController {
  private Difficulties difficulty;
  private GameModes gameMode;
  private Table gameTable;
  private GameBoard board;
  private boolean activeGame = true;
  private int remainingMines; // All mines that are not flagged yet
  // How many mines are unflagged, if the flagging is correct:
  private int unflaggedOneMines;
  private int unflaggedTwoMines;
  private int unflaggedThreeMines;

  public GameController(Difficulties dif, GameModes mode, Table gamTable) {
    difficulty = dif;
    gameMode = mode;
  }

  // This function returns with the number of lines and columns for each
  // difficulty.
  private int[] getTableParametersForDifficulty() {
    if (difficulty == Difficulties.EASY)
      return new int[] { 10, 10, 12 };
    else if (difficulty == Difficulties.MEDIUM)
      return new int[] { 16, 16, 40 };
    else
      return new int[] { 20, 20, 100 };
  }

  // This function starts a game and it initalizes the size of the table by the
  // difficulty chosen.
  public void startGame() {
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

  public void numberChoser(Field selectedField) {
    if (selectedField.getRevealed() == false) {
      selectedField.setRevealed(true);
      if (selectedField.getNumberOfNeighbors() == 0)
        revealNeighborsOfEmptyFields(selectedField, new HashSet<>());
      if (gameMode == GameModes.RAT) {
        List<Position> path = ((RatTable) gameTable).getShortestPath();
        ((RatTable) gameTable).moveByOne(path);
      }
      if (selectedField.getIsMine() == true) {
        activeGame = false;
      } else {
        board.rePaintCell(gameTable.getPositionByField(selectedField));
      }
    }
  }

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
  }

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
  }

  public void mineChoser(Field selectedField) {
    if (selectedField.getRevealed() == false) {
      selectedField.incrementFlags(gameMode);
      if (gameMode == GameModes.DEFAULT)
        defaultMineChoser(selectedField);
      else
        ratMineChoser(selectedField);
    } else {
      ((RatTable) gameTable).getRat().setGoal(gameTable.getPositionByField(selectedField));
      ((RatTable) gameTable).setShortestPath();
    }
  }

  public void generateTable(Position selectedPosition) {
    startGame();
    ArrayList<Field> availableFields = gameTable.getavailableFields(selectedPosition);
    gameTable.selectingMines(availableFields);
    gameTable.checkNeighbors();
    if (gameMode == GameModes.RAT)
      ((RatTable) gameTable).setRat(selectedPosition);
    Field selectField = gameTable.getFieldByPosition(selectedPosition);
    mouseChoser("Left", selectField);

  }

  public boolean getWin(String[] args, Field[] fields) {
    generateTable(rows[0], columns[0]);
    while (activeGame == true && remainingMines > 0) {
      mouseChoser(args[i], fields[i]);

    }
    if (remainingMines == 0)
      return true;
    return false;
  }

  public void mouseChoser(MouseClicks click, Field slectedField) {
    if (click == MouseClicks.LEFT)
      numberChoser(slectedField);
    else if (click == MouseClicks.RIGHT)
      mineChoser(slectedField);

  }

  public Table getTable() {
    return getTable();
  }
}
