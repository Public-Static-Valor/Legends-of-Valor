package com.legends.game;

/**
 * Exception thrown when the player chooses to quit the game.
 * Used to break out of the game loop.
 */
public class QuitGameException extends RuntimeException {
    /**
     * Constructs a new QuitGameException with the specified message.
     *
     * @param message The detail message.
     */
    public QuitGameException(String message) {
        super(message);
    }
}
