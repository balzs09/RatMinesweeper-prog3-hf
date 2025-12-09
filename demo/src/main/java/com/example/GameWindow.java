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

/**
 * @brief A játék főablaka, amely tartalmazza a menüt, a játékpanelt,
 *        az információs panelt és a ranglistát.
 *
 *        Feladata:
 *        - A GameBoard és GameController összekapcsolása.
 *        - Az eltelt idő és a be nem jelölt bombák számának megjelenítése.
 *        - A HighscoreManager kezelése, játékos eredmények mentése és
 *        megjelenítése.
 *        - Új játék indítása a kiválasztott nehézségi szint és játékmód
 *        alapján.
 *
 *        Kapcsolódó osztályok:
 *        - GameBoard: a játékmezők kirajzolása.
 *        - GameController: a játék logikájának vezérlése.
 *        - GameMenuBar: a játék menüje, innen lehet indítani új játékot.
 *        - HighscoreManager: a legjobb eredmények kezelése.
 */
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

    /**
     * Ez a metódus beállítja az infoPanelt, ezen szerepel az eltelt idő
     * és a bejelöletlen bombák száma.
     * RAT játékmód esetén az is felkerül, hogy hány két és három bombát
     * tartalmazó bejelöletlen mező van.
     * 
     * @param mode a kiválasztott játékmód
     */
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

    /**
     * Ez a metódus beállítja a sidePanelt.
     * Ezen szerepel a jelenlegi játékhoz tartozó ranglista.
     * A ranglistában a játékosok neve és az elért ideje szerepel.
     * Ez a játékos által kézzel nem szerkeszthető.
     * 
     */
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

    /**
     * Ez a metódus frissíti a ranglista megjelenítését.
     * Általa az új játékos eredménye is bekerül.
     */
    public void refreshHighscoreArea() {
        StringBuilder sb = new StringBuilder();
        int index = 1;
        for (HighscoreEntry entry : highscoreManager.getHighscoreEntries()) {
            sb.append(index++)
                    .append(".")
                    .append(entry.getName())
                    .append("-")
                    .append(entry.getTime())
                    .append("sec \n");
        }
        highscoreArea.setText(sb.toString());
    }

    /**
     * Ez a metódus elmenti a nyertes eredmlnyt, ha az benne van a legjobb 10-ben.
     * Ha nincs név beírva akkor a név helyére Anonymus kerül.
     * Meghívja a legjobb eredmények hozzáadási metódusát.
     * Ez akkor adja hozzá, ha a legjobb 10 idő között van.
     * Ha hozzáadás után szerepel az új eredmény a legjobb eredmények listájában
     * a legjobb eredmények frissülnek az ablakban és elmenti az eredményeket egy
     * bináris fájlban.
     * Más eredmény jelenik meg, ha bekerül a játékos a top 10-be, mint ha
     * nem kerülne be.
     * 
     * @param timePassed az eltelt idő másodpercben
     * @param outputFile a kimeneti bináris fájl
     */
    public void saveHighscoreAfterWin(int timePassed, File outputFile) {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            name = "Anonymus";
        }
        HighscoreEntry newEntry = new HighscoreEntry(timePassed, name);
        highscoreManager.addScore(newEntry);
        if (highscoreManager.getHighscoreEntries().contains(newEntry)) {
            highscoreManager.saveHighscore(outputFile);
            refreshHighscoreArea();
            JOptionPane.showMessageDialog(this, "Congratulations! You won and made it into the top 10.");
        } else {
            JOptionPane.showMessageDialog(this, "Congratulations! You won.");
        }
    }

    /**
     * Ez a metódus vesztes játék után ír ki üzenetet.
     * 
     */
    public void writeMessageAfterLosing() {
        JOptionPane.showMessageDialog(this, "You lost. Better luck next time!");
    }

    /**
     * A GameWindow osztály konstruktora.
     * Hozzáadja az ablakhoz a gameMenuBar, infopanel, sidePanel és gameBoard
     * objektumokat. Az ablak nagy részét a játékpanel teszi ki.
     * Az infopanel a felső részre kerül, a sidePanel az ablak jobb oldalára.
     * Kilépést az X-et megnyomva lehet végrehajtani.
     * 
     */
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

    /**
     * Ez a metódus elínditja az eltelt idő számítását.
     * 
     */
    private void startTimer() {
        stopTimer();
        timer = new Timer(1000, e -> {
            timePassed++;
            timerLabel.setText("Time:" + timePassed);
            infoPanel.revalidate();
            infoPanel.repaint();
        });
        timer.start();

    }

    /**
     * Ez a metódus megállítja az eltelt idő számítását.
     */
    public void stopTimer() {
        if (timer != null)
            timer.stop();
    }

    /**
     * Frissiti a be nem jelölt bombát tartalmazó mezők számát az ablakban.
     * 
     * @param remainingMines be nem jelölt bombák száma
     * @param flagnumber     bomba típusa(1-normál, 2-dupla, 3-tripla)
     */
    public void updateMines(int remainingMines, int flagnumber) {
        if (flagnumber == 1)
            minesLabel.setText("Mines: " + remainingMines);
        else if (flagnumber == 2)
            doubleMinesLabel.setText("Double mines:" + remainingMines);
        else if (flagnumber == 3)
            tripleMinesLabel.setText("Triple mines:" + remainingMines);
    }

    /**
     * Ez a metódus elindít egy új játékot
     * Felrajzolja a játékpanel kezdeti állapotát
     * Elkezdi az idő számolását.
     * Beolvassa az eddigi eredményeket ebben a játékmódban és nehézségben
     * 
     * @param mode a játékmód
     * @param dif  a játék nehézsége
     */
    public void startNewGame(GameModes mode, Difficulties dif) {
        controller = new GameController(mode, dif, gameBoard, this);
        controller.setGameTable();
        gameBoard.setGameController(controller);
        gameBoard.repaint();
        timePassed = 0;
        setInfoPanel(mode);
        startTimer();
        highscoreManager = new HighscoreManager(10);
        File inputFile = highscoreManager.getTextFileByModeAndDifficulty(mode, dif);
        highscoreManager.loadHighScores(inputFile);
        refreshHighscoreArea();
    }

    /**
     * Ez a metódus visszatéríti az ablakhoz tartozó HighScoreManager objektumot.
     * 
     * @return jelenlegi játékhoz tartozó HighScoreManager objektum
     */
    public HighscoreManager getHighscoreManager() {
        return highscoreManager;
    }

    /**
     * Ez a metódus visszatéríti az eltelt időt másodpercben
     * 
     * @return az eltelt idő másodpercben
     */
    public int getTimePassed() {
        return timePassed;
    }

}
