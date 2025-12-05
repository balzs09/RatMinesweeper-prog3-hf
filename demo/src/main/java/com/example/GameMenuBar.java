package com.example;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
public class GameMenuBar extends JMenuBar {
    private Difficulties selectedDifficulty = null;
    private GameModes selectedGameMode = null;
    private GameWindow window;

    public JMenu getDifficultyMenu(){
        JMenu difficultyMenu = new JMenu("Difficulty");
        JMenuItem easyItem = new JMenuItem("Easy");
        easyItem.addActionListener(e -> selectedDifficulty = Difficulties.EASY);
        difficultyMenu.add(easyItem);
        JMenuItem mediumItem = new JMenuItem("Medium");
        mediumItem.addActionListener(e -> selectedDifficulty=Difficulties.MEDIUM);
        difficultyMenu.add(mediumItem);
        JMenuItem hardItem = new JMenuItem("Hard");
        hardItem.addActionListener(e -> selectedDifficulty=Difficulties.HARD);
        difficultyMenu.add(hardItem);
        return difficultyMenu;
    }
    
    public JMenu getGameModeMenu(){
        JMenu gameModeMenu= new JMenu("Gamemode");
        JMenuItem defaultItem= new JMenuItem("Default");
        defaultItem.addActionListener(e -> selectedGameMode=GameModes.DEFAULT);
        gameModeMenu.add(defaultItem);
        JMenuItem ratItem= new JMenuItem("Rat");
        ratItem.addActionListener(e -> selectedGameMode=GameModes.RAT);
        gameModeMenu.add(ratItem);
        return gameModeMenu;
    }
    public boolean modeOrDifficultyIsNotSelected(){
        if (selectedDifficulty == null || selectedGameMode == null) {
            JOptionPane.showMessageDialog(null,
            "Please select difficulty AND game mode first!",
            "Missing configuration", JOptionPane.WARNING_MESSAGE);
            return true;
        }
        return false;
    }
    public JMenuItem getNewGameMenuItem(){
        JMenuItem newGameItem= new JMenuItem("New Game");
        newGameItem.addActionListener(e ->{
            if(modeOrDifficultyIsNotSelected()) return;
            window.startNewGame(selectedGameMode,selectedDifficulty);
        });
        return newGameItem;
    }
    public GameMenuBar(GameWindow window){
       this.window=window;
       add(getDifficultyMenu());
       add(getGameModeMenu());
       add(getNewGameMenuItem());
    }
  
}
