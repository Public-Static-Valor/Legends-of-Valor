package com.legends.gameFiles;

import com.legends.model.Hero;

/**
 * Represents a Koulou tile in Legends of Valor.
 * Koulou tiles increase the strength of heroes standing on them.
 */
public class KoulouTile extends Tile {
    private static final double STRENGTH_BONUS = 0.10; // 10% bonus

    /**
     * Constructs a new KoulouTile at the specified coordinates.
     *
     * @param x The x-coordinate of the tile.
     * @param y The y-coordinate of the tile.
     */
    public KoulouTile(int x, int y) {
        super(x, y);
    }

    /**
     * Gets the type of the tile.
     *
     * @return The type of the tile ("Koulou").
     */
    @Override
    public String getType() {
        return "Koulou";
    }

    /**
     * Checks if the tile is accessible.
     *
     * @return True, as koulou tiles are accessible.
     */
    @Override
    public boolean isAccessible() {
        return true;
    }

    /**
     * Gets the symbol representing the tile.
     *
     * @return The symbol "K".
     */
    @Override
    public String getSymbol() {
        return "K";
    }

    /**
     * Applies the strength bonus to a hero.
     *
     * @param hero The hero to apply the bonus to.
     * @return The bonus amount applied.
     */
    public int applyBonus(Hero hero) {
        int bonus = (int) (hero.getStrength() * STRENGTH_BONUS);
        hero.setStrength(hero.getStrength() + bonus);
        return bonus;
    }

    /**
     * Removes the strength bonus from a hero.
     *
     * @param hero The hero to remove the bonus from.
     * @return The bonus amount removed.
     */
    public int removeBonus(Hero hero) {
        int bonus = (int) (hero.getStrength() / (1 + STRENGTH_BONUS) * STRENGTH_BONUS);
        hero.setStrength(hero.getStrength() - bonus);
        return bonus;
    }
}
