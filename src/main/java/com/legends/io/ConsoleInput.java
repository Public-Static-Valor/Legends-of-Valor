package com.legends.io;

import java.util.Scanner;

import com.legends.game.QuitGameException;

/**
 * Implementation of the Input interface using the console.
 */
public class ConsoleInput implements Input {
    private Scanner scanner;

    /**
     * Constructs a new ConsoleInput.
     */
    public ConsoleInput() {
        // Use a shared scanner if possible, or create new one but be careful
        // For now, we'll stick to new Scanner(System.in) but there can be buffering
        this.scanner = new Scanner(System.in);
    }

    /**
     * Constructs a new ConsoleInput with a specific scanner.
     */
    public ConsoleInput(Scanner scanner) {
        this.scanner = scanner;
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
