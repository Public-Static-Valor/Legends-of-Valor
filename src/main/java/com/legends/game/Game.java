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
    private List<Monster> monsters;
    private List<Item> items;
    private Board board;
    private boolean isRunning;
    private Input input;
    private Output output;

    public Game(Input input, Output output) {
        this.heroes = new ArrayList<>();
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

        // Basic game loop
        while (isRunning) {
            output.println("\nMain Menu:");
            output.println("1. Show Heroes");
            output.println("2. Show Monsters");
            output.println("3. Show Items");
            output.println("4. Show Board");
            output.println("5. Exit");
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
        this.board.printBoard(output);
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
