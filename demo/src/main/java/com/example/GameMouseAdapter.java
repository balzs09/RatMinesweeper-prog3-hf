package com.example;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
public class GameMouseAdapter extends MouseAdapter {
    private GameBoard board;
    
    public GameMouseAdapter(GameBoard board){
        this.board=board;
    }
    @Override
    public void mousePressed(MouseEvent e){
        MouseClicks click=MouseClicks.LEFT;
        Position pos= board.getCellFromCoordinates(e.getX(),e.getY());
        if(SwingUtilities.isLeftMouseButton(e)) click=MouseClicks.LEFT;
        else if(SwingUtilities.isRightMouseButton(e)) click= MouseClicks.RIGHT;
        board.getController().mouseChoser(click, board.getController().getTable().getFieldByPosition(pos));
    }
}
