package com.legends.io;

import java.util.Scanner;

import com.legends.Game.QuitGameException;

/**
 * Implementation of the Input interface using the console.
 */
public class ConsoleInput implements Input {
    private Scanner scanner;

    /**
     * Constructs a new ConsoleInput.
     */
    public ConsoleInput() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads a line of text from the console.
     *
     * @return The line read from the console.
     * @throws QuitGameException If the user enters 'q' or 'Q'.
     */
    @Override
    public String readLine() {
        String line = scanner.nextLine();
        if (line != null && line.trim().equalsIgnoreCase("q")) {
            throw new QuitGameException("Player quit the game.");
        }
        return line;
    }
}
