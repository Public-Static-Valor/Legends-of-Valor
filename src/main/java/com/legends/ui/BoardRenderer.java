package com.legends.ui;

import com.legends.board.Board;
import com.legends.board.ValorBoard;
import com.legends.board.tiles.BushTile;
import com.legends.board.tiles.CaveTile;
import com.legends.board.tiles.InaccessibleTile;
import com.legends.board.tiles.KoulouTile;
import com.legends.board.tiles.MarketTile;
import com.legends.board.tiles.NexusTile;
import com.legends.board.tiles.ObstacleTile;
import com.legends.board.tiles.Tile;
import com.legends.model.Hero;
import com.legends.model.Monster;
import com.legends.io.Output;

/**
 * Renders game boards with enhanced ASCII art and colors.
 * Provides beautiful visualization for both Monsters & Heroes and Legends of
 * Valor.
 */
public class BoardRenderer {

    // ANSI Color codes
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_WHITE = "\u001B[37m";

    // Bright variants
    private static final String BRIGHT_BLACK = "\u001B[90m";
    private static final String BRIGHT_RED = "\u001B[91m";
    private static final String BRIGHT_GREEN = "\u001B[92m";
    private static final String BRIGHT_YELLOW = "\u001B[93m";
    private static final String BRIGHT_BLUE = "\u001B[94m";
    private static final String BRIGHT_MAGENTA = "\u001B[95m";
    private static final String BRIGHT_CYAN = "\u001B[96m";
    private static final String BRIGHT_WHITE = "\u001B[97m";

    /**
     * Renders the Legends of Valor board with enhanced visuals
     */
    public static void renderValorBoard(ValorBoard board, Output output) {
        output.println(BRIGHT_YELLOW
                + "\n+====================================== LEGENDS OF VALOR BATTLEFIELD ======================================+"
                + ANSI_RESET);
        output.println(BRIGHT_CYAN + "        Lane 0 (Top)                  Lane 1 (Mid)                  Lane 2 (Bot)"
                + ANSI_RESET);

        // Print column numbers
        output.print(BRIGHT_WHITE + "     ");
        for (int x = 0; x < board.getWidth(); x++) {
            output.print(String.format("%-10d", x));
        }
        output.println(ANSI_RESET);

        for (int y = 0; y < board.getHeight(); y++) {
            // Print row separator
            output.print("   +");
            for (int x = 0; x < board.getWidth(); x++) {
                output.print("---------+");
            }
            output.println();

            // Print entity row (heroes/monsters)
            output.print(String.format(BRIGHT_WHITE + "%2d |" + ANSI_RESET, y));
            for (int x = 0; x < board.getWidth(); x++) {
                output.print(" ");
                Hero hero = board.getHeroAt(x, y);
                Monster monster = board.getMonsterAt(x, y);

                String cellContent;
                if (hero != null && monster != null) {
                    cellContent = String.format(BRIGHT_GREEN + "H%d" + ANSI_RESET + "/" +
                            BRIGHT_RED + "M%d" + ANSI_RESET + "   ",
                            hero.getOriginalLane() + 1, monster.getLane() + 1);
                } else if (hero != null) {
                    cellContent = String.format(BRIGHT_GREEN + "H%d" + ANSI_RESET + "      ",
                            hero.getOriginalLane() + 1);
                } else if (monster != null) {
                    cellContent = String.format(BRIGHT_RED + "M%d" + ANSI_RESET + "      ",
                            monster.getLane() + 1);
                } else {
                    cellContent = "        ";
                }

                output.print(cellContent + "|");
            }
            output.println();

            // Print tile type row
            output.print("   |");
            for (int x = 0; x < board.getWidth(); x++) {
                Tile tile = board.getTileAt(x, y);
                String symbol = getTileSymbol(tile);
                String color = getTileColor(tile);

                output.print(" " + color + symbol + ANSI_RESET + "       |");
            }
            output.println();
        }

        // Print bottom border
        output.print("   +");
        for (int x = 0; x < board.getWidth(); x++) {
            output.print("---------+");
        }
        output.println();

        printValorLegend(output, board);
    }

    /**
     * Prints the legend for Valor board
     */
    private static void printValorLegend(Output output, ValorBoard board) {
        output.println();
        output.println(
                BRIGHT_YELLOW + "+==================================== LEGEND ====================================+"
                        + ANSI_RESET);

        // Print Hero info
        output.print(BRIGHT_GREEN + "| Heroes: " + ANSI_RESET);
        for (Hero h : board.getHeroes()) {
            String laneInfo = h.getLane() != h.getOriginalLane()
                    ? " (was L" + h.getOriginalLane() + ", now L" + h.getLane() + ")"
                    : " (Lane " + h.getLane() + ")";
            output.print(BRIGHT_GREEN + "H" + (h.getOriginalLane() + 1) + ANSI_RESET +
                    ": " + h.getName() + laneInfo + " | ");
        }
        output.println();

        output.println(BRIGHT_RED + "| Monsters: " + ANSI_RESET + "M1/M2/M3 by lane");
        output.println("|");
        output.println("| " + BRIGHT_YELLOW + "N" + ANSI_RESET +
                " = Nexus (Market Access)    | " + BRIGHT_BLUE + "I" + ANSI_RESET + " = Wall       | " +
                "P = Plain");
        output.println("| " + BRIGHT_GREEN + "B" + ANSI_RESET + " = Bush (Dex+)      | " +
                BRIGHT_CYAN + "C" + ANSI_RESET + " = Cave (Agi+)       | " +
                BRIGHT_MAGENTA + "K" + ANSI_RESET + " = Koulou (Str+)");
        output.println("| " + BRIGHT_RED + "O" + ANSI_RESET + " = Obstacle");
        output.println(
                BRIGHT_YELLOW + "+\"+================================================================================+"
                        + ANSI_RESET);
    }

    /**
     * Renders the Monsters & Heroes board
     */
    public static void renderMHBoard(Board board, Output output) {
        output.println(
                BRIGHT_CYAN + "\n+========== REALM OF LEGENDS ==========+" + ANSI_RESET);

        // Print top border
        output.print("   +");
        for (int x = 0; x < board.getWidth(); x++) {
            output.print("-----+");
        }
        output.println();

        for (int y = 0; y < board.getHeight(); y++) {
            output.print(String.format(BRIGHT_WHITE + "%2d " + ANSI_RESET + "|", y));
            for (int x = 0; x < board.getWidth(); x++) {
                Tile tile = board.getTileAt(x, y);
                String symbol;
                String color;

                if (tile.isOccupied()) {
                    if (tile.getEntity() instanceof Hero) {
                        symbol = "  H  ";
                        color = BRIGHT_GREEN;
                    } else if (tile.getEntity() instanceof Monster) {
                        symbol = "  M  ";
                        color = BRIGHT_RED;
                    } else {
                        symbol = "  ?  ";
                        color = ANSI_WHITE;
                    }
                } else {
                    if (tile instanceof MarketTile) {
                        symbol = "  M  ";
                        color = BRIGHT_YELLOW;
                    } else if (tile instanceof InaccessibleTile) {
                        symbol = "  X  ";
                        color = BRIGHT_BLACK;
                    } else {
                        symbol = "     ";
                        color = ANSI_RESET;
                    }
                }

                output.print(color + symbol + ANSI_RESET + "|");
            }
            output.println();

            // Print row separator
            if (y < board.getHeight() - 1) {
                output.print("   +");
                for (int x = 0; x < board.getWidth(); x++) {
                    output.print("-----+");
                }
                output.println();
            }
        }

        // Print bottom border
        output.print("   +");
        for (int x = 0; x < board.getWidth(); x++) {
            output.print("-----+");
        }
        output.println();

        printMHLegend(output);
    }

    /**
     * Prints the legend for M&H board
     */
    private static void printMHLegend(Output output) {
        output.println();
        output.println(BRIGHT_YELLOW + "+========  LEGEND  =========+" + ANSI_RESET);
        output.println("| " + BRIGHT_GREEN + "H" + ANSI_RESET + " = Hero Party            |");
        output.println("| " + BRIGHT_YELLOW + "M" + ANSI_RESET + " = Market                |");
        output.println("| " + BRIGHT_BLACK + "X" + ANSI_RESET + " = Inaccessible          |");
        output.println("|   = Common Land           |");
        output.println(BRIGHT_YELLOW + "+===========================+" + ANSI_RESET);
    }

    /**
     * Gets the symbol for a tile in Valor board
     */
    private static String getTileSymbol(Tile tile) {
        if (tile instanceof NexusTile) {
            return "N";
        } else if (tile instanceof InaccessibleTile) {
            return "I";
        } else if (tile instanceof BushTile) {
            return "B";
        } else if (tile instanceof CaveTile) {
            return "C";
        } else if (tile instanceof KoulouTile) {
            return "K";
        } else if (tile instanceof ObstacleTile) {
            return "O";
        } else {
            return "P";
        }
    }

    /**
     * Gets the color for a tile in Valor board
     */
    private static String getTileColor(Tile tile) {
        if (tile instanceof NexusTile) {
            return BRIGHT_YELLOW;
        } else if (tile instanceof InaccessibleTile) {
            return BRIGHT_BLUE;
        } else if (tile instanceof BushTile) {
            return BRIGHT_GREEN;
        } else if (tile instanceof CaveTile) {
            return BRIGHT_CYAN;
        } else if (tile instanceof KoulouTile) {
            return BRIGHT_MAGENTA;
        } else if (tile instanceof ObstacleTile) {
            return BRIGHT_RED;
        } else {
            return ANSI_RESET;
        }
    }
}