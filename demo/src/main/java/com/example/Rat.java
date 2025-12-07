package com.example;

public class Rat {
    private Position currentPosition;
    private Position goalPosition;

    /**
     * A Rat osztály konstruktora.
     * Beállítja a jelenlegi és célpozíciót is a paraméterként megkapottra
     * 
     * @param currentPosition jelenlegi pozíció, a játék kezdetén a célpozíció is
     *                        ezt az értéket kapja meg
     */
    public Rat(Position currentPosition) {
        this.currentPosition = currentPosition;
        goalPosition = currentPosition;
    }

    /**
     * Beállítja a célpozíció értékét.
     * 
     * @param goalPosition- célpozíció értéke
     */
    public void setGoal(Position goalPosition) {
        this.goalPosition = goalPosition;
    }

    /**
     * Beállítja a jelenlegi pozíció értékét.
     * 
     * @param currentPosition- jelenlegi pozíció értéke
     */
    public void setCurrentPosition(Position currentPosition) {
        this.currentPosition = currentPosition;
    }

    /**
     * isszatéríti a jelenlegi pozícióval.
     * 
     * @return  jelenlegi pozíció
     */
    public Position getCurrentPosition() {
        return currentPosition;
    }

    /**
     * Visszatéríti a célpozícióval.
     * 
     * @return célpozíció
     */
    public Position getGoalPosition() {
        return goalPosition;
    }

}
