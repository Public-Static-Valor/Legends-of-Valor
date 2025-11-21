package com.legends.game;

public class CommonTile extends Tile {
    public CommonTile(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return "Common";
    }

    @Override
    public boolean isAccessible() {
        return true;
    }

    @Override
    public String getSymbol() {
        return "C";
    }
}
