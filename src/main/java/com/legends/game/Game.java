package com.legends.game;

import com.legends.model.*;
import com.legends.utils.DataLoader;
import com.legends.io.Input;
import com.legends.io.Output;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private List<Hero> heroes;
    private Party party;
    private List<Monster> monsters;
    private List<Item> items;
    private Board board;
    private boolean isRunning;
    private Input input;
    private Output output;

    public Game(Input input, Output output) {
        this.heroes = new ArrayList<>();
        this.party = new Party();
        this.monsters = new ArrayList<>();
        this.items = new ArrayList<>();
        this.isRunning = true;
        this.input = input;
        this.output = output;
    }

    public void init() {
        try {
            // Load Heroes
            heroes.addAll(DataLoader.loadHeroes("Paladins.csv", "Paladin"));
            heroes.addAll(DataLoader.loadHeroes("Sorcerers.csv", "Sorcerer"));
            heroes.addAll(DataLoader.loadHeroes("Warriors.csv", "Warrior"));

            // Load Monsters
            monsters.addAll(DataLoader.loadMonsters("Spirits.csv", "Spirit"));
            monsters.addAll(DataLoader.loadMonsters("Dragons.csv", "Dragon"));
            monsters.addAll(DataLoader.loadMonsters("Exoskeletons.csv", "Exoskeleton"));

            // Load Items
            items.addAll(DataLoader.loadWeapons("Weaponry.csv"));
            items.addAll(DataLoader.loadArmor("Armory.csv"));
            items.addAll(DataLoader.loadPotions("Potions.csv"));
            items.addAll(DataLoader.loadSpells("FireSpells.csv", "Fire"));
            items.addAll(DataLoader.loadSpells("IceSpells.csv", "Ice"));
            items.addAll(DataLoader.loadSpells("LightningSpells.csv", "Lightning"));

            output.println("Game Data Loaded Successfully!");
            output.println("Loaded " + heroes.size() + " Heroes.");
            output.println("Loaded " + monsters.size() + " Monsters.");
            output.println("Loaded " + items.size() + " Items.");

        } catch (IOException e) {
            output.printError("Error loading game data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void start() {
        output.println("Welcome to Legends: Monsters and Heroes!");
        
        setupBoard();
        initializeParty();
        placeHeroesOnBoard();

        // Basic game loop
        while (isRunning) {
            output.println("\nMain Menu:");
            output.println("1. Show Heroes");
            output.println("2. Show Monsters");
            output.println("3. Show Items");
            output.println("4. Show Board");
            output.println("5. Move");
            output.println("6. Exit");
            output.print("Choose an option: ");

            String choice = input.readLine();

            switch (choice) {
                case "1":
                    showHeroes();
                    break;
                case "2":
                    showMonsters();
                    break;
                case "3":
                    showItems();
                    break;
                case "4":
                    if (board != null) board.printBoard(output);
                    else output.println("Board not initialized.");
                    break;
                case "5":
                    handleMove();
                    break;
                case "6":
                    isRunning = false;
                    output.println("Goodbye!");
                    break;
                default:
                    output.println("Invalid option.");
            }
        }
    }

    private void setupBoard() {
        int width = 0;
        int height = 0;
        final int MIN_SIZE = 4;
        final int MAX_SIZE = 20; // Max size for terminal display limitations

        output.println("\n--- World Configuration ---");
        
        while (width < MIN_SIZE || width > MAX_SIZE) {
            output.print("Enter world width (" + MIN_SIZE + "-" + MAX_SIZE + "): ");
            try {
                String in = input.readLine();
                width = Integer.parseInt(in);
                if (width < MIN_SIZE || width > MAX_SIZE) {
                    output.println("Invalid width. Must be between " + MIN_SIZE + " and " + MAX_SIZE + ".");
                }
            } catch (NumberFormatException e) {
                output.println("Invalid input. Please enter a number.");
            }
        }

        while (height < MIN_SIZE || height > MAX_SIZE) {
            output.print("Enter world height (" + MIN_SIZE + "-" + MAX_SIZE + "): ");
            try {
                String in = input.readLine();
                height = Integer.parseInt(in);
                if (height < MIN_SIZE || height > MAX_SIZE) {
                    output.println("Invalid height. Must be between " + MIN_SIZE + " and " + MAX_SIZE + ".");
                }
            } catch (NumberFormatException e) {
                output.println("Invalid input. Please enter a number.");
            }
        }

        this.board = new Board(width, height);
        output.println("World created with size " + width + "x" + height + ".");
    }

    private void initializeParty() {
        int partySize = 0;
        while (partySize < 1 || partySize > 3) {
            output.print("Enter number of heroes (1-3): ");
            try {
                String in = input.readLine();
                partySize = Integer.parseInt(in);
                if (partySize < 1 || partySize > 3) {
                    output.println("Invalid number. Must be between 1 and 3.");
                }
            } catch (NumberFormatException e) {
                output.println("Invalid input. Please enter a number.");
            }
        }

        output.println("\n--- Choose your Heroes ---");
        for (int i = 0; i < partySize; i++) {
            output.println("Select Hero " + (i + 1) + ":");
            for (int j = 0; j < heroes.size(); j++) {
                Hero h = heroes.get(j);
                output.println((j + 1) + ". " + h.getName() + 
                    " (Lvl " + h.getLevel() + 
                    ", HP: " + h.getHp() + 
                    ", MP: " + h.getMana() + 
                    ", Str: " + h.getStrength() + 
                    ", Agi: " + h.getAgility() + 
                    ", Dex: " + h.getDexterity() + 
                    ", Money: " + h.getMoney() + 
                    ", Exp: " + h.getExperience() + ")");
            }
            
            int choice = -1;
            while (choice < 1 || choice > heroes.size()) {
                output.print("Enter choice: ");
                try {
                    String in = input.readLine();
                    choice = Integer.parseInt(in);
                    if (choice < 1 || choice > heroes.size()) {
                        output.println("Invalid choice.");
                    } else {
                        Hero selected = heroes.get(choice - 1);
                        if (party.contains(selected)) {
                            output.println("Hero already in party. Choose another.");
                            choice = -1;
                        } else {
                            party.addHero(selected);
                            output.println(selected.getName() + " added to party.");
                        }
                    }
                } catch (NumberFormatException e) {
                    output.println("Invalid input.");
                }
            }
        }
    }

    private void placeHeroesOnBoard() {
        if (!party.isEmpty()) {
            // Place only the first hero to represent the party
            boolean placed = false;
            for (int y = 0; y < board.getHeight(); y++) {
                for (int x = 0; x < board.getWidth(); x++) {
                    if (board.getTileAt(x, y).isAccessible() && !board.getTileAt(x, y).isOccupied()) {
                        board.placeEntity(party.getLeader(), x, y);
                        // Update all party members' coordinates to match the leader
                        party.setLocation(x, y);
                        placed = true;
                        break;
                    }
                }
                if (placed) break;
            }
            
            if (!placed) {
                output.println("Warning: Could not place party due to lack of space!");
            } else {
                output.println("Party placed on board.");
            }
        }
        this.board.printBoard(output);
    }

    private void handleMove() {
        if (board == null) {
            output.println("Board not initialized.");
            return;
        }

        // The party moves together, represented by the first hero
        Hero partyLeader = party.getLeader();

        output.print("Enter direction (W/A/S/D): ");
        String dir = input.readLine().toUpperCase();
        int newX = partyLeader.getX();
        int newY = partyLeader.getY();

        switch (dir) {
            case "W": newY--; break;
            case "S": newY++; break;
            case "A": newX--; break;
            case "D": newX++; break;
            default:
                output.println("Invalid direction.");
                return;
        }

        if (board.moveEntity(partyLeader.getX(), partyLeader.getY(), newX, newY, output)) {
            output.println("Party moved to (" + newX + ", " + newY + ")");
            
            // Update all party members' coordinates to match the leader
            party.setLocation(newX, newY);
            
            // Check for encounter on CommonTile
            Tile tile = board.getTileAt(newX, newY);
            if (tile instanceof CommonTile) {
                checkEncounter();
            }
            
            board.printBoard(output);
        }
    }

    private void checkEncounter() {
        java.util.Random rand = new java.util.Random();
        // 50% chance of encounter
        if (rand.nextInt(100) < 50) {
            output.println("You have encountered monsters!");
            // Logic to create monsters would go here
            // For now, just print a message
            output.println("A battle is about to begin... (Battle logic not implemented yet)");
        }
    }

    private void showHeroes() {
        output.println("\n--- Heroes ---");
        for (Hero h : heroes) {
            output.println(h);
        }
    }

    private void showMonsters() {
        output.println("\n--- Monsters ---");
        for (Monster m : monsters) {
            output.println(m);
        }
    }

    private void showItems() {
        output.println("\n--- Items ---");
        for (Item i : items) {
            output.println(i.getName() + " (Cost: " + i.getCost() + ")");
        }
    }
}
