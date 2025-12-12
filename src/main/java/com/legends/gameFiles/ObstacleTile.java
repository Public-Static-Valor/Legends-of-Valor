package com.legends.gameFiles;

/**
 * Represents an Obstacle tile in Legends of Valor.
 * Obstacles block movement but can be destroyed by heroes.
 */
public class ObstacleTile extends Tile {
    /**
     * Constructs a new ObstacleTile at the specified coordinates.
     *
     * @param x The x-coordinate of the tile.
     * @param y The y-coordinate of the tile.
     */
    public ObstacleTile(int x, int y) {
        super(x, y);
    }

    /**
     * Gets the type of the tile.
     *
     * @return The type of the tile ("Obstacle").
     */
    @Override
    public String getType() {
        return "Obstacle";
    }

    /**
     * Checks if the tile is accessible.
     *
     * @return False, as obstacle tiles block movement.
     */
    @Override
    public boolean isAccessible() {
        return false;
    }

    /**
     * Gets the symbol representing the tile.
     *
     * @return The symbol "O".
     */
    @Override
    public String getSymbol() {
        return "O";
    }
}
