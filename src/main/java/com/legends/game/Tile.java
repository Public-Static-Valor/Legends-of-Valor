package com.legends.game;

import com.legends.model.Entity;

/**
 * Abstract base class for all tiles on the game board.
 * Manages entity placement and coordinates.
 */
public abstract class Tile {
    protected Entity entity;
    protected int x;
    protected int y;

    /**
     * Constructs a new Tile at the specified coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the entity currently on this tile.
     *
     * @return The entity, or null if empty.
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Sets the entity on this tile.
     *
     * @param entity The entity to place.
     */
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    /**
     * Checks if the tile is occupied by an entity.
     *
     * @return True if occupied, false otherwise.
     */
    public boolean isOccupied() {
        return entity != null;
    }

    /**
     * Gets the x-coordinate of the tile.
     *
     * @return The x-coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the tile.
     *
     * @return The y-coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the type of the tile.
     *
     * @return The type string.
     */
    public abstract String getType();

    /**
     * Checks if the tile is accessible.
     *
     * @return True if accessible, false otherwise.
     */
    public abstract boolean isAccessible();

    /**
     * Gets the symbol representing the tile.
     *
     * @return The symbol string.
     */
    public abstract String getSymbol();
}
