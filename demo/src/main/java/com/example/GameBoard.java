package com.example;

import javax.swing.JPanel;
import javax.swing.JTable;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Graphics;

public class GameBoard extends JPanel {
  private GameController controller;
  private boolean initialized;

  public GameBoard(GameController cr) {
    controller = cr;
    addMouseListener(new GameMouseAdapter(this));
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
  public void handleClicks(MouseClicks click, Position pos){
    controller.mouseChoser(click, controller.getTable().getFieldByPosition(pos));
    repaint();

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


  public void initializeBoard(Graphics g){
      drawBorders(g);
      for (int i = 0; i < controller.getTable().getRows(); i++) {
        for (int j = 0; j < controller.getTable().getColumns(); j++) {
          Position paintedPos = new Position(i, j);
          Field paintedField = controller.getTable().getFieldByPosition(paintedPos);
          drawCell(i, j, g, paintedField);
        }
      }
      initialized=true;
  }
  
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (initialized == false) initializeBoard(g);
    else{
          Rectangle clip= g.getClipBounds();
          int firstRow = Math.max(clip.y / getCellHeight(),0);
          int lastRow = Math.min((clip.y + clip.height) / getCellHeight(),controller.getTable().getRows()-1);
          int firstColumn = Math.max(clip.x / getCellWidth(),0);
          int lastColumn = Math.min((clip.x + clip.width) / getCellWidth(),controller.getTable().getColumns()-1);
          for(int row=firstRow;row<=lastRow;row++){
            for(int column=firstColumn;column<=lastColumn;column++){
              Field paintedField = controller.getTable().getFieldByPosition(new Position(row, column));
              drawCell(row, column, g, paintedField);
            }
          }
    }
  }
  //Gives the color of the number given as parameter
  //There are 9 colors, if the neigborNumber is bigger than 9, it will have thes same color as the rest, when divided by 9.
  private Color getNumberColor(int neighborNumber){
    switch(neighborNumber%9){
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
      case 8:
        return Color.YELLOW;
       default:
        return Color.BLACK;
    }
  }
  //Draws the cell for a number field. 
  public void drawNumberCell(Graphics g, int neighborNumber, int boardPositionX, int boardPositionY){
    if(neighborNumber>0){
      g.setColor(getNumberColor(neighborNumber));
      g.setFont(g.getFont().deriveFont(18f));
      String numberText= String.valueOf(neighborNumber);
      int numberTextWidth= g.getFontMetrics().stringWidth(numberText);
      int numberTextHeight= g.getFontMetrics().getHeight();
      int numberTextX=boardPositionX+ (getCellWidth()-numberTextWidth)/2;
      int numberTextY=boardPositionY+ (getCellHeight()-numberTextHeight)/2+ g.getFontMetrics().getAscent();
      g.drawString(numberText, numberTextX, numberTextY);
    }
  }
  //Draws the flags in the cell 
  //A zászlók oválisok által vannak felrajzolva, a könnyebb láthatóság érdekében.
  public void drawFlags (Graphics g, int flagNumber, int boardPositionX, int boardPositionY, Color background){
     g.setColor(background);
    g.fillRect(boardPositionX, boardPositionY, getCellWidth(), getCellHeight());
    int size=getCellWidth()/5;
    int spacing= size+2;
    for(int i=0;i<flagNumber;i++){
      if(i==0) g.setColor(Color.RED);
      if(i==1) g.setColor(Color.BLUE);
      if(i==2) g.setColor(Color.GREEN);
      g.fillOval(boardPositionX + spacing*i + 2, boardPositionY + getCellHeight()/2 - size/2, size, size);

    }
  }

  public void drawBomb( Graphics g, int boardPositionX, int boardPositionY, Color background){
    int width = getCellWidth();
    int height = getCellHeight();
    g.setColor(background);
    g.fillRect(boardPositionX, boardPositionY, width, height);
    g.setColor(Color.BLACK);
    int size=Math.min(width,height)/2;
    int bombX= boardPositionX+(width-size)/2;
    int bombY= boardPositionY+(height-size)/2;
    g.fillOval(bombX,bombY,size,size);
  }
  public void drawCell(int row, int column, Graphics g, Field field) {
    int boardPositionX = getBoardPositionX(column);
    int boardPositionY = getBoardPositionY(row);
    if(!field.getRevealed()&&!field.getFlagged()){
      g.setColor(Color.LIGHT_GRAY);
      g.fillRect(boardPositionX, boardPositionY, getCellWidth(), getCellHeight());
    }
    
    if(field.getRevealed()&&!field.getIsMine()) drawNumberCell(g,field.getNumberOfNeighbors(),boardPositionX,boardPositionY);
    if(field.getFlagged()) drawFlags(g,field.getFlags(),boardPositionX,boardPositionY,Color.LIGHT_GRAY);
    if(field.getRevealed()&&field.getIsMine()) drawBomb(g,boardPositionX,boardPositionY,Color.RED);
    if(!field.getRevealed()&&!field.getFlagged()&&!controller.getActiveGame()&&field.getIsMine()) 
      drawBomb(g,boardPositionX,boardPositionY,Color.LIGHT_GRAY);
    if(!controller.getBomb()&&field.getFlagged()&&field.getFlags()!=field.getMineNumber())
      drawFlags(g, field.getFlags(), boardPositionX, boardPositionY, Color.RED);
  }
  public GameController getController() {
    return controller;
  }

}
