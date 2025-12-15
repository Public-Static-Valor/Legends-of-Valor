package com.legends.board.tiles;

import com.legends.model.Hero;

/**
 * Represents a Cave tile in Legends of Valor.
 * Cave tiles increase the agility of heroes standing on them.
 */
public class CaveTile extends Tile {
    private static final double AGILITY_BONUS = 0.10; // 10% bonus

    /**
     * Constructs a new CaveTile at the specified coordinates.
     *
     * @param x The x-coordinate of the tile.
     * @param y The y-coordinate of the tile.
     */
    public CaveTile(int x, int y) {
        super(x, y);
    }

    /**
     * Gets the type of the tile.
     *
     * @return The type of the tile ("Cave").
     */
    @Override
    public String getType() {
        return "Cave";
    }

    /**
     * Checks if the tile is accessible.
     *
     * @return True, as cave tiles are accessible.
     */
    @Override
    public boolean isAccessible() {
        return true;
    }

    /**
     * Gets the symbol representing the tile.
     *
     * @return The symbol "C".
     */
    @Override
    public String getSymbol() {
        return "C";
    }

    /**
     * Applies the agility bonus to a hero.
     *
     * @param hero The hero to apply the bonus to.
     * @return The bonus amount applied.
     */
    public int applyBonus(Hero hero) {
        int bonus = (int) (hero.getAgility() * AGILITY_BONUS);
        hero.setAgility(hero.getAgility() + bonus);
        return bonus;
    }

    /**
     * Removes the agility bonus from a hero.
     *
     * @param hero The hero to remove the bonus from.
     * @return The bonus amount removed.
     */
    public int removeBonus(Hero hero) {
        int bonus = (int) (hero.getAgility() / (1 + AGILITY_BONUS) * AGILITY_BONUS);
        hero.setAgility(hero.getAgility() - bonus);
        return bonus;
    }
}
