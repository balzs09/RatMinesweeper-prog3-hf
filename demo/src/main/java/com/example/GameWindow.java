package com.example;
import javax.swing.JFrame;
public class GameWindow extends JFrame {
    private GameMenuBar gameMenuBar;
    private GameBoard gameBoard;
    public GameWindow(){
        setTitle("Minesweeper");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        gameBoard= new GameBoard();
        this.add(gameBoard);
        gameMenuBar= new GameMenuBar();
        this.add(gameMenuBar);
        setVisible(true);
        this.pack();


    }

}
