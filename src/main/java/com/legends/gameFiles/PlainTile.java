package com.legends.gameFiles;

/**
 * Represents a Plain tile in Legends of Valor.
 * Plain tiles have no special attributes.
 */
public class PlainTile extends Tile {
    /**
     * Constructs a new PlainTile at the specified coordinates.
     *
     * @param x The x-coordinate of the tile.
     * @param y The y-coordinate of the tile.
     */
    public PlainTile(int x, int y) {
        super(x, y);
    }

    /**
     * Gets the type of the tile.
     *
     * @return The type of the tile ("Plain").
     */
    @Override
    public String getType() {
        return "Plain";
    }

    /**
     * Checks if the tile is accessible.
     *
     * @return True, as plain tiles are accessible.
     */
    @Override
    public boolean isAccessible() {
        return true;
    }

    /**
     * Gets the symbol representing the tile.
     *
     * @return The symbol "P".
     */
    @Override
    public String getSymbol() {
        return "P";
    }
}
