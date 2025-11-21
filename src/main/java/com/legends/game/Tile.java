package com.legends.game;

import com.legends.model.Entity;

public abstract class Tile {
    protected Entity entity;
    protected int x;
    protected int y;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public boolean isOccupied() {
        return entity != null;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public abstract String getType();
    public abstract boolean isAccessible();
    public abstract String getSymbol();
}
