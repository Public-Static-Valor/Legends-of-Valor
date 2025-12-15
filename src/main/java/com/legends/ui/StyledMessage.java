package com.legends.ui;

/**
 * Builder class for creating styled messages with icons and colors.
 * Uses the Builder pattern for easy construction of complex messages.
 */
public class StyledMessage {
    private StringBuilder content;

    public StyledMessage() {
        this.content = new StringBuilder();
    }

    /**
     * Adds text with a specific style
     */
    public StyledMessage add(String text, MessageStyle style) {
        content.append(style.format(text));
        return this;
    }

    /**
     * Adds text with a specific style without icon
     */
    public StyledMessage addNoIcon(String text, MessageStyle style) {
        content.append(style.formatNoIcon(text));
        return this;
    }

    /**
     * Adds plain text
     */
    public StyledMessage add(String text) {
        content.append(text);
        return this;
    }

    /**
     * Adds text with a specific color
     */
    public StyledMessage addColored(String text, String color) {
        content.append(color).append(text).append(ConsoleColors.RESET);
        return this;
    }

    /**
     * Adds a newline
     */
    public StyledMessage newLine() {
        content.append("\n");
        return this;
    }

    /**
     * Adds multiple newlines
     */
    public StyledMessage newLines(int count) {
        for (int i = 0; i < count; i++) {
            content.append("\n");
        }
        return this;
    }

    /**
     * Adds a separator
     */
    public StyledMessage separator(int length) {
        content.append(AsciiArt.createSeparator(length)).append("\n");
        return this;
    }

    /**
     * Adds a thick separator
     */
    public StyledMessage thickSeparator(int length) {
        content.append(AsciiArt.createThickSeparator(length)).append("\n");
        return this;
    }

    /**
     * Adds an icon
     */
    public StyledMessage icon(String icon) {
        content.append(icon).append(" ");
        return this;
    }

    /**
     * Adds bold text
     */
    public StyledMessage bold(String text) {
        content.append(ConsoleColors.bold(text));
        return this;
    }

    /**
     * Adds a header
     */
    public StyledMessage header(String title) {
        content.append(AsciiArt.createHeader(title));
        return this;
    }

    /**
     * Adds a box around text
     */
    public StyledMessage box(String text) {
        content.append(AsciiArt.createBox(text));
        return this;
    }

    /**
     * Builds and returns the final message
     */
    public String build() {
        return content.toString();
    }

    /**
     * Returns the message as a string
     */
    @Override
    public String toString() {
        return build();
    }

    // Static factory methods for common message types

    public static String attack(String attackerName, String targetName, int damage) {
        return new StyledMessage()
                .add(attackerName, MessageStyle.HERO)
                .add(" attacked ")
                .add(targetName, MessageStyle.MONSTER)
                .add(" for ")
                .addColored(String.valueOf(damage), ConsoleColors.BRIGHT_RED)
                .add(" damage!")
                .build();
    }

    public static String dodge(String dodgerName) {
        return new StyledMessage()
                .add(dodgerName + " dodged the attack!", MessageStyle.DODGE)
                .build();
    }

    public static String death(String entityName) {
        return new StyledMessage()
                .add(entityName + " has been defeated!", MessageStyle.DEATH)
                .build();
    }

    public static String levelUp(String heroName, int newLevel) {
        return new StyledMessage()
                .add(heroName + " leveled up to Level " + newLevel + "!", MessageStyle.LEVEL_UP)
                .build();
    }

    public static String potionUse(String heroName, String potionName) {
        return new StyledMessage()
                .add(heroName, MessageStyle.HERO)
                .add(" used ")
                .add(potionName, MessageStyle.POTION_USE)
                .add("!")
                .build();
    }

    public static String spellCast(String casterName, String spellName, String targetName, int damage) {
        return new StyledMessage()
                .add(casterName, MessageStyle.HERO)
                .add(" cast ")
                .add(spellName, MessageStyle.SPELL_CAST)
                .add(" on ")
                .add(targetName, MessageStyle.MONSTER)
                .add(" for ")
                .addColored(String.valueOf(damage), ConsoleColors.BRIGHT_MAGENTA)
                .add(" damage!")
                .build();
    }

    public static String buy(String heroName, String itemName, int cost) {
        return new StyledMessage()
                .add(heroName, MessageStyle.HERO)
                .add(" purchased ")
                .add(itemName, MessageStyle.SUCCESS)
                .add(" for ")
                .add(cost + " gold", MessageStyle.BUY)
                .add("!")
                .build();
    }

    public static String sell(String heroName, String itemName, int price) {
        return new StyledMessage()
                .add(heroName, MessageStyle.HERO)
                .add(" sold ")
                .add(itemName, MessageStyle.WARNING)
                .add(" for ")
                .add(price + " gold", MessageStyle.SELL)
                .add("!")
                .build();
    }

    public static String move(String entityName, int x, int y) {
        return new StyledMessage()
                .add(entityName, MessageStyle.HERO)
                .add(" moved to (")
                .addColored(x + "," + y, ConsoleColors.CYAN)
                .add(")")
                .build();
    }

    public static String teleport(String heroName, int lane) {
        return new StyledMessage()
                .add(heroName + " teleported to lane " + lane + "!", MessageStyle.TELEPORT)
                .build();
    }

    public static String recall(String heroName) {
        return new StyledMessage()
                .add(heroName + " recalled to their Nexus!", MessageStyle.RECALL)
                .build();
    }

    public static String error(String message) {
        return new StyledMessage()
                .add(message, MessageStyle.ERROR)
                .build();
    }

    public static String success(String message) {
        return new StyledMessage()
                .add(message, MessageStyle.SUCCESS)
                .build();
    }

    public static String info(String message) {
        return new StyledMessage()
                .add(message, MessageStyle.INFO)
                .build();
    }

    public static String warning(String message) {
        return new StyledMessage()
                .add(message, MessageStyle.WARNING)
                .build();
    }
}
