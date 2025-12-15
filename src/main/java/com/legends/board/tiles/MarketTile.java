package com.legends.board.tiles;

import com.legends.market.Market;

/**
 * Represents a market tile on the game board.
 * Market tiles are accessible and allow heroes to buy and sell items.
 */
public class MarketTile extends Tile {
    private Market market;

    /**
     * Constructs a new MarketTile at the specified coordinates.
     *
     * @param x The x-coordinate of the tile.
     * @param y The y-coordinate of the tile.
     */
    public MarketTile(int x, int y) {
        super(x, y);
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public Market getMarket() {
        return market;
    }

    /**
     * Gets the type of the tile.
     *
     * @return The type of the tile ("Market").
     */
    @Override
    public String getType() {
        return "Market";
    }

    /**
     * Checks if the tile is accessible.
     *
     * @return True, as market tiles are accessible.
     */
    @Override
    public boolean isAccessible() {
        return true;
    }

    /**
     * Gets the symbol representing the tile.
     *
     * @return The symbol "M".
     */
    @Override
    public String getSymbol() {
        return "M";
    }
}
