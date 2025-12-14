package com.legends.gameFiles;

// import com.legends.model.Entity;
import com.legends.model.Hero;
import com.legends.model.Monster;
import com.legends.utils.audio.SoundManager;
import com.legends.io.Output;
import java.io.Serializable;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Legends of Valor game board.
 * An 8x8 grid divided into 3 lanes separated by inaccessible walls.
 */
public class ValorBoard implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int BOARD_SIZE = 8;
    // private static final int NUM_LANES = 3;

    private Tile[][] grid;
    private List<Hero> heroes;
    private List<Monster> monsters;

    /**
     * Constructs a new ValorBoard with the standard 8x8 layout.
     */
    public ValorBoard() {
        this.grid = new Tile[BOARD_SIZE][BOARD_SIZE];
        this.heroes = new ArrayList<>();
        this.monsters = new ArrayList<>();
        initializeBoard();
    }

    /**
     * Initializes the board with the proper layout:
     * - Row 0: Monsters' Nexus
     * - Row 7: Heroes' Nexus
     * - Columns 2 and 5: Inaccessible walls
     * - Other spaces: randomly distributed special tiles
     */
    private void initializeBoard() {
        Random rand = new Random();

        for (int y = 0; y < BOARD_SIZE; y++) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                // First row: Monsters' Nexus
                if (y == 0 && isLaneColumn(x)) {
                    grid[y][x] = new NexusTile(x, y, false);
                }
                // Last row: Heroes' Nexus
                else if (y == BOARD_SIZE - 1 && isLaneColumn(x)) {
                    grid[y][x] = new NexusTile(x, y, true);
                }
                // Wall columns (2 and 5)
                else if (x == 2 || x == 5) {
                    grid[y][x] = new InaccessibleTile(x, y);
                }
                // Lane spaces: distribute special tiles
                else if (isLaneColumn(x)) {
                    int roll = rand.nextInt(100);
                    if (roll < 20) { // 20% Bush
                        grid[y][x] = new BushTile(x, y);
                    } else if (roll < 40) { // 20% Cave
                        grid[y][x] = new CaveTile(x, y);
                    } else if (roll < 60) { // 20% Koulou
                        grid[y][x] = new KoulouTile(x, y);
                    } else if (roll < 70) { // 10% Obstacle
                        grid[y][x] = new ObstacleTile(x, y);
                    } else { // 30% Plain
                        grid[y][x] = new PlainTile(x, y);
                    }
                } else {
                    grid[y][x] = new PlainTile(x, y);
                }
            }
        }
    }

    /**
     * Checks if a column is part of a lane (not a wall).
     *
     * @param x The column index.
     * @return True if the column is in a lane.
     */
    private boolean isLaneColumn(int x) {
        return (x == 0 || x == 1 || x == 3 || x == 4 || x == 6 || x == 7);
    }

    /**
     * Gets the lane number for a given x-coordinate.
     * Lane 0 = columns 0-1, Lane 1 = columns 3-4, Lane 2 = columns 6-7
     *
     * @param x The x-coordinate.
     * @return The lane number (0-2), or -1 if not in a lane.
     */
    public int getLane(int x) {
        if (x == 0 || x == 1)
            return 0;
        if (x == 3 || x == 4)
            return 1;
        if (x == 6 || x == 7)
            return 2;
        return -1;
    }

    /**
     * Gets the left spawn column for a lane.
     *
     * @param lane The lane number (0-2).
     * @return The x-coordinate of the left column in the lane.
     */
    public int getLeftColumnOfLane(int lane) {
        switch (lane) {
            case 0:
                return 0;
            case 1:
                return 3;
            case 2:
                return 6;
            default:
                return -1;
        }
    }

    /**
     * Gets the right spawn column for a lane.
     *
     * @param lane The lane number (0-2).
     * @return The x-coordinate of the right column in the lane.
     */
    public int getRightColumnOfLane(int lane) {
        switch (lane) {
            case 0:
                return 1;
            case 1:
                return 4;
            case 2:
                return 7;
            default:
                return -1;
        }
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
     * Gets the height of the board.
     *
     * @return The height (8).
     */
    public int getHeight() {
        return BOARD_SIZE;
    }

    /**
     * Gets the width of the board.
     *
     * @return The width (8).
     */
    public int getWidth() {
        return BOARD_SIZE;
    }

    /**
     * Checks if coordinates are valid and within bounds.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return True if valid, false otherwise.
     */
    private boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE;
    }

    /**
     * Places a hero on the board at the specified coordinates.
     *
     * @param hero The hero to place.
     * @param x    The x-coordinate.
     * @param y    The y-coordinate.
     */
    public void placeHero(Hero hero, int x, int y) {
        if (isValidCoordinate(x, y) && grid[y][x].isAccessible()) {
            grid[y][x].setEntity(hero);
            hero.setX(x);
            hero.setY(y);
            if (!heroes.contains(hero)) {
                heroes.add(hero);
            }
        }
    }

    /**
     * Places a monster on the board at the specified coordinates.
     *
     * @param monster The monster to place.
     * @param x       The x-coordinate.
     * @param y       The y-coordinate.
     */
    public void placeMonster(Monster monster, int x, int y) {
        if (isValidCoordinate(x, y) && grid[y][x].isAccessible()) {
            // In Valor, monsters don't replace the entity, they're tracked separately
            monster.setX(x);
            monster.setY(y);
            if (!monsters.contains(monster)) {
                monsters.add(monster);
            }
        }
    }

    /**
     * Gets the hero at the specified coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return The hero at the coordinates, or null if none.
     */
    public Hero getHeroAt(int x, int y) {
        for (Hero hero : heroes) {
            if (hero.getX() == x && hero.getY() == y && hero.isAlive()) {
                return hero;
            }
        }
        return null;
    }

    /**
     * Gets the monster at the specified coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return The monster at the coordinates, or null if none.
     */
    public Monster getMonsterAt(int x, int y) {
        for (Monster monster : monsters) {
            if (monster.getX() == x && monster.getY() == y && monster.isAlive()) {
                return monster;
            }
        }
        return null;
    }

    /**
     * Checks if a space has a hero.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return True if a hero is at the location.
     */
    public boolean hasHeroAt(int x, int y) {
        return getHeroAt(x, y) != null;
    }

    /**
     * Checks if a space has a monster.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return True if a monster is at the location.
     */
    public boolean hasMonsterAt(int x, int y) {
        return getMonsterAt(x, y) != null;
    }

    /**
     * Moves a hero from one position to another.
     *
     * @param hero   The hero to move.
     * @param toX    The destination x-coordinate.
     * @param toY    The destination y-coordinate.
     * @param output The output interface for messages.
     * @return True if the move was successful.
     */
    public boolean moveHero(Hero hero, int toX, int toY, Output output) {
        int fromX = hero.getX();
        int fromY = hero.getY();

        if (!isValidCoordinate(toX, toY)) {
            if (output != null)
                output.println("Invalid coordinates!");
            return false;
        }

        Tile toTile = grid[toY][toX];
        if (!toTile.isAccessible()) {
            if (output != null)
                output.println("Cannot move to inaccessible tile!");
            return false;
        }

        // Check for hero collision
        if (hasHeroAt(toX, toY)) {
            if (output != null)
                output.println("Another hero is already there!");
            return false;
        }

        // Check if trying to move behind a monster in the same lane
        if (getLane(fromX) == getLane(toX) && hasMonsterAt(toX, toY)) {
            if (toY < fromY) { // trying to move up behind a monster
                if (output != null)
                    output.println("Cannot move behind a monster!");
                return false;
            }
        }

        // Update hero position
        Tile fromTile = grid[fromY][fromX];
        if (fromTile.getEntity() == hero) {
            fromTile.setEntity(null);
        }

        hero.setX(toX);
        hero.setY(toY);
        grid[toY][toX].setEntity(hero);

        SoundManager.getInstance().playMoveSound();

        return true;
    }

    /**
     * Moves a monster forward (down) one space.
     *
     * @param monster The monster to move.
     * @param output  The output interface for messages.
     * @return True if the move was successful.
     */
    public boolean moveMonster(Monster monster, Output output) {
        int fromX = monster.getX();
        int fromY = monster.getY();
        int toY = fromY + 1; // Move down

        if (!isValidCoordinate(fromX, toY)) {
            return false;
        }

        Tile toTile = grid[toY][fromX];
        if (!toTile.isAccessible()) {
            return false;
        }

        // Monsters can't move to spaces with other monsters
        if (hasMonsterAt(fromX, toY)) {
            return false;
        }

        // If there's a hero in the way, don't move
        if (hasHeroAt(fromX, toY)) {
            return false;
        }

        monster.setX(fromX);
        monster.setY(toY);
        output.printlnRed(monster.getName() + " advanced forward!" );
        return true;
    }

    /**
     * Destroys an obstacle, turning it into a plain tile.
     *
     * @param x      The x-coordinate of the obstacle.
     * @param y      The y-coordinate of the obstacle.
     * @param output The output interface for messages.
     * @return True if the obstacle was destroyed.
     */
    public boolean destroyObstacle(int x, int y, Output output) {
        if (!isValidCoordinate(x, y)) {
            return false;
        }

        if (grid[y][x] instanceof ObstacleTile) {
            grid[y][x] = new PlainTile(x, y);
            if (output != null) {
                output.printlnGreen("Obstacle destroyed!");
            }
            return true;
        }

        if (output != null) {
            output.println("No obstacle to destroy!");
        }
        return false;
    }

    /**
     * Gets all heroes on the board.
     *
     * @return List of heroes.
     */
    public List<Hero> getHeroes() {
        return heroes;
    }

    /**
     * Gets all monsters on the board.
     *
     * @return List of monsters.
     */
    public List<Monster> getMonsters() {
        return monsters;
    }

    /**
     * Removes a dead monster from the board.
     *
     * @param monster The monster to remove.
     */
    public void removeMonster(Monster monster) {
        monsters.remove(monster);
    }

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_MAGENTA = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";

    /**
     * Prints the board in a formatted display.
     *
     * @param output The output interface.
     */
    public void printBoard(Output output) {
        output.println("\n=== Legends of Valor Board ===");
        output.print("     ");
        for (int x = 0; x < BOARD_SIZE; x++) {
            output.print(String.format("%-10d", x));
        }
        output.println();

        for (int y = 0; y < BOARD_SIZE; y++) {
            // Print row separator
            output.print("   ");
            for (int x = 0; x < BOARD_SIZE; x++) {
                output.print("+--------");
            }
            output.println("+");

            // Print tile content (first line - hero/monster)
            output.print(String.format("%2d ", y));
            for (int x = 0; x < BOARD_SIZE; x++) {
                output.print("| ");
                Hero hero = getHeroAt(x, y);
                Monster monster = getMonsterAt(x, y);

                if (hero != null && monster != null) {
                    output.print(
                            ANSI_GREEN + "H" + hero.getLane() + ANSI_RESET + "/" + ANSI_RED + "M" + ANSI_RESET + "  ");
                } else if (hero != null) {
                    output.print(ANSI_GREEN + "H" + hero.getLane() + ANSI_RESET + "     ");
                } else if (monster != null) {
                    output.print(ANSI_RED + "M" + monster.getLane() + ANSI_RESET + "     ");
                } else {
                    output.print("       ");
                }
            }
            output.println("|");

            // Print tile type (second line)
            output.print("   ");
            for (int x = 0; x < BOARD_SIZE; x++) {
                output.print("| ");
                Tile tile = grid[y][x];
                String symbol = tile.getSymbol();
                String color = "";

                if (tile instanceof NexusTile) {
                    color = ANSI_YELLOW;
                } else if (tile instanceof InaccessibleTile) {
                    color = ANSI_BLUE;
                } else if (tile instanceof BushTile) {
                    color = ANSI_GREEN;
                } else if (tile instanceof CaveTile) {
                    color = ANSI_CYAN;
                } else if (tile instanceof KoulouTile) {
                    color = ANSI_MAGENTA;
                } else if (tile instanceof ObstacleTile) {
                    color = ANSI_RED;
                }

                output.print(color + symbol + ANSI_RESET + "      ");
            }
            output.println("|");
        }

        // Print bottom border
        output.print("   ");
        for (int x = 0; x < BOARD_SIZE; x++) {
            output.print("+--------");
        }
        output.println("+");

        // Print legend
        output.println("\nLegend:");
        output.println(ANSI_GREEN + "H0/H1/H2" + ANSI_RESET + " = Heroes | " +
                ANSI_RED + "M0/M1/M2" + ANSI_RESET + " = Monsters");
        output.println(ANSI_YELLOW + "N" + ANSI_RESET + " = Nexus | " +
                ANSI_BLUE + "I" + ANSI_RESET + " = Inaccessible | " +
                "P = Plain");
        output.println(ANSI_GREEN + "B" + ANSI_RESET + " = Bush (Dex+) | " +
                ANSI_CYAN + "C" + ANSI_RESET + " = Cave (Agi+) | " +
                ANSI_MAGENTA + "K" + ANSI_RESET + " = Koulou (Str+)");
        output.println(ANSI_RED + "O" + ANSI_RESET + " = Obstacle");
    }
}
