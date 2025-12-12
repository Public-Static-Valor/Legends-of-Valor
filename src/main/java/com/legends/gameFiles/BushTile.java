package com.legends.gameFiles;

import com.legends.model.Hero;

/**
 * Represents a Bush tile in Legends of Valor.
 * Bush tiles increase the dexterity of heroes standing on them.
 */
public class BushTile extends Tile {
    private static final double DEXTERITY_BONUS = 0.10; // 10% bonus

    /**
     * Constructs a new BushTile at the specified coordinates.
     *
     * @param x The x-coordinate of the tile.
     * @param y The y-coordinate of the tile.
     */
    public BushTile(int x, int y) {
        super(x, y);
    }

    /**
     * Gets the type of the tile.
     *
     * @return The type of the tile ("Bush").
     */
    @Override
    public String getType() {
        return "Bush";
    }

    /**
     * Checks if the tile is accessible.
     *
     * @return True, as bush tiles are accessible.
     */
    @Override
    public boolean isAccessible() {
        return true;
    }

    /**
     * Gets the symbol representing the tile.
     *
     * @return The symbol "B".
     */
    @Override
    public String getSymbol() {
        return "B";
    }

    /**
     * Applies the dexterity bonus to a hero.
     *
     * @param hero The hero to apply the bonus to.
     * @return The bonus amount applied.
     */
    public int applyBonus(Hero hero) {
        int bonus = (int) (hero.getDexterity() * DEXTERITY_BONUS);
        hero.setDexterity(hero.getDexterity() + bonus);
        return bonus;
    }

    /**
     * Removes the dexterity bonus from a hero.
     *
     * @param hero The hero to remove the bonus from.
     * @return The bonus amount removed.
     */
    public int removeBonus(Hero hero) {
        int bonus = (int) (hero.getDexterity() / (1 + DEXTERITY_BONUS) * DEXTERITY_BONUS);
        hero.setDexterity(hero.getDexterity() - bonus);
        return bonus;
    }
}
