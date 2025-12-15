package com.legends.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import com.legends.io.Input;
import com.legends.io.Output;
import com.legends.model.DefaultItemFactory;
import com.legends.model.Hero;
import com.legends.model.Item;
import com.legends.model.Monster;
import com.legends.utils.DataLoader;
import com.legends.utils.audio.SoundManager;

public abstract class GameInterface implements Serializable {
    private static final long serialVersionUID = 1L;
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
            heroes.addAll(DataLoader.loadHeroes("Paladins.csv", "Paladin"));
            heroes.addAll(DataLoader.loadHeroes("Sorcerers.csv", "Sorcerer"));
            heroes.addAll(DataLoader.loadHeroes("Warriors.csv", "Warrior"));

            // Load Monsters
            monsters.addAll(DataLoader.loadMonsters("Spirits.csv", "Spirit"));
            monsters.addAll(DataLoader.loadMonsters("Dragons.csv", "Dragon"));
            monsters.addAll(DataLoader.loadMonsters("Exoskeletons.csv", "Exoskeleton"));

            // Load Items
            items.addAll(DataLoader.loadWeapons("Weaponry.csv", new DefaultItemFactory()));
            items.addAll(DataLoader.loadArmor("Armory.csv", new DefaultItemFactory()));
            items.addAll(DataLoader.loadPotions("Potions.csv", new DefaultItemFactory()));
            items.addAll(DataLoader.loadSpells("FireSpells.csv", "Fire", new DefaultItemFactory()));
            items.addAll(DataLoader.loadSpells("IceSpells.csv", "Ice", new DefaultItemFactory()));
            items.addAll(DataLoader.loadSpells("LightningSpells.csv", "Lightning", new DefaultItemFactory()));

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
                output.println("5. Settings");
                output.println("6. Quit");
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
                        showSettingsMenu();
                        break;
                    case "6":
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

    private void showSettingsMenu() {
        boolean inSettings = true;
        SoundManager soundManager = SoundManager.getInstance();

        while (inSettings) {
            output.println("\n--- Settings ---");
            output.println("1. Toggle Sound (Current: " + (soundManager.isSoundEnabled() ? "On" : "Off") + ")");
            output.println("2. Set Volume (Current: " + (int) (soundManager.getMasterVolume() * 100) + "%)");
            output.println("3. Back to Main Menu");
            output.print("Choose an option: ");

            String choice = input.readLine();

            switch (choice) {
                case "1":
                    boolean newState = !soundManager.isSoundEnabled();
                    soundManager.setSoundEnabled(newState);
                    output.println("Sound turned " + (newState ? "On" : "Off"));
                    break;
                case "2":
                    output.print("Enter volume (0-100): ");
                    try {
                        int vol = Integer.parseInt(input.readLine());
                        if (vol < 0 || vol > 100) {
                            output.println("Invalid volume. Please enter 0-100.");
                        } else {
                            soundManager.setMasterVolume(vol / 100.0f);
                            output.println("Volume set to " + vol + "%");
                        }
                    } catch (NumberFormatException e) {
                        output.println("Invalid input.");
                    }
                    break;
                case "3":
                    inSettings = false;
                    break;
                default:
                    output.println("Invalid option.");
            }
        }
    }

    protected boolean confirmQuit() {
        output.println("Checking for unsaved changes...");
        if (isGameSaved()) {
            return true;
        }
        output.print("You have unsaved changes. Are you sure you want to quit? (Y/N): ");
        String response = input.readLine().trim().toUpperCase();
        return response.equals("Y");
    }

    private boolean isGameSaved() {
        File saveFile = new File(getSaveFileName());
        if (!saveFile.exists()) {
            return false;
        }

        try {
            // Create temp file
            File tempFile = File.createTempFile("tempSave", ".ser");
            tempFile.deleteOnExit();

            // Save current state to temp file
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(tempFile))) {
                oos.writeObject(this);
            }

            // Compare hashes
            String saveHash = calculateFileHash(saveFile);
            String tempHash = calculateFileHash(tempFile);

            // Clean up temp file immediately
            tempFile.delete();

            return saveHash.equals(tempHash);

        } catch (Exception e) {
            // If any error occurs during check, assume unsaved
            return false;
        }
    }

    private String calculateFileHash(File file) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        }
        byte[] hashBytes = digest.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
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
