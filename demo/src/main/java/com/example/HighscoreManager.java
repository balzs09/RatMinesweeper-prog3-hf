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
    private int maxEntrynumber ;
    public HighscoreManager(int maxEntry){
        highscores= new ArrayList<>();
        maxEntrynumber=maxEntry;
    }
    public  File getTextFileByModeAndDifficulty(GameModes mode, Difficulties difficulty){
       if(mode==GameModes.DEFAULT){
        if(difficulty==Difficulties.EASY) return new File("Default-Easy.dat");
        if(difficulty==Difficulties.MEDIUM) return new File("Default-Medium.dat");
        if(difficulty==Difficulties.HARD) return new File("Default-Hard.dat");
       }
       else{
        if(difficulty==Difficulties.EASY) return new File("Rat-Easy.dat");
        if(difficulty==Difficulties.MEDIUM) return new File("Rat-Medium.dat");
        if(difficulty==Difficulties.HARD) return new File("Rat-Hard.dat");
        
       }
       return new File("Error.txt");
       
    }
    public void loadHighScores(File readableHighscores) {
        if (!readableHighscores.exists())
            return ;
        try (FileInputStream fs = new FileInputStream(readableHighscores);
                ObjectInputStream ois = new ObjectInputStream(fs)) {
            ArrayList<HighscoreEntry> readedEntries = (ArrayList<HighscoreEntry>) ois.readObject();
            highscores.addAll(readedEntries);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error durring reading: " + e.getMessage());
        }

    }

    public void saveHighscore(File writeableHighscores) {
        try (FileOutputStream fs = new FileOutputStream(writeableHighscores);
                ObjectOutputStream oos = new ObjectOutputStream(fs)) {
            oos.writeObject(highscores);
        } catch (IOException e) {
            System.out.println("Error durring writing: " + e.getMessage());
        }
    }

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
    public ArrayList<HighscoreEntry> getHighscoreEntries(){
        return highscores;
    }
  

}
