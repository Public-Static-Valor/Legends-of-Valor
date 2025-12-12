package com.legends.gameFiles;

/**
 * Represents a Nexus tile in Legends of Valor.
 * Nexus tiles are where heroes and monsters spawn.
 * The heroes' Nexus also functions as a market.
 */
public class NexusTile extends Tile {
    private boolean isHeroNexus;

    /**
     * Constructs a new NexusTile at the specified coordinates.
     *
     * @param x           The x-coordinate of the tile.
     * @param y           The y-coordinate of the tile.
     * @param isHeroNexus True if this is the heroes' Nexus, false for monsters' Nexus.
     */
    public NexusTile(int x, int y, boolean isHeroNexus) {
        super(x, y);
        this.isHeroNexus = isHeroNexus;
    }

    /**
     * Gets the type of the tile.
     *
     * @return The type of the tile ("Nexus").
     */
    @Override
    public String getType() {
        return "Nexus";
    }

    /**
     * Checks if the tile is accessible.
     *
     * @return True, as Nexus tiles are accessible.
     */
    @Override
    public boolean isAccessible() {
        return true;
    }

    /**
     * Gets the symbol representing the tile.
     *
     * @return The symbol "N".
     */
    @Override
    public String getSymbol() {
        return "N";
    }

    /**
     * Checks if this is the heroes' Nexus.
     *
     * @return True if this is the heroes' Nexus, false otherwise.
     */
    public boolean isHeroNexus() {
        return isHeroNexus;
    }

    /**
     * Checks if this Nexus can function as a market.
     * Only the heroes' Nexus functions as a market.
     *
     * @return True if this is the heroes' Nexus.
     */
    public boolean isMarket() {
        return isHeroNexus;
    }
}
