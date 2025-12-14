package com.legends.io;

/**
 * Implementation of the Output interface using the console.
 */
public class ConsoleOutput implements Output {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";

    /**
     * Creates a colored progress bar string.
     *
     * @param current Current value.
     * @param max     Maximum value.
     * @param color   ANSI color code.
     * @return The formatted progress bar string.
     */
    public static String createProgressBar(int current, int max, String color) {
        int totalLength = 10;
        int filledLength = 0;
        if (max > 0) {
            filledLength = (int) ((((double) current) / max) * totalLength);
        }
        if (filledLength > totalLength) filledLength = totalLength;
        if (filledLength < 0) filledLength = 0;

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(color);
        for (int i = 0; i < filledLength; i++) {
            sb.append("=");
        }
        sb.append(ANSI_RESET);
        for (int i = filledLength; i < totalLength; i++) {
            sb.append(" ");
        }
        sb.append("] " + current + "/" + max);
        return sb.toString();
    }

    /**
     * Prints an object to the console without a newline.
     *
     * @param s The object to print.
     */
    @Override
    public void print(Object s) {
        System.out.print(s);
    }

    /**
     * Prints an object to the console followed by a newline.
     *
     * @param s The object to print.
     */
    @Override
    public void println(Object s) {
        System.out.println(s);
    }

    /**
     * Prints a newline to the console.
     */
    @Override
    public void println() {
        System.out.println();
    }

    /**
     * Prints an error message to the standard error stream.
     *
     * @param s The error message to print.
     */
    @Override
    public void printError(Object s) {
        System.err.println(s);
    }

    /**
     * Prints a message in green color to the console.
     *
     * @param s The message to print.
     */
    @Override
    public void printlnGreen(Object s) {
        System.out.println(ANSI_GREEN + s + ANSI_RESET);
    }

    /**
     * Prints a message in red color to the console.
     *
     * @param s The message to print.
     */
    @Override
    public void printlnRed(Object s) {
        System.out.println(ANSI_RED + s + ANSI_RESET);
    }
}
