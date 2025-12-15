package com.legends.ui;

/**
 * Enumeration of different message styles with associated colors and icons.
 * Used to provide consistent styling across the game.
 */
public enum MessageStyle {
    // Combat Messages
    ATTACK(ConsoleColors.BRIGHT_RED, AsciiArt.SWORD_ATTACK),
    DAMAGE(ConsoleColors.RED, AsciiArt.CRITICAL_HIT),
    DODGE(ConsoleColors.BRIGHT_CYAN, AsciiArt.DODGE),
    DEFENSE(ConsoleColors.BLUE, AsciiArt.SHIELD_BLOCK),
    
    // Item Usage
    POTION_USE(ConsoleColors.BRIGHT_MAGENTA, AsciiArt.POTION),
    SPELL_CAST(ConsoleColors.BRIGHT_BLUE, AsciiArt.SPELL_SCROLL),
    EQUIP_WEAPON(ConsoleColors.YELLOW, AsciiArt.WEAPON),
    EQUIP_ARMOR(ConsoleColors.CYAN, AsciiArt.ARMOR),
    
    // Transaction Messages
    BUY(ConsoleColors.BRIGHT_YELLOW, AsciiArt.GOLD),
    SELL(ConsoleColors.YELLOW, AsciiArt.GOLD),
    MARKET(ConsoleColors.BRIGHT_YELLOW, AsciiArt.MARKET),
    
    // Status Messages
    LEVEL_UP(ConsoleColors.BRIGHT_GREEN, AsciiArt.LEVEL_UP),
    DEATH(ConsoleColors.BRIGHT_RED, AsciiArt.DEATH),
    VICTORY(ConsoleColors.BRIGHT_BLUE, AsciiArt.VICTORY),
    DEFEAT(ConsoleColors.DARK_RED, AsciiArt.DEFEAT),
    
    // Movement and Location
    MOVE(ConsoleColors.CYAN, AsciiArt.ARROW_RIGHT),
    TELEPORT(ConsoleColors.BRIGHT_MAGENTA, AsciiArt.TELEPORT),
    RECALL(ConsoleColors.BRIGHT_CYAN, AsciiArt.RECALL),
    NEXUS(ConsoleColors.YELLOW, AsciiArt.NEXUS),
    
    // General Messages
    SUCCESS(ConsoleColors.BRIGHT_GREEN, AsciiArt.CHECKMARK),
    ERROR(ConsoleColors.BRIGHT_RED, AsciiArt.CROSS),
    INFO(ConsoleColors.BRIGHT_BLUE, AsciiArt.BULLET),
    WARNING(ConsoleColors.BRIGHT_YELLOW, AsciiArt.BULLET),
    
    // Hero/Monster specific
    HERO(ConsoleColors.BRIGHT_GREEN, AsciiArt.HEART),
    MONSTER(ConsoleColors.BRIGHT_RED, ""),
    
    // Default
    DEFAULT(ConsoleColors.RESET, "");
    
    private final String color;
    private final String icon;
    
    MessageStyle(String color, String icon) {
        this.color = color;
        this.icon = icon;
    }
    
    public String getColor() {
        return color;
    }
    
    public String getIcon() {
        return icon;
    }
    
    /**
     * Formats a message with this style
     */
    public String format(String message) {
        return color + icon + message + ConsoleColors.RESET;
    }
    
    /**
     * Formats a message with this style without icon
     */
    public String formatNoIcon(String message) {
        return color + message + ConsoleColors.RESET;
    }
}
