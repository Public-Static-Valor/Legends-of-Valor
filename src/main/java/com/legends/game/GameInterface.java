package com.legends.game;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.legends.io.Input;
import com.legends.io.Output;
import com.legends.model.DefaultItemFactory;
import com.legends.model.Hero;
import com.legends.model.Item;
import com.legends.model.Monster;
import com.legends.utils.DataLoader;

public abstract class GameInterface {
    protected List<Hero> heroes;
    protected List<Monster> monsters;
    protected List<Item> items;
    protected boolean gameRunning;
    protected transient Input input;
    protected transient Output output;

    public GameInterface(Input input, Output output) {
        this.heroes = new ArrayList<>();
        this.monsters = new ArrayList<>();
        this.items = new ArrayList<>();
        this.input = input;
        this.output = output;
    }

    public void init() {
        heroes.clear();
        monsters.clear();
        items.clear();
        try {
            // Load Heroes
            heroes.addAll(DataLoader.loadHeroes("resources/Paladins.csv", "Paladin"));
            heroes.addAll(DataLoader.loadHeroes("resources/Sorcerers.csv", "Sorcerer"));
            heroes.addAll(DataLoader.loadHeroes("resources/Warriors.csv", "Warrior"));

            // Load Monsters
            monsters.addAll(DataLoader.loadMonsters("resources/Spirits.csv", "Spirit"));
            monsters.addAll(DataLoader.loadMonsters("resources/Dragons.csv", "Dragon"));
            monsters.addAll(DataLoader.loadMonsters("resources/Exoskeletons.csv", "Exoskeleton"));

            // Load Items
            items.addAll(DataLoader.loadWeapons("resources/Weaponry.csv", new DefaultItemFactory()));
            items.addAll(DataLoader.loadArmor("resources/Armory.csv", new DefaultItemFactory()));
            items.addAll(DataLoader.loadPotions("resources/Potions.csv", new DefaultItemFactory()));
            items.addAll(DataLoader.loadSpells("resources/FireSpells.csv", "Fire", new DefaultItemFactory()));
            items.addAll(DataLoader.loadSpells("resources/IceSpells.csv", "Ice", new DefaultItemFactory()));
            items.addAll(DataLoader.loadSpells("resources/LightningSpells.csv", "Lightning", new DefaultItemFactory()));

        } catch (IOException e) {
            output.printError("Error loading game data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void start() {
        output.println(getWelcomeMessage());

        boolean isRunning = true;
        while (isRunning) {
            try {
                output.println("\n--- Main Menu ---");
                output.println("1. Start New Game");
                output.println("2. Load Game");
                output.println("3. Delete Saved Game");
                output.println("4. How to Play");
                output.println("5. Quit");
                output.print("Choose an option: ");

                String choice = input.readLine();

                switch (choice) {
                    case "1":
                        startGame();
                        break;
                    case "2":
                        loadGame();
                        break;
                    case "3":
                        deleteSaveGame();
                        break;
                    case "4":
                        showInstructions();
                        break;
                    case "5":
                        isRunning = false;
                        output.println("Goodbye!");
                        break;
                    default:
                        output.println("Invalid option.");
                }
            } catch (QuitGameException e) {
                output.println("\nReturning to Main Menu...");
            }
        }
    }

    public void saveGame() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(getSaveFileName()))) {
            oos.writeObject(this);
            output.printlnGreen("Game saved successfully!");
        } catch (IOException e) {
            output.printError("Error saving game: " + e.getMessage());
        }
    }

    public void deleteSaveGame() {
        File saveFile = new File(getSaveFileName());
        if (saveFile.exists()) {
            if (saveFile.delete()) {
                output.printlnGreen("Saved game deleted successfully.");
            } else {
                output.printlnRed("Failed to delete saved game.");
            }
        } else {
            output.printlnRed("No saved game to delete.");
        }
    }

    protected abstract String getSaveFileName();

    protected abstract String getWelcomeMessage();

    public abstract void loadGame();

    protected abstract void startGame();

    protected abstract void resetGame();

    protected abstract void showInstructions();

    protected abstract void gameLoop();

    protected abstract void showInfoMenu();

}
