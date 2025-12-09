package com.example;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenu;

/**
 * @brief A GameMenuBar kezeli a játék ablak menüsávját.
 *
 * Feladata, hogy a felhasználó kiválaszthassa a nehézségi szintet, 
 * játékmódot, valamint új játékot indíthasson.
 *
 * Kapcsolódó osztályok:
 * - GameWindow: az ablak, amelyhez a menüsáv tartozik
 * - Difficulties: az elérhető nehézségi szinteket tartalmazó enum
 * - GameModes: az elérhető játékmódokat tartalmazó enum
 * - JMenuBar, JMenu, JMenuItem: a Swing komponensek, amiket a menüsáv használ
 */
public class GameMenuBar extends JMenuBar {
    private Difficulties selectedDifficulty = null;
    private GameModes selectedGameMode = null;
    private GameWindow window;
    /**
     * Ez a metódus egy új JMenu-t generál, amely neve "Difficulties"
     * Ehhez  a könnyű, közepes és nehéz  nehézségi szintek vannak hozzáadva.
     * Minden menüponthoz hozzárendeli a megfelelő egérkattintás kezelést,
     * amely beállítja a kiválasztott nehézséget.
     * 
     * @return a létrehozott JMenu objektum
     */
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
    
    /**
     * Ez a metódus egy új JMenu-t generál, amely neve "Gamemode"
     * Ehhez a default és rat játékmód van hozzáadva.
     * Mindkettő menüponthoz hozzárendeli a megfelelő egérkattintás kezelést,
     * amely beállítja a kiválasztott játékmódot.
     * 
     * @return a létrehozott JMenu objektum
     */
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
    /**
     * Ha a nehézségi szint, vagy a játékmód nincs kiválasztva 
     * megjelenik egy hibaüzenet.
     * A metódus ebben az esetben true értékkel tér vissza.
     * Ha mindkettő ki van választva false értékkel tér vissza.
     * 
     * @return true, ha valamelyik nincs kiválasztva
     *         false, ha mindkettő ki van választva
     */
    public boolean modeOrDifficultyIsNotSelected(){
        if (selectedDifficulty == null || selectedGameMode == null) {
            JOptionPane.showMessageDialog(null,
            "Please select difficulty AND game mode first!",
            "Missing configuration", JOptionPane.WARNING_MESSAGE);
            return true;
        }
        return false;
    }
    /**
     * Ez a metódus egy új JMenuItem-et generál, amely neve "Gamemode".
     * Ha a nehézségi szint és játékmód is ki van választva, 
     * a menüpontra kattintva elindul egy új játék.
     * 
     * @return a létrehozott JMenuItem objektum
     */
    public JMenuItem getNewGameMenuItem(){
        JMenuItem newGameItem= new JMenuItem("New Game");
        newGameItem.addActionListener(e ->{
            if(modeOrDifficultyIsNotSelected()) return;
            window.startNewGame(selectedGameMode,selectedDifficulty);
        });
        return newGameItem;
    }
    /**
     * Létrehozza a GameMenuBar objektumot és hozzáadja a menüpontokat.
     * 
     * @param window az ablak amelyen szerepel a menü
     */
    public GameMenuBar(GameWindow window){
       this.window=window;
       add(getDifficultyMenu());
       add(getGameModeMenu());
       add(getNewGameMenuItem());
    }
  
}
