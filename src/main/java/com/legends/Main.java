package com.legends;

import com.legends.game.Game;
import com.legends.game.QuitGameException;
import com.legends.io.ConsoleInput;
import com.legends.io.ConsoleOutput;

/**
 * Main class to start the Legends: Monsters and Heroes game.
 */
public class Main {
    /**
     * The entry point of the application.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        try {
            Game game = new Game(new ConsoleInput(), new ConsoleOutput());
            game.init();
            game.start();
        } catch (QuitGameException e) {
            System.out.println("\n" + e.getMessage());
            System.out.println("Goodbye!");
        }
    }
}
