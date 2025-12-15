package com.legends.ui;

/**
 * Centralized repository for all ASCII art used in the game.
 * Provides icons and decorative elements for enhanced visual experience.
 * All art is terminal-compatible using standard ASCII characters.
 * Java 8 compatible - no String.repeat() usage.
 */
public class AsciiArt {

    // Combat Icons - Simple and terminal compatible
    public static final String SWORD_ATTACK = "[>]";
    public static final String SHIELD_BLOCK = "[#]";
    public static final String DODGE = "<<";
    public static final String CRITICAL_HIT = "**";

    // Item Icons
    public static final String POTION = "(!)";
    public static final String SPELL_SCROLL = "[?]";
    public static final String WEAPON = "[S]";
    public static final String ARMOR = "[A]";
    public static final String GOLD = "[$]";

    // Status Icons
    public static final String HEART = "<3";
    public static final String MANA = "[M]";
    public static final String LEVEL_UP = "[^]";
    public static final String DEATH = "[X]";
    public static final String VICTORY = "[V]";
    public static final String DEFEAT = "[D]";

    // Location Icons
    public static final String MARKET = "[M]";
    public static final String NEXUS = "[N]";
    public static final String TELEPORT = "[@]";
    public static final String RECALL = "[R]";

    // Monster Icons
    public static final String DRAGON = "[D]";
    public static final String SPIRIT = "[S]";
    public static final String EXOSKELETON = "[E]";

    // Hero Icons
    public static final String WARRIOR = "[W]";
    public static final String PALADIN = "[P]";
    public static final String SORCERER = "[S]";

    // Decorative Elements
    public static final String STAR = "*";
    public static final String BULLET = ">";
    public static final String ARROW_RIGHT = "->";
    public static final String ARROW_UP = "^";
    public static final String ARROW_DOWN = "v";
    public static final String CHECKMARK = "[X]";
    public static final String CROSS = "[ ]";

    /**
     * Helper method to repeat a string n times (Java 8 compatible).
     */
    private static String repeat(String str, int count) {
        if (count <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(str.length() * count);
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * Creates a fancy header with the given title
     */
    public static String createHeader(String title) {
        int totalWidth = 60;
        int titleLength = title.length();
        int padding = (totalWidth - titleLength - 2) / 2;

        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(repeat("=", totalWidth)).append("\n");
        sb.append(repeat(" ", padding)).append(title);
        sb.append(repeat(" ", totalWidth - padding - titleLength)).append("\n");
        sb.append(repeat("=", totalWidth)).append("\n");
        return sb.toString();
    }

    /**
     * Creates a fancy box around text
     */
    public static String createBox(String content) {
        String[] lines = content.split("\n");
        int maxLength = 0;
        for (String line : lines) {
            if (line.length() > maxLength) {
                maxLength = line.length();
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("+").append(repeat("-", maxLength + 2)).append("+\n");
        for (String line : lines) {
            sb.append("| ").append(line);
            sb.append(repeat(" ", maxLength - line.length())).append(" |\n");
        }
        sb.append("+").append(repeat("-", maxLength + 2)).append("+");
        return sb.toString();
    }

    /**
     * Creates a separator line
     */
    public static String createSeparator(int length) {
        return repeat("-", length);
    }

    /**
     * Creates a thick separator line
     */
    public static String createThickSeparator(int length) {
        return repeat("=", length);
    }

    /**
     * Creates a battle banner
     */
    public static String createBattleBanner() {
        StringBuilder sb = new StringBuilder();
        sb.append("+").append(repeat("=", 58)).append("+\n");
        sb.append("|").append(repeat(" ", 58)).append("|\n");
        sb.append("|              [>] BATTLE COMMENCED [>]                  |\n");
        sb.append("|").append(repeat(" ", 58)).append("|\n");
        sb.append("+").append(repeat("=", 58)).append("+\n");
        return sb.toString();
    }

    /**
     * Creates a victory banner
     */
    public static String createVictoryBanner() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("+").append(repeat("=", 58)).append("+\n");
        sb.append("|").append(repeat(" ", 58)).append("|\n");
        sb.append("|            [V]  VICTORY! YOU TRIUMPHED!  [V]           |\n");
        sb.append("|").append(repeat(" ", 58)).append("|\n");
        sb.append("+").append(repeat("=", 58)).append("+\n");
        sb.append("\n");
        return sb.toString();
    }

    /**
     * Creates a defeat banner
     */
    public static String createDefeatBanner() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("+").append(repeat("=", 58)).append("+\n");
        sb.append("|").append(repeat(" ", 58)).append("|\n");
        sb.append("|               [X]  DEFEAT - HEROES FALLEN  [X]         |\n");
        sb.append("|").append(repeat(" ", 58)).append("|\n");
        sb.append("+").append(repeat("=", 58)).append("+\n");
        sb.append("\n");
        return sb.toString();
    }

    /**
     * Creates a level up banner
     */
    public static String createLevelUpBanner(String heroName, int newLevel) {
        String message = heroName + " LEVELED UP TO " + newLevel + "!";
        int totalWidth = 58;
        int padding = (totalWidth - message.length() - 8) / 2;

        StringBuilder sb = new StringBuilder();
        sb.append("\n+").append(repeat("=", 58)).append("+\n");
        sb.append("|").append(repeat(" ", 58)).append("|\n");
        sb.append("| ").append(repeat(" ", padding)).append("[^] ").append(message).append(" [^]");
        sb.append(repeat(" ", totalWidth - padding - message.length() - 8)).append(" |\n");
        sb.append("|").append(repeat(" ", 58)).append("|\n");
        sb.append("+").append(repeat("=", 58)).append("+\n");
        return sb.toString();
    }

    /**
     * Creates a market banner
     */
    public static String createMarketBanner() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("+").append(repeat("=", 58)).append("+\n");
        sb.append("|").append(repeat(" ", 58)).append("|\n");
        sb.append("|              [M]  WELCOME TO THE MARKET  [M]             |\n");
        sb.append("|").append(repeat(" ", 58)).append("|\n");
        sb.append("+").append(repeat("=", 58)).append("+\n");
        sb.append("\n");
        return sb.toString();
    }

    /**
     * Creates ASCII art for HP bar
     */
    public static String createHpBar(int current, int max, int width) {
        if (max <= 0) {
            return "[" + repeat(" ", width) + "]";
        }

        int filled = (int) ((double) current / max * width);
        if (filled < 0) {
            filled = 0;
        }
        if (filled > width) {
            filled = width;
        }

        return "[" + repeat("=", filled) + repeat(" ", width - filled) + "]";
    }

    /**
     * Creates ASCII art for mana bar
     */
    public static String createManaBar(int current, int max, int width) {
        if (max <= 0) {
            return "[" + repeat(" ", width) + "]";
        }

        int filled = (int) ((double) current / max * width);
        if (filled < 0) {
            filled = 0;
        }
        if (filled > width) {
            filled = width;
        }

        return "[" + repeat("~", filled) + repeat(" ", width - filled) + "]";
    }

    /**
     * Creates a simple divider
     */
    public static String createDivider() {
        return repeat("-", 60);
    }

    /**
     * Creates a double divider
     */
    public static String createDoubleDivider() {
        return repeat("=", 60);
    }

    /**
     * Creates a title card
     */
    public static String createTitleCard(String title) {
        int width = 60;
        int padding = (width - title.length() - 4) / 2;

        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(repeat("*", width)).append("\n");
        sb.append("*").append(repeat(" ", width - 2)).append("*\n");
        sb.append("*").append(repeat(" ", padding));
        sb.append("  ").append(title).append("  ");
        sb.append(repeat(" ", width - padding - title.length() - 4)).append("*\n");
        sb.append("*").append(repeat(" ", width - 2)).append("*\n");
        sb.append(repeat("*", width)).append("\n");
        return sb.toString();
    }

    /**
     * Creates the Legends of Valor title ASCII art
     */
    public static String getLegendsOfValorTitle() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(
                ".____                                    .___               _____                .__                \n");
        sb.append(
                "|    |    ____   ____   ____   ____    __| _/______   _____/ ____\\ ___  _______  |  |   ___________ \n");
        sb.append(
                "|    |  _/ __ \\ / ___\\_/ __ \\ /    \\  / __ |/  ___/  /  _ \\   __\\  \\  \\/\\__  \\ |  |  /  _ \\_  __ \\\n");
        sb.append(
                "|    |__\\  ___// /_/  >  ___/|   |  \\/ /_/ |\\___ \\  (  <_> )  |     \\   /  / __ \\|  |_(  <_> )  | \\/\n");
        sb.append(
                "|_______ \\___  >___  / \\___  >___|  /\\____ /____  >  \\____/|__|      \\_/  (____  /____/\\____/|__|   \n");
        sb.append(
                "        \\/   \\/_____/      \\/     \\/      \\/    \\/                             \\/                   \n");
        sb.append("\n");
        return sb.toString();
    }

    /**
     * Creates the Legends: Monsters and Heroes title ASCII art
     */
    public static String getMonstersAndHeroesTitle() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(".____                                    .___          \n");
        sb.append("|    |    ____   ____   ____   ____    __| _/______  \n");
        sb.append("|    |  _/ __ \\ / ___\\_/ __ \\ /    \\  / __ |/  ___/  \n");
        sb.append("|    |__\\  ___// /_/  >  ___/|   |  \\/ /_/ |\\___ \\   \n");
        sb.append("|_______ \\___  >___  / \\___  >___|  /\\____ /____  >  \n");
        sb.append("        \\/   \\/_____/      \\/     \\/      \\/    \\/   \n");
        sb.append("\n");
        sb.append("   _____                       __                        \n");
        sb.append("  /     \\   ____   ____   _______/  |_  ___________  ______\n");
        sb.append(" /  \\ /  \\ /  _ \\ /    \\ /  ___/\\   __\\/ __ \\_  __ \\/  ___/\n");
        sb.append("/    Y    (  <_> )   |  \\\\___ \\  |  | \\  ___/|  | \\/\\___ \\ \n");
        sb.append("\\____|__  /\\____/|___|  /____  > |__|  \\___  >__|  /____  >\n");
        sb.append("        \\/            \\/     \\/            \\/           \\/ \n");
        sb.append("\n");
        sb.append("               ___________                                \n");
        sb.append("               \\_   _____/                                \n");
        sb.append("                |    __)_                                 \n");
        sb.append("                |        \\                                \n");
        sb.append("               /_______  /                                \n");
        sb.append("                       \\/                                 \n");
        sb.append("\n");
        sb.append("  ___ ___                                  \n");
        sb.append(" /   |   \\   ___________  ____   ____   ______\n");
        sb.append("/    ~    \\_/ __ \\_  __ \\/  _ \\_/ __ \\ /  ___/\n");
        sb.append("\\    Y    /\\  ___/|  | \\(  <_> )  ___/ \\___ \\ \n");
        sb.append(" \\___|_  /  \\___  >__|   \\____/ \\___  >____  >\n");
        sb.append("       \\/       \\/                   \\/     \\/ \n");
        sb.append("\n");
        return sb.toString();
    }
}