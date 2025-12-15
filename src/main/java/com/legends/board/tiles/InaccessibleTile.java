package com.legends.board.tiles;

/**
 * Represents an inaccessible tile on the game board.
 * Heroes cannot move onto inaccessible tiles.
 */
public class InaccessibleTile extends Tile {
    /**
     * Constructs a new InaccessibleTile at the specified coordinates.
     *
     * @param x The x-coordinate of the tile.
     * @param y The y-coordinate of the tile.
     */
    public InaccessibleTile(int x, int y) {
        super(x, y);
    }

    /**
     * Gets the type of the tile.
     *
     * @return The type of the tile ("Inaccessible").
     */
    @Override
    public String getType() {
        return "Inaccessible";
    }

    /**
     * Checks if the tile is accessible.
     *
     * @return False, as inaccessible tiles cannot be entered.
     */
    @Override
    public boolean isAccessible() {
        return false;
    }

    /**
     * Gets the symbol representing the tile.
     *
     * @return The symbol "X".
     */
    @Override
    public String getSymbol() {
        return "X";
    }
}
