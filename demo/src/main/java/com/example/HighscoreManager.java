package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class HighscoreManager {
    private ArrayList<HighscoreEntry> highscores;
    private int maxEntrynumber;

    /**
     * A HighscoreManager osztály konstruktora.
     * Létrehoz egy listát és beállítja a lista maximális tárolható eredményszámot
     *  a paraméterként megkapottal
     * 
     * @param maxEntry maximum eredmény szám, amennyi benne lehet a listában.
     */
    public HighscoreManager(int maxEntry) {
        highscores = new ArrayList<>();
        maxEntrynumber = maxEntry;
    }

    /**
     * Ez a metódus meghatározza a játékhoz tartozó bináris fájlt.
     * Ezt a játékmód és a nehézség alapján teszi meg.
     * Ezt a fájlt téríti vissza eredményként.
     * 
     * @param mode       játékmód
     * @param difficulty játék nehézsége
     * @return a metódus által meghatározott fájl
     */
    public File getTextFileByModeAndDifficulty(GameModes mode, Difficulties difficulty) {
        if (mode == GameModes.DEFAULT) {
            if (difficulty == Difficulties.EASY)
                return new File("Default-Easy.dat");
            if (difficulty == Difficulties.MEDIUM)
                return new File("Default-Medium.dat");
            if (difficulty == Difficulties.HARD)
                return new File("Default-Hard.dat");
        } else {
            if (difficulty == Difficulties.EASY)
                return new File("Rat-Easy.dat");
            if (difficulty == Difficulties.MEDIUM)
                return new File("Rat-Medium.dat");
            if (difficulty == Difficulties.HARD)
                return new File("Rat-Hard.dat");

        }
        return new File("Error.txt");
    }

    /**
     * Ez a metódus kiolvassa a bináris fájlban szereplő eredményeket.
     * Ha a fájl nem létezik visszatér, ha pedig hiba történik az olvasás közben,
     * a hibát elkapja.
     * 
     * @param readableHighscores az erdményeket tartalmazó bináris fájl
     */
    public void loadHighScores(File readableHighscores) {
        if (!readableHighscores.exists())
            return;
        try (FileInputStream fs = new FileInputStream(readableHighscores);
                ObjectInputStream ois = new ObjectInputStream(fs)) {
            ArrayList<HighscoreEntry> readedEntries = (ArrayList<HighscoreEntry>) ois.readObject();
            highscores.addAll(readedEntries);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error during reading: " + e.getMessage());
        }

    }

    /**
     * Ez a metódus kiíratja egy bináris fájlba a játék legjobb eredményeit.
     * Ha hiba történik fájlba írás közben, a hibát elkapja.
     * 
     * @param writeableHighscores a fájl, amelybebe az eredményeket írjuk
     */
    public void saveHighscore(File writeableHighscores) {
        try (FileOutputStream fs = new FileOutputStream(writeableHighscores);
                ObjectOutputStream oos = new ObjectOutputStream(fs)) {
            oos.writeObject(highscores);
        } catch (IOException e) {
            System.out.println("Error during writing: " + e.getMessage());
        }
    }

    /**
     * Ez a metódus hozzáadja a paraméterként megkapott eredményt, ha
     * annak ideje a megengedett számú legjobb idő között szerepel.
     * Ha már volt előtte a listában maximális számú eredmény és az új eredmény is bekerült
     * az utolsó a listából törlődik.
     * A listában az eredmények idő szerinti növekvő sorrendben szerepelnek.
     * 
     * @param entry az új eredmény
     */
    public void addScore(HighscoreEntry entry) {
        boolean inserted = false;
        for (int i = 0; i < highscores.size(); i++) {
            if (entry.getTime() < highscores.get(i).getTime()) {
                highscores.add(i, entry);
                inserted = true;
                break;
            }
        }
        if (inserted == false && highscores.size() < maxEntrynumber)
            highscores.add(entry);
        if (highscores.size() > maxEntrynumber)
            highscores.removeLast();
    }

    /**
     * Ez a metódus visszatéríti az legjobb eredmények listáját
     * 
     * @return legjobb eredmények listája
     */
    public ArrayList<HighscoreEntry> getHighscoreEntries() {
        return highscores;
    }

}
