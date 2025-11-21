package com.legends.game;

public class InaccessibleTile extends Tile {
    public InaccessibleTile(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return "Inaccessible";
    }

    @Override
    public boolean isAccessible() {
        return false;
    }

    @Override
    public String getSymbol() {
        return "I";
    }
}
