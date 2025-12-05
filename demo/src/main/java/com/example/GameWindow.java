package com.example;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
public class GameWindow extends JFrame {
    private GameMenuBar gameMenuBar;
    private GameBoard gameBoard;
    private GameController controller;
    private HighscoreManager highscoreManager;

    private JLabel minesLabel;
    private JLabel doubleMinesLabel;
    private JLabel tripleMinesLabel;
    private JLabel timerLabel;
    private JPanel infoPanel;
    private Timer timer;
    private int timePassed = 0;

    private JPanel sidePanel;
    private JTextField nameField;
    private JLabel nameLabel;
    private JTextArea highscoreArea;
    private JLabel highscoreLabel;

    public void setInfoPanel(GameModes mode) {
        infoPanel.removeAll();
        minesLabel = new JLabel("Mines: " + controller.getUnflaggedOneMines());
        timerLabel = new JLabel("Time: " + timePassed);
        infoPanel.add(minesLabel);
        if (mode == GameModes.RAT) {
            doubleMinesLabel = new JLabel("Double Mines: " + controller.getUnflaggedTwoMines());
            tripleMinesLabel = new JLabel("Triple Mines: " + controller.getUnflaggedThreeMines());
            infoPanel.add(doubleMinesLabel);
            infoPanel.add(tripleMinesLabel);
        }
        infoPanel.add(timerLabel);
        infoPanel.revalidate();
        infoPanel.repaint();
    }

    private void setSidePanel() {
        sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        nameLabel = new JLabel("Your name:");
        nameField = new JTextField(10);
        highscoreLabel = new JLabel("Highscores:");
        highscoreArea = new JTextArea(10, 12);
        highscoreArea.setEditable(false);
        sidePanel.add(nameLabel);
        sidePanel.add(nameField);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(highscoreLabel);
        sidePanel.add(new JScrollPane(highscoreArea));
    }
    public void refreshHighscoreArea(){
        StringBuilder sb= new StringBuilder();
        int index=1;
        for(HighscoreEntry entry:highscoreManager.getHighscoreEntries()){
            sb.append(index++)
              .append(".")
              .append(entry.getName())
              .append("-")
              .append(entry.getTime())
              .append("sec \n");
        }
        highscoreArea.setText(sb.toString());
    }
    public void saveHighscoreAfterWin(int timePassed,File outputFile){
        String name= nameField.getText().trim();
        if(name.isEmpty()){
            name="Anonymus";
        }
        HighscoreEntry newEntry= new HighscoreEntry(timePassed, name);
        highscoreManager.addScore(newEntry);
        if(highscoreManager.getHighscoreEntries().contains(newEntry)){
            highscoreManager.saveHighscore(outputFile);
            refreshHighscoreArea();
            JOptionPane.showMessageDialog(this,"Congratulations! You won and made it into the top 10.");
        }
        else{
             JOptionPane.showMessageDialog(this,"Congratulations! You won.");
        }
    }
    public void writeMessageAfterLosing(){
        JOptionPane.showMessageDialog(this,"You lost. Better luck next time!");
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
        setSidePanel();
        this.add(sidePanel, BorderLayout.EAST);
        gameBoard = new GameBoard();
        this.add(gameBoard, BorderLayout.CENTER);
        this.pack();
        setVisible(true);
        
    }

    private void startTimer() {
        stopTimer();
        timer = new Timer(1000, e -> {
            timePassed++;
            timerLabel.setText("Time:" + timePassed);
        });
        timer.start();
    }
    
    public void stopTimer() {
        if (timer != null)
            timer.stop();
    }

    public void updateMines(int remainingMines, int flagnumber) {
        if (flagnumber == 1)
            minesLabel.setText("Mines: " + remainingMines);
        else if(flagnumber==2)
            doubleMinesLabel.setText("Double mines:"+remainingMines);
        else if(flagnumber==3)
            tripleMinesLabel.setText("Triple mines:"+ remainingMines);
    }

    public void startNewGame(GameModes mode, Difficulties dif) {
        controller = new GameController(mode, dif, gameBoard, this);
        controller.setGameTable();
        gameBoard.setGameController(controller);
        gameBoard.repaint();
        timePassed = 0;
        setInfoPanel(mode);
        startTimer();
        highscoreManager= new HighscoreManager(10);
        File inputFile= highscoreManager.getTextFileByModeAndDifficulty(mode, dif);
        highscoreManager.loadHighScores(inputFile);
        refreshHighscoreArea();
    }
    public HighscoreManager getHighscoreManager(){
        return highscoreManager;
    }
    public int getTimePassed(){
        return timePassed;
    }

}
