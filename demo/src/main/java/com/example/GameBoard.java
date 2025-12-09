package com.example;

import javax.swing.JPanel;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * @brief A GameBoard osztály felelős a játéktábla vizuális megjelenítéséért.
 * 
 * Feladata a cellák, szomszédos bombák száma, zászlók, bombák,
 * egér és sajt kirajzolása a panelen.
 * Kezeli a repaint-et a változások szerint, és a felhasználó kattintásaira 
 * reagál, a GameController logikáját használva.
 * 
 * Kapcsolódó osztályok:
 * - GameMouseAdapter: Az egérkattintásra való reagálás
 * - GameController: a játék logikájának kezelése
 * - Field: a játéktábla mezői
 * - Position: a mezők pozícióinak kezelése
 * - RatTable, Rat: RAT játékmódhoz tartozó egér és célpozíció
 * - GameModes, Difficulties: játékmód és nehézségi szint információk
 */
public class GameBoard extends JPanel {
  private GameController controller;
  private boolean initialized = false;

  /**
   * Gameboard osztály konstruktora
   * Hozzáadja az egérkattintások kezelését.
   * 
   */
  public GameBoard() {
    addMouseListener(new GameMouseAdapter(this));

  }

  /**
   * Beállítja a játéktábla panelhez tartozó játékvezérlőt.
   * 
   * @param controller játékvezérlő
   */
  public void setGameController(GameController controller) {
    this.controller = controller;
  }

  /**
   * Visszatér a cellák szélességével.
   * 
   * @return cellák szélessége
   */
  private int getCellWidth() {
    return getWidth() / controller.getTable().getColumns();
  }

  /**
   * Visszatér a cellák magasságával.
   * 
   * @return cellák magassága
   */
  private int getCellHeight() {
    return getHeight() / controller.getTable().getRows();
  }

  /**
   * Visszatér a tábla oszlopának a panelen belüli X koordinátájával.
   * 
   * @param column tábla oszlopa
   * @return az X-koordináta panelen belül
   */
  private int getBoardPositionX(int column) {
    return column * getCellWidth();
  }

  /**
   * Visszatér a tábla sorának a panelen belüli Y koordinátájával.
   * 
   * @param row tábla sora
   * @return az Y-koordináta panelen belül
   */
  private int getBoardPositionY(int row) {
    return row * getCellHeight();
  }

  /**
   * Ez a metódus visszatér azzal a pozícióval, ami a panelen belüli
   * koordinátákhoz tartozik.
   * 
   * @param x az X-koordináta panelen belül
   * @param y az Y-koordináta panelen belül
   * @return a koordinátákhoz tartozó pozíció
   */
  public Position getCellFromCoordinates(int x, int y) {
    int row = y / getCellHeight();
    int column = x / getCellWidth();
    return new Position(row, column);
  }
  
  /**
   * Egy pozícióhoz tartozó cellát újrafest.
   * 
   * @param pos mező pozíciója a táblán
   */
  public void rePaintCell(Position pos) {
    int boardPositionX = getBoardPositionX(pos.getColumn());
    int boardPositionY = getBoardPositionY(pos.getRow());
    repaint(boardPositionX, boardPositionY, getCellWidth(), getCellHeight());
  }
  
  /**
   * Ez a metódus berajzolja a panelen a cellák széleit fekete színúre.
   * 
   * @param g a grafikus objektum, amire rajzolunk
   */
  public void drawBorders(Graphics g) {
    int rows = controller.getTable().getRows();
    int columns = controller.getTable().getColumns();
    g.setColor(Color.BLACK);
    for (int i = 0; i < rows; i++)
      g.drawLine(0, i * getCellHeight(), columns * getCellWidth(), i * getCellHeight());
    for (int j = 0; j < columns; j++)
      g.drawLine(j * getCellWidth(), 0, j * getCellWidth(), rows * getCellHeight());
  }
  
  /**
   * Ez a metódus berajzol minden mezőt a panelre a kezdeti
   * állapot alapján.
   * 
   * @param g a grafikus objektum, amire rajzolunk
   */
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
  
  /**
   * Ez a metódus felel a komponensek kirajzolásáért.
   * Elsőnek meghívja a JPanel paintComponent metódusát.
   * Ezután ha panel nem volt még inicializálva minden mezőt berajzol.
   * Ha a panel már inicializálva van csak azokat cellákat rajzolja meg.
   * amelyek a repaint által érintett területbe beleesnek.
   * 
   * @param g a grafikus objektum, amire rajzolunk
   */
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
  
  /**
   * A szomszédos bombák száma alapján visszatér egy színnel.
   * Összesen 8 szín van, így a számot 8-cal kell maradékosan osztani, ahhoz
   * hogy megkapjuk a hozzá tartozó színt.
   * 
   * @param neighborNumber szomszédos bombák száma
   * @return mező színe
   */
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
  
  /**
   * Ez a metódus felrajzolja a mező szomszédos bombáinak számát a panelre. 
   * 
   * @param g a grafikus objektum, amire rajzolunk
   * @param number a szomszédos bombák száma
   * @param boardPositionX a cella X-koordinátája a panelen
   * @param boardPositionY a cella Y-koordinátája a panelen
   */
  public void drawNumber(Graphics g, int number, int boardPositionX, int boardPositionY) {
    String numberText = String.valueOf(number);
    int numberTextWidth = g.getFontMetrics().stringWidth(numberText);
    int numberTextHeight = g.getFontMetrics().getHeight();
    int numberTextX = boardPositionX + (getCellWidth() - numberTextWidth) / 2;
    int numberTextY = boardPositionY + (getCellHeight() - numberTextHeight) / 2 + g.getFontMetrics().getAscent();
    g.drawString(numberText, numberTextX, numberTextY);

  }

  /**
   * Ez a metódus abban az esetben, ha a szomszédos bombák száma legalább 1,
   * kirajzolja a panelre a számot.
   * A szám színe a getNumberColor metódus által visszaadott lesz.
   * 
   * @param g a grafikus objektum, amire rajzolunk
   * @param neighborNumber a szomszédos bombák száma
   * @param boardPositionX a cella X-koordinátája a panelen
   * @param boardPositionY a cella Y-koordinátája a panelen
   */
  public void drawNumberCell(Graphics g, int neighborNumber, int boardPositionX, int boardPositionY) {
    if (neighborNumber > 0) {
      g.setColor(getNumberColor(neighborNumber));
      g.setFont(g.getFont().deriveFont(18f));
      drawNumber(g, neighborNumber, boardPositionX, boardPositionY);
    }
  }

  /**
   * Ez a metódus kirajzolja mezőkhöz tartozó zászlókat.
   * Ha több zászló is van egy mezőben, a zászlók színe különbözik.
   * A zászlóhoz tartozik egy fekete tartórúd is.
   * A cella háttérszíne a paraméterként megkapott szín lesz.
   * 
   * @param g a grafikus objektum, amire rajzolunk
   * @param flagNumber a zászlók száma
   * @param boardPositionX a cella X-koordinátája a panelen
   * @param boardPositionY a cella Y-koordinátája a panelen
   * @param background a cella hátterének színe
   */
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
  
  /**
   * Ez a metódus kirajzol egy bomba mezőt.
   * Erre akkor van szükség, ha egy bomba helytelenül
   * biztonságos mezőnek lett bejelölve.
   * Minden esetben egy bomba van a cellába berajzolva.
   * A bombák belsejében szerepel, hogy a cella hány bombát tartalmaz.
   * A cella háttérszíne a paraméterként megkapott szín lesz.
   * 
   * @param g a grafikus objektum, amire rajzolunk
   * @param boardPositionX a cella X-koordinátája a panelen
   * @param boardPositionY a cella Y-koordinátája a panelen
   * @param background a cella hátterének színe
   * @param mineNumber a cellában szereplő bombák száma
   */
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
  
  /**
   * Ez a metódus sajtot rajzol arra a pozícióra,
   * ami a RAT objektumnak a célpozíciója.
   * A sajt kirajzolásához 2D-s grafika van alkalmazva.
   * 
   * @param g a grafikus objektum, amire rajzolunk
   * @param x a cella X-koordinátája a panelen
   * @param y a cella Y-koordinátája a panelen
   */
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
  
  /**
   * Ez a metódus az egeret rajzolja ki, a RAT objektum
   * jelenlegi pozíciójára
   * Az egér feje, fülei, szemei és orra van a panelre felrajzolva.
   * @param g a grafikus objektum, amire rajzolunk
   * @param x a cella X-koordinátája a panelen
   * @param y a cella Y-koordinátája a panelen
   */
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
  
  /**
   * Ez a metódus választja ki, hogy mi igaz a megadott mezőre
   * és ez alapján, mit kell a panelre felrajzolni.
   * Ha a játékos vesztett, a biztonságos mezőnek nézett bomba
   * és a helytelen számú zászlóval bejelölt mező piros színű hátteret kapnak.
   * 
   * @param row a kijelölt mező sora
   * @param column a kijelölt mező oszlopa
   * @param g a grafikus objeltum amire rajzolunk
   * @param field a kijelölt mező
   */
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
      drawBomb(g, boardPositionX, boardPositionY, Color.RED, field.getMineNumber());
    if (!field.getRevealed() && !field.getFlagged() && controller.getBombActivated() && field.getIsMine())
      drawBomb(g, boardPositionX, boardPositionY, Color.LIGHT_GRAY, field.getMineNumber());
    if (controller.getBombActivated() && field.getFlagged() && field.getFlags() != field.getMineNumber())
      drawFlags(g, field.getFlags(), boardPositionX, boardPositionY, Color.RED);

  }
  /**
   * Visszatéríti a játékpanelhez tartozó játékvezérlőt.
   * 
   * @return játékpanelhez tartozó játékvezérlő
   */
  public GameController getController() {
    return controller;
  }

}
