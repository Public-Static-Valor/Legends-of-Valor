package com.legends.gameFiles;

import com.legends.model.Entity;
import com.legends.model.Hero;
import com.legends.model.Monster;
import com.legends.io.Output;
import java.io.Serializable;
import java.util.Random;
import java.util.Queue;
import java.util.LinkedList;

/**
 * Represents the game board.
 * Manages the grid of tiles and entity placement.
 */
public class Board implements Serializable {
    private static final long serialVersionUID = 1L;
    private int width;
    private int height;
    private Tile[][] grid;

    /**
     * Constructs a new Board with the specified dimensions.
     * Initializes the board with a random layout of tiles.
     *
     * @param width  The width of the board.
     * @param height The height of the board.
     */
    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Tile[height][width];
        initializeBoard();
    }

    /**
     * Initializes the board by generating random layouts until a connected board is
     * formed.
     */
    private void initializeBoard() {
        do {
            generateRandomBoard();
        } while (!isConnected());
    }

    /**
     * Generates a random board layout with Inaccessible, Market, and Common tiles.
     */
    private void generateRandomBoard() {
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

    /**
     * Checks if all accessible tiles on the board are connected.
     *
     * @return True if the board is connected, false otherwise.
     */
    private boolean isConnected() {
        int totalAccessible = 0;
        int startX = -1;
        int startY = -1;

        // Count accessible tiles and find a starting point
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (grid[y][x].isAccessible()) {
                    totalAccessible++;
                    if (startX == -1) {
                        startX = x;
                        startY = y;
                    }
                }
            }
        }

        if (totalAccessible == 0)
            return false;

        // BFS to find all reachable tiles
        boolean[][] visited = new boolean[height][width];
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[] { startX, startY });
        visited[startY][startX] = true;
        int reachableCount = 0;

        int[] dx = { 0, 0, 1, -1 };
        int[] dy = { 1, -1, 0, 0 };

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            reachableCount++;

            for (int i = 0; i < 4; i++) {
                int nx = curr[0] + dx[i];
                int ny = curr[1] + dy[i];

                if (isValidCoordinate(nx, ny) && !visited[ny][nx] && grid[ny][nx].isAccessible()) {
                    visited[ny][nx] = true;
                    queue.add(new int[] { nx, ny });
                }
            }
        }

        return reachableCount == totalAccessible;
    }

    /**
     * Gets the width of the board.
     *
     * @return The width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the board.
     *
     * @return The height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Places an entity on the board at the specified coordinates.
     *
     * @param entity The entity to place.
     * @param x      The x-coordinate.
     * @param y      The y-coordinate.
     */
    public void placeEntity(Entity entity, int x, int y) {
        if (isValidCoordinate(x, y) && grid[y][x].isAccessible()) {
            grid[y][x].setEntity(entity);
            entity.setX(x);
            entity.setY(y);
        }
    }

    /**
     * Gets the entity at the specified coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return The entity at the coordinates, or null if none.
     */
    public Entity getEntityAt(int x, int y) {
        if (isValidCoordinate(x, y)) {
            return grid[y][x].getEntity();
        }
        return null;
    }

    /**
     * Gets the tile at the specified coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return The tile at the coordinates, or null if invalid.
     */
    public Tile getTileAt(int x, int y) {
        if (isValidCoordinate(x, y)) {
            return grid[y][x];
        }
        return null;
    }

    /**
     * Moves an entity from one position to another.
     *
     * @param fromX  The starting x-coordinate.
     * @param fromY  The starting y-coordinate.
     * @param toX    The destination x-coordinate.
     * @param toY    The destination y-coordinate.
     * @param output The output interface for messages.
     * @return True if the move was successful, false otherwise.
     */
    public boolean moveEntity(int fromX, int fromY, int toX, int toY, Output output) {
        if (!isValidCoordinate(fromX, fromY) || !isValidCoordinate(toX, toY)) {
            return false;
        }

        Tile fromTile = grid[fromY][fromX];
        Tile toTile = grid[toY][toX];

        if (!toTile.isAccessible()) {
            if (output != null)
                output.println("Cannot move to inaccessible tile!");
            return false;
        }

        if (toTile.isOccupied()) {
            if (output != null)
                output.println("Tile is already occupied!");
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

    /**
     * Checks if the coordinates are within the board boundaries.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return True if valid, false otherwise.
     */
    private boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";

    /**
     * Prints the current state of the board to the output.
     *
     * @param output The output interface.
     */
    public void printBoard(Output output) {
        // Print top border
        output.print("   ");
        for (int x = 0; x < width; x++) {
            output.print("+---");
        }
        output.println("+");

        for (int y = 0; y < height; y++) {
            output.print(String.format("%2d ", y)); // Row number
            for (int x = 0; x < width; x++) {
                output.print("| ");
                Tile tile = grid[y][x];
                if (tile.isOccupied()) {
                    Entity entity = tile.getEntity();
                    if (entity instanceof Hero) {
                        output.print(ANSI_GREEN + "H" + ANSI_RESET + " ");
                    } else if (entity instanceof Monster) {
                        output.print(ANSI_RED + "M" + ANSI_RESET + " ");
                    } else {
                        output.print("? ");
                    }
                } else {
                    if (tile instanceof MarketTile) {
                        output.print(ANSI_YELLOW + "M" + ANSI_RESET + " ");
                    } else if (tile instanceof InaccessibleTile) {
                        output.print(ANSI_BLUE + "X" + ANSI_RESET + " ");
                    } else {
                        output.print("  "); // Empty space for common tiles looks cleaner
                    }
                }
            }
            output.println("|");

            // Print row separator
            output.print("   ");
            for (int x = 0; x < width; x++) {
                output.print("+---");
            }
            output.println("+");
        }

        output.println("\nMap Legend:");
        output.println(ANSI_GREEN + "H" + ANSI_RESET + " : Hero      " +
                ANSI_YELLOW + "M" + ANSI_RESET + " : Market");
        output.println(ANSI_BLUE + "X" + ANSI_RESET + " : Inaccessible  " +
                "  : Common Land");
    }
}
