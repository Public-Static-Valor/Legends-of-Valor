package com.legends.io;

/**
 * Interface for output operations.
 */
public interface Output {
    /**
     * Prints an object.
     *
     * @param s The object to print.
     */
    void print(Object s);

    /**
     * Prints an object followed by a newline.
     *
     * @param s The object to print.
     */
    void println(Object s);

    /**
     * Prints a newline.
     */
    void println();

    /**
     * Prints an error message.
     *
     * @param s The error message to print.
     */
    void printError(Object s);

    /**
     * Prints a message in green.
     *
     * @param s The message to print.
     */
    void printlnGreen(Object s);

    /**
     * Prints a message in red.
     *
     * @param s The message to print.
     */
    void printlnRed(Object s);
}
