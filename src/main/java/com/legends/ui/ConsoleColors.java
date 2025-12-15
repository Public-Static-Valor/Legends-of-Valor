package com.legends.ui;

/**
 * ANSI color codes for console output.
 * Provides a wide range of colors and text styles for enhanced visual experience.
 */
public class ConsoleColors {
    // Reset
    public static final String RESET = "\033[0m";
    
    // Regular Colors
    public static final String BLACK = "\033[0;30m";
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String BLUE = "\033[0;34m";
    public static final String MAGENTA = "\033[0;35m";
    public static final String CYAN = "\033[0;36m";
    public static final String WHITE = "\033[0;37m";
    
    // Bold
    public static final String BOLD = "\033[1m";
    public static final String BLACK_BOLD = "\033[1;30m";
    public static final String RED_BOLD = "\033[1;31m";
    public static final String GREEN_BOLD = "\033[1;32m";
    public static final String YELLOW_BOLD = "\033[1;33m";
    public static final String BLUE_BOLD = "\033[1;34m";
    public static final String MAGENTA_BOLD = "\033[1;35m";
    public static final String CYAN_BOLD = "\033[1;36m";
    public static final String WHITE_BOLD = "\033[1;37m";
    
    // Underline
    public static final String UNDERLINE = "\033[4m";
    
    // Bright (High Intensity)
    public static final String BRIGHT_BLACK = "\033[0;90m";
    public static final String BRIGHT_RED = "\033[0;91m";
    public static final String BRIGHT_GREEN = "\033[0;92m";
    public static final String BRIGHT_YELLOW = "\033[0;93m";
    public static final String BRIGHT_BLUE = "\033[0;94m";
    public static final String BRIGHT_MAGENTA = "\033[0;95m";
    public static final String BRIGHT_CYAN = "\033[0;96m";
    public static final String BRIGHT_WHITE = "\033[0;97m";
    
    // Background Colors
    public static final String BG_BLACK = "\033[40m";
    public static final String BG_RED = "\033[41m";
    public static final String BG_GREEN = "\033[42m";
    public static final String BG_YELLOW = "\033[43m";
    public static final String BG_BLUE = "\033[44m";
    public static final String BG_MAGENTA = "\033[45m";
    public static final String BG_CYAN = "\033[46m";
    public static final String BG_WHITE = "\033[47m";
    
    // Bright Background Colors
    public static final String BG_BRIGHT_BLACK = "\033[0;100m";
    public static final String BG_BRIGHT_RED = "\033[0;101m";
    public static final String BG_BRIGHT_GREEN = "\033[0;102m";
    public static final String BG_BRIGHT_YELLOW = "\033[0;103m";
    public static final String BG_BRIGHT_BLUE = "\033[0;104m";
    public static final String BG_BRIGHT_MAGENTA = "\033[0;105m";
    public static final String BG_BRIGHT_CYAN = "\033[0;106m";
    public static final String BG_BRIGHT_WHITE = "\033[0;107m";
    
    // Custom colors for specific game purposes
    public static final String DARK_RED = "\033[38;5;52m";  // For defeat
    public static final String GOLD = "\033[38;5;220m";      // For money/transactions
    public static final String ORANGE = "\033[38;5;208m";    // For warnings
    public static final String PURPLE = "\033[38;5;93m";     // For magic/spells
    
    /**
     * Wraps text with a color
     */
    public static String colorize(String text, String color) {
        return color + text + RESET;
    }
    
    /**
     * Makes text bold
     */
    public static String bold(String text) {
        return BOLD + text + RESET;
    }
    
    /**
     * Underlines text
     */
    public static String underline(String text) {
        return UNDERLINE + text + RESET;
    }
    
    /**
     * Combines color and bold
     */
    public static String colorBold(String text, String color) {
        return BOLD + color + text + RESET;
    }
}
