package com.example;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
public class GameMenuBar extends JMenuBar {
    public GameMenuBar(){
        JMenuBar menuBar=new JMenuBar();
        JMenu gameMenu= new JMenu("Game");
        gameMenu.add(new JMenuItem("New Game"));
        JMenu difficultyMenu = new JMenu("Difficulty");
        difficultyMenu.add(new JMenuItem("Easy"));
        difficultyMenu.add(new JMenuItem("Medium"));
        difficultyMenu.add(new JMenuItem("Hard"));
        JMenu gameModeMenu= new JMenu("Gamemode");
        gameModeMenu.add(new JMenuItem("Default"));
        gameModeMenu.add(new JMenuItem("Rat"));
        menuBar.add(gameMenu);
        menuBar.add(difficultyMenu);
        menuBar.add(gameModeMenu);
        
}
