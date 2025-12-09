package com.example;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

/**
 * @brief A GameMouseAdapter kezeli az egér kattintásokat a játéktáblán.
 *
 * Feladata, hogy a GameBoard panelen történt egér eseményeket a 
 * GameController megfelelő metódusaihoz továbbítsa.
 *
 * Kapcsolódó osztályok:
 * - GameBoard: a panel, amin az adapter figyeli az egér eseményeket
 * - MouseClicks: az egérgombok típusát reprezentáló enum
 * - Position: a kattintás helyének táblabeli koordinátái
 */
public class GameMouseAdapter extends MouseAdapter {
    private GameBoard board;

    /**
     * A GameMouseAdapter osztály konstruktora, megkapja
     * a hozzá tartozó  játékpanelt.
     * 
     * @param board a játékpanel
     */
    public GameMouseAdapter(GameBoard board) {
        this.board = board;
    }

    /**
     * Felülírja a gombnyomás műveletét, úgy, hogy a játékvezérlő
     * mouseChoser metódusát hívja meg, azzal a gombbal ami meg lett nyomva
     * 
     * @param e az egérrel történt esemény
     */
    @Override
    public void mousePressed(MouseEvent e) {
        MouseClicks click;
        Position pos = board.getCellFromCoordinates(e.getX(), e.getY());
        if (SwingUtilities.isLeftMouseButton(e))
            click = MouseClicks.LEFT;
        else if (SwingUtilities.isRightMouseButton(e))
            click = MouseClicks.RIGHT;
        else return;
        board.getController().mouseChoser(click, board.getController().getTable().getFieldByPosition(pos));
    }
}
