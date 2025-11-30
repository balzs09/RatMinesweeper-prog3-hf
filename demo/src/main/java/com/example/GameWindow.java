package com.example;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
public class GameWindow extends JFrame {
    private GameMenuBar gameMenuBar;
    private GameBoard gameBoard;
    private GameController controller;
    private JLabel minesLabel;
    private JLabel doubleMinesLabel;
    private JLabel tripleMinesLabel;
    private JLabel timerLabel;
    private JPanel infoPanel;
    private Timer timer;
    private int timePassed=0;

    public void  setInfoPanel(GameModes mode){
        infoPanel.removeAll();
        minesLabel = new JLabel("Mines: "+controller.getUnflaggedOneMines());
        timerLabel = new JLabel("Time: "+timePassed);
        infoPanel.add(minesLabel);
        if(mode==GameModes.RAT){
            doubleMinesLabel = new JLabel("Double Mines: "+controller.getUnflaggedTwoMines());
            tripleMinesLabel = new JLabel("Triple Mines: "+controller.getUnflaggedThreeMines());
            infoPanel.add(doubleMinesLabel);
            infoPanel.add(tripleMinesLabel);
        }
        infoPanel.add(timerLabel);
        infoPanel.revalidate();
        infoPanel.repaint();
    }
    public GameWindow() {
        setTitle("Minesweeper");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        gameMenuBar = new GameMenuBar(this);
        setJMenuBar(gameMenuBar);
        infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        add(infoPanel, BorderLayout.NORTH);
        gameBoard = new GameBoard();
        this.add(gameBoard);
        setVisible(true);
        this.pack();
    }
    private void startTimer(){
        stopTimer();
        timer = new Timer(1000, e -> {
            timePassed++;
            timerLabel.setText("Time:"+timePassed);
        }
        );
        timer.start();
    }
    public void stopTimer(){
         if(timer!= null) timer.stop();
    }
    public void updateMines(int remainingMines, int flagnumber){
       if(flagnumber==1) minesLabel.setText("Mines: "+remainingMines);
    }
    public void startNewGame(GameModes mode, Difficulties dif) {
        controller = new GameController(mode, dif, gameBoard,this);
        controller.setGameTable();
        gameBoard.setGameController(controller);
        gameBoard.repaint();
        timePassed=0;
        setInfoPanel(mode);
        startTimer();

    }

}
