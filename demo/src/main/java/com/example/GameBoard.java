package com.example;

import javax.swing.JPanel;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class GameBoard extends JPanel {
  private GameController controller;
  private boolean initialized = false;

  public GameBoard() {
    addMouseListener(new GameMouseAdapter(this));

  }

  public void setGameController(GameController controller) {
    this.controller = controller;
  }

  private int getCellWidth() {
    return getWidth() / controller.getTable().getColumns();
  }

  private int getCellHeight() {
    return getHeight() / controller.getTable().getRows();
  }

  private int getBoardPositionX(int column) {
    return column * getCellWidth();
  }

  private int getBoardPositionY(int row) {
    return row * getCellHeight();
  }

  public Position getCellFromCoordinates(int x, int y) {
    int row = y / getCellHeight();
    int column = x / getCellWidth();
    return new Position(row, column);
  }

  public void rePaintCell(Position pos) {

    int boardPositionX = getBoardPositionX(pos.getColumn());
    int boardPositionY = getBoardPositionY(pos.getRow());
    repaint(boardPositionX, boardPositionY, getCellWidth(), getCellHeight());
  }

  public void drawBorders(Graphics g) {
    int rows = controller.getTable().getRows();
    int columns = controller.getTable().getColumns();
    g.setColor(Color.BLACK);
    for (int i = 0; i < rows; i++)
      g.drawLine(0, i * getCellHeight(), columns * getCellWidth(), i * getCellHeight());
    for (int j = 0; j < columns; j++)
      g.drawLine(j * getCellWidth(), 0, j * getCellWidth(), rows * getCellHeight());
  }

  public void initializeBoard(Graphics g) {
    for (int i = 0; i < controller.getTable().getRows(); i++) {
      for (int j = 0; j < controller.getTable().getColumns(); j++) {
        Position paintedPos = new Position(i, j);
        Field paintedField = controller.getTable().getFieldByPosition(paintedPos);
        drawCell(i, j, g, paintedField);
      }
    }
    initialized = true;
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (controller == null)
      return;
    if (initialized == false)
      initializeBoard(g);
    else {
      Rectangle clip = g.getClipBounds();
      int firstRow = Math.max(clip.y / getCellHeight(), 0);
      int lastRow = Math.min((clip.y + clip.height) / getCellHeight(), controller.getTable().getRows() - 1);
      int firstColumn = Math.max(clip.x / getCellWidth(), 0);
      int lastColumn = Math.min((clip.x + clip.width) / getCellWidth(), controller.getTable().getColumns() - 1);
      for (int row = firstRow; row <= lastRow; row++) {
        for (int column = firstColumn; column <= lastColumn; column++) {
          Field paintedField = controller.getTable().getFieldByPosition(new Position(row, column));
          drawCell(row, column, g, paintedField);
        }
      }
    }
    drawBorders(g);
  }

  // Gives the color of the number given as parameter
  // There are 9 colors, if the neigborNumber is bigger than 8, it will have thes
  // same color as the rest, when divided by 8.
  private Color getNumberColor(int neighborNumber) {
    switch (neighborNumber % 8) {
      case 1:
        return Color.BLUE;
      case 2:
        return Color.GREEN;
      case 3:
        return Color.RED;
      case 4:
        return Color.MAGENTA;
      case 5:
        return Color.ORANGE;
      case 6:
        return Color.CYAN;
      case 7:
        return Color.PINK;
      default:
        return Color.BLACK;
    }
  }

  public void drawNumber(Graphics g, int number, int boardPositionX, int boardPositionY) {
    String numberText = String.valueOf(number);
    int numberTextWidth = g.getFontMetrics().stringWidth(numberText);
    int numberTextHeight = g.getFontMetrics().getHeight();
    int numberTextX = boardPositionX + (getCellWidth() - numberTextWidth) / 2;
    int numberTextY = boardPositionY + (getCellHeight() - numberTextHeight) / 2 + g.getFontMetrics().getAscent();
    g.drawString(numberText, numberTextX, numberTextY);

  }

  // Draws the cell for a number field.
  public void drawNumberCell(Graphics g, int neighborNumber, int boardPositionX, int boardPositionY) {
    if (neighborNumber > 0) {
      g.setColor(getNumberColor(neighborNumber));
      g.setFont(g.getFont().deriveFont(18f));
      drawNumber(g, neighborNumber, boardPositionX, boardPositionY);
    }
  }

  // Draws the flags in the cell
  // A zászlók oválisok által vannak felrajzolva, a könnyebb láthatóság érdekében.
  public void drawFlags(Graphics g, int flagNumber, int boardPositionX, int boardPositionY, Color background) {
    g.setColor(background);
    g.fillRect(boardPositionX, boardPositionY, getCellWidth(), getCellHeight());
    int width = getCellWidth();
    int height = getCellHeight();
    int flagWidth = width / 4;
    int flagHeight = height / 3;
    for (int i = 0; i < flagNumber; i++) {
      int offsetX = boardPositionX + 10 + i * (flagWidth + 4);
      g.setColor(Color.BLACK);
      int poleX = offsetX;
      int poleYTop = boardPositionY + height / 3 - flagHeight / 2;
      int poleYBottom = poleYTop + flagHeight * 2;
      int poleWidth = width / 30;
      g.fillRect(poleX, poleYTop, poleWidth, poleYBottom - poleYTop);
      if (i == 0)
        g.setColor(new Color(255, 90, 90)); // lighter red
      if (i == 1)
        g.setColor(Color.BLUE);
      if (i == 2)
        g.setColor(Color.GREEN);
      int[] xs = { poleX + poleWidth, poleX + poleWidth + flagWidth, poleX + poleWidth };
      int[] ys = { poleYTop, poleYTop + flagHeight / 2, poleYTop + flagHeight };
      g.fillPolygon(xs, ys, 3);
    }
  }

  public void drawBomb(Graphics g, int boardPositionX, int boardPositionY, Color background, int mineNumber) {
    int width = getCellWidth();
    int height = getCellHeight();
    g.setColor(background);
    g.fillRect(boardPositionX, boardPositionY, width, height);
    g.setColor(Color.BLACK);
    int size = Math.min(width, height) / 2;
    int bombX = boardPositionX + (width - size) / 2;
    int bombY = boardPositionY + (height - size) / 2;
    g.fillOval(bombX, bombY, size, size);
    if (mineNumber == 1)
      g.setColor(Color.WHITE);
    if (mineNumber == 2)
      g.setColor(Color.YELLOW);
    if (mineNumber == 3)
      g.setColor(Color.PINK);
    drawNumber(g, mineNumber, boardPositionX, boardPositionY);
  }

  public void drawCheese(Graphics g, int x, int y) {
    int width = getCellWidth();
    int height = getCellHeight();
    Graphics2D g2 = (Graphics2D) g;
    g2.setColor(new Color(255, 230, 0));
    int[] xs = { x + width / 4, x + width - width / 4, x + width / 4 };
    int[] ys = { y + height / 4, y + height / 2, y + height - height / 4 };
    g2.fillPolygon(xs, ys, 3);

    g2.setColor(new Color(230, 180, 0));
    g2.fillOval(x + width / 2, y + height / 2, width / 9, height / 9);
    g2.fillOval(x + width / 3, y + height / 3, width / 8, height / 8);
  }

  public void drawMouse(Graphics g, int x, int y) {
    Graphics2D g2 = (Graphics2D) g;
    int width = getCellWidth();
    int height = getCellHeight();
    g2.setColor(Color.GRAY);
    g2.fillOval(x + width / 4, y + height / 4, width / 2, height / 2);
    int headW = width / 3;
    int headH = height / 3;
    int headX = x + width / 2 - headW / 2;
    int headY = y + height / 4 - headH / 4;
    g2.fillOval(headX, headY, headW, headH);
    g2.setColor(Color.LIGHT_GRAY);
    g2.fillOval(x + width / 2 - width / 4, y + height / 4 - height / 6, width / 6, height / 6);
    g2.fillOval(x + width / 2 + width / 12, y + height / 4 - height / 6, width / 6, height / 6);
    g2.setColor(Color.BLACK);
    int eyeSize = width / 15;
    int eyeY = headY + headH / 3;
    int leftEyeX = headX + headW / 4;
    int rightEyeX = headX + headW * 3 / 4 - eyeSize;
    g2.fillOval(leftEyeX, eyeY, eyeSize, eyeSize);
    g2.fillOval(rightEyeX, eyeY, eyeSize, eyeSize);
    g2.setColor(Color.RED);
    int noseSize = width / 12;
    int noseX = headX + headW / 2 - noseSize / 2;
    int noseY = headY + headH * 2 / 3;
    g2.fillOval(noseX, noseY, noseSize, noseSize);
  }

  public void drawCell(int row, int column, Graphics g, Field field) {

    int boardPositionX = getBoardPositionX(column);
    int boardPositionY = getBoardPositionY(row);
    if (!field.getRevealed() && !field.getFlagged()) {
      g.setColor(Color.LIGHT_GRAY);
      g.fillRect(boardPositionX, boardPositionY, getCellWidth(), getCellHeight());
    }
    if (field.getFlagged())
      drawFlags(g, field.getFlags(), boardPositionX, boardPositionY, Color.LIGHT_GRAY);
    if (controller.getGameMode() == GameModes.RAT && controller.getTableGenerated()) {
      RatTable ratTable = (RatTable) controller.getTable();
      if (ratTable.getRat().getCurrentPosition().equals(ratTable.getPositionByField(field)))
        drawMouse(g, boardPositionX, boardPositionY);
      if (ratTable.getRat().getGoalPosition().equals(ratTable.getPositionByField(field)))
        drawCheese(g, boardPositionX, boardPositionY);
    }
    if (field.getRevealed() && !field.getIsMine())
      drawNumberCell(g, field.getNumberOfNeighbors(), boardPositionX, boardPositionY);
    if (field.getRevealed() && field.getIsMine())
      drawBomb(g, boardPositionX, boardPositionY, Color.RED,field.getMineNumber());
    if (!field.getRevealed() && !field.getFlagged() && controller.getBombActivated() && field.getIsMine())
      drawBomb(g, boardPositionX, boardPositionY, Color.LIGHT_GRAY,field.getMineNumber());
    if (controller.getBombActivated() && field.getFlagged() && field.getFlags() != field.getMineNumber())
      drawFlags(g, field.getFlags(), boardPositionX, boardPositionY, Color.RED);

  }

  public GameController getController() {
    return controller;
  }

}
