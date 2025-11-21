package com.legends.game;

import com.legends.model.Entity;
import com.legends.model.Hero;
import com.legends.model.Monster;
import com.legends.io.Output;
import java.util.Random;

public class Board {
    private int width;
    private int height;
    private Tile[][] grid;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Tile[height][width];
        initializeBoard();
    }

    private void initializeBoard() {
        Random rand = new Random();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int roll = rand.nextInt(100);
                if (roll < 20) { // 20% Inaccessible
                    grid[y][x] = new InaccessibleTile(x, y);
                } else if (roll < 50) { // 30% Market
                    grid[y][x] = new MarketTile(x, y);
                } else { // 50% Common
                    grid[y][x] = new CommonTile(x, y);
                }
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void placeEntity(Entity entity, int x, int y) {
        if (isValidCoordinate(x, y) && grid[y][x].isAccessible()) {
            grid[y][x].setEntity(entity);
            entity.setX(x);
            entity.setY(y);
        }
    }

    public Entity getEntityAt(int x, int y) {
        if (isValidCoordinate(x, y)) {
            return grid[y][x].getEntity();
        }
        return null;
    }

    public Tile getTileAt(int x, int y) {
        if (isValidCoordinate(x, y)) {
            return grid[y][x];
        }
        return null;
    }

    public boolean moveEntity(int fromX, int fromY, int toX, int toY, Output output) {
        if (!isValidCoordinate(fromX, fromY) || !isValidCoordinate(toX, toY)) {
            return false;
        }
        
        Tile fromTile = grid[fromY][fromX];
        Tile toTile = grid[toY][toX];

        if (!toTile.isAccessible()) {
            if (output != null) output.println("Cannot move to inaccessible tile!");
            return false;
        }

        if (toTile.isOccupied()) {
            if (output != null) output.println("Tile is already occupied!");
            return false;
        }

        Entity entity = fromTile.getEntity();
        if (entity != null) {
            fromTile.setEntity(null);
            toTile.setEntity(entity);
            entity.setX(toX);
            entity.setY(toY);
            return true;
        }
        return false;
    }

    private boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public void printBoard(Output output) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Tile tile = grid[y][x];
                if (tile.isOccupied()) {
                    Entity entity = tile.getEntity();
                    if (entity instanceof Hero) {
                        output.print("H ");
                    } else if (entity instanceof Monster) {
                        output.print("M ");
                    } else {
                        output.print("? ");
                    }
                } else {
                    output.print(tile.getSymbol() + " ");
                }
            }
            output.println();
        }
    }
}
