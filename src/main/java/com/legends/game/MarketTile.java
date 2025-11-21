package com.legends.game;

public class MarketTile extends Tile {
    public MarketTile(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return "Market";
    }

    @Override
    public boolean isAccessible() {
        return true;
    }

    @Override
    public String getSymbol() {
        return "M";
    }
}
