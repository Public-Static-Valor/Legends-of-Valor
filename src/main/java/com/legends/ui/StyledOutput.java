package com.legends.ui;

import com.legends.io.Output;

/**
 * Decorator for Output that adds styled printing capabilities.
 * Implements the Decorator pattern to extend functionality without modifying
 * existing code.
 */
public class StyledOutput implements Output {
    private final Output output;

    public StyledOutput(Output output) {
        this.output = output;
    }

    // Delegate basic methods
    @Override
    public void print(Object s) {
        output.print(s);
    }

    @Override
    public void println(Object s) {
        output.println(s);
    }

    @Override
    public void println() {
        output.println();
    }

    @Override
    public void printError(Object s) {
        output.printError(s);
    }

    @Override
    public void printlnGreen(Object s) {
        output.printlnGreen(s);
    }

    @Override
    public void printlnRed(Object s) {
        output.printlnRed(s);
    }

    // Styled print methods

    public void printStyled(StyledMessage message) {
        output.println(message.build());
    }

    public void printStyled(String message, MessageStyle style) {
        output.println(style.format(message));
    }

    // Combat messages
    public void printAttack(String attackerName, String targetName, int damage) {
        output.println(StyledMessage.attack(attackerName, targetName, damage));
    }

    public void printDodge(String dodgerName) {
        output.println(StyledMessage.dodge(dodgerName));
    }

    public void printDeath(String entityName) {
        output.println(StyledMessage.death(entityName));
    }

    public void printDamage(String targetName, int damage) {
        StyledMessage msg = new StyledMessage()
                .add(targetName, MessageStyle.MONSTER)
                .add(" took ")
                .addColored(String.valueOf(damage), ConsoleColors.BRIGHT_RED)
                .add(" damage!");
        output.println(msg.build());
    }

    // Item usage messages
    public void printPotionUse(String heroName, String potionName) {
        output.println(StyledMessage.potionUse(heroName, potionName));
    }

    public void printSpellCast(String casterName, String spellName, String targetName, int damage) {
        output.println(StyledMessage.spellCast(casterName, spellName, targetName, damage));
    }

    public void printEquipWeapon(String heroName, String weaponName) {
        StyledMessage msg = new StyledMessage()
                .add(heroName, MessageStyle.HERO)
                .add(" equipped ")
                .add(weaponName, MessageStyle.EQUIP_WEAPON)
                .add("!");
        output.println(msg.build());
    }

    public void printEquipArmor(String heroName, String armorName) {
        StyledMessage msg = new StyledMessage()
                .add(heroName, MessageStyle.HERO)
                .add(" equipped ")
                .add(armorName, MessageStyle.EQUIP_ARMOR)
                .add("!");
        output.println(msg.build());
    }

    // Transaction messages
    public void printBuy(String heroName, String itemName, int cost) {
        output.println(StyledMessage.buy(heroName, itemName, cost));
    }

    public void printSell(String heroName, String itemName, int price) {
        output.println(StyledMessage.sell(heroName, itemName, price));
    }

    // Movement messages
    public void printMove(String entityName, int x, int y) {
        output.println(StyledMessage.move(entityName, x, y));
    }

    public void printTeleport(String heroName, int lane) {
        output.println(StyledMessage.teleport(heroName, lane));
    }

    public void printRecall(String heroName) {
        output.println(StyledMessage.recall(heroName));
    }

    // Status messages
    public void printLevelUp(String heroName, int newLevel) {
        output.println(AsciiArt.createLevelUpBanner(heroName, newLevel));
    }

    public void printVictory() {
        output.println(AsciiArt.createVictoryBanner());
    }

    public void printDefeat() {
        output.println(AsciiArt.createDefeatBanner());
    }

    public void printMarketBanner() {
        output.println(AsciiArt.createMarketBanner());
    }

    public void printBattleBanner() {
        output.println(AsciiArt.createBattleBanner());
    }

    // General purpose messages
    public void printSuccess(String message) {
        output.println(StyledMessage.success(message));
    }

    public void printError(String message) {
        output.printError(StyledMessage.error(message));
    }

    public void printInfo(String message) {
        output.println(StyledMessage.info(message));
    }

    public void printWarning(String message) {
        output.println(StyledMessage.warning(message));
    }

    // Headers and separators
    public void printHeader(String title) {
        output.println(AsciiArt.createHeader(title));
    }

    public void printSeparator(int length) {
        output.println(AsciiArt.createSeparator(length));
    }

    public void printThickSeparator(int length) {
        output.println(AsciiArt.createThickSeparator(length));
    }

    public void printBox(String content) {
        output.println(AsciiArt.createBox(content));
    }

    // Hero/Monster status displays
    public void printHeroStatus(String name, int hp, int maxHp, int mana, int maxMana, int gold) {
        StyledMessage msg = new StyledMessage()
                .add(name, MessageStyle.HERO)
                .add(" | HP: ")
                .addColored(createBar(hp, maxHp, 10, "<3 "), ConsoleColors.RED)
                .add(" ")
                .addColored(hp + "/" + maxHp, ConsoleColors.RED)
                .add(" | MP: ")
                .addColored(createBar(mana, maxMana, 10, "<3 "), ConsoleColors.BLUE)
                .add(" ")
                .addColored(mana + "/" + maxMana, ConsoleColors.BLUE)
                .add(" | ")
                .add("Gold: " + gold, MessageStyle.BUY);
        output.println(msg.build());
    }

    public void printMonsterStatus(String name, int hp, int maxHp, int damage, int defense) {
        StyledMessage msg = new StyledMessage()
                .add(name, MessageStyle.MONSTER)
                .add(" | HP: ")
                .addColored(createBar(hp, maxHp, 10, "<3 "), ConsoleColors.RED)
                .add(" ")
                .addColored(hp + "/" + maxHp, ConsoleColors.RED)
                .add(" | ATK: ")
                .addColored(String.valueOf(damage), ConsoleColors.BRIGHT_RED)
                .add(" | DEF: ")
                .addColored(String.valueOf(defense), ConsoleColors.CYAN);
        output.println(msg.build());
    }

    /**
     * Helper method to repeat a string n times (Java 8 compatible).
     */
    private static String repeat(String str, int count) {
        if (count <= 0)
            return "";
        StringBuilder sb = new StringBuilder(str.length() * count);
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    // Helper method to create progress bars
    private String createBar(int current, int max, int length, String fillChar) {
        if (max == 0)
            return repeat(" ", length);

        int filled = (int) ((double) current / max * length);
        if (filled > length)
            filled = length;
        if (filled < 0)
            filled = 0;

        return repeat(fillChar, filled) + repeat(" ", length - filled);
    }

    // Round announcements
    public void printRoundStart(int roundNumber) {
        String roundText = String.format("======== ROUND %d ========", roundNumber);
        output.println("\n" + ConsoleColors.BRIGHT_YELLOW + roundText + ConsoleColors.RESET);
    }

    public void printHeroesTurn() {
        output.println(ConsoleColors.BRIGHT_GREEN + "\n─── HEROES' TURN ───" + ConsoleColors.RESET);
    }

    public void printMonstersTurn() {
        output.println(ConsoleColors.BRIGHT_RED + "\n─── MONSTERS' TURN ───" + ConsoleColors.RESET);
    }

    // Get the underlying output for cases where direct access is needed
    public Output getOutput() {
        return output;
    }
}
