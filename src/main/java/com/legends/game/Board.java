package com.legends.game;

import com.legends.model.Entity;
import com.legends.model.Hero;
import com.legends.model.Monster;
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

    public void placeEntity(Entity entity, int x, int y) {
        if (isValidCoordinate(x, y) && grid[y][x].isAccessible()) {
            grid[y][x].setEntity(entity);
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

    public boolean moveEntity(int fromX, int fromY, int toX, int toY) {
        if (!isValidCoordinate(fromX, fromY) || !isValidCoordinate(toX, toY)) {
            return false;
        }
        
        Tile fromTile = grid[fromY][fromX];
        Tile toTile = grid[toY][toX];

        if (!toTile.isAccessible()) {
            System.out.println("Cannot move to inaccessible tile!");
            return false;
        }

        if (toTile.isOccupied()) {
            System.out.println("Tile is already occupied!");
            return false;
        }

        Entity entity = fromTile.getEntity();
        if (entity != null) {
            fromTile.setEntity(null);
            toTile.setEntity(entity);
            return true;
        }
        return false;
    }

    private boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public void printBoard() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Tile tile = grid[y][x];
                if (tile.isOccupied()) {
                    Entity entity = tile.getEntity();
                    if (entity instanceof Hero) {
                        System.out.print("H ");
                    } else if (entity instanceof Monster) {
                        System.out.print("M ");
                    } else {
                        System.out.print("? ");
                    }
                } else {
                    System.out.print(tile.getSymbol() + " ");
                }
            }
            System.out.println();
        }
    }
}
