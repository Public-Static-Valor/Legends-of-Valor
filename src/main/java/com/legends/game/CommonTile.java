package com.legends.game;

/**
 * Represents a common tile on the game board.
 * Common tiles are accessible and may trigger battles.
 */
public class CommonTile extends Tile {
    /**
     * Constructs a new CommonTile at the specified coordinates.
     *
     * @param x The x-coordinate of the tile.
     * @param y The y-coordinate of the tile.
     */
    public CommonTile(int x, int y) {
        super(x, y);
    }

    /**
     * Gets the type of the tile.
     *
     * @return The type of the tile ("Common").
     */
    @Override
    public String getType() {
        return "Common";
    }

    /**
     * Checks if the tile is accessible.
     *
     * @return True, as common tiles are accessible.
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
}
