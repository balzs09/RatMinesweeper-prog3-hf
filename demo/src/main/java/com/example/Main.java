package com.example;

/**
 * @brief A program belépési pontja.
 *
 * Feladata:
 * - elindítani a játékot egy új GameWindow ablak létrehozásával.
 *
 * Kapcsolódó osztályok:
 * - GameWindow: a játék grafikus felülete és fő logikája.
 */
public class Main {
    /**
     * A Main osztály main metódusa egy új játékablakot indít el.
     * @param args
     */
    public static void main(String[] args) {
         new GameWindow();
    }
}