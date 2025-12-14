package com.legends.Game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.legends.io.Input;
import com.legends.io.Output;
import com.legends.model.Hero;
import com.legends.model.Item;
import com.legends.model.Monster;
import com.legends.utils.DataLoader;

public abstract class GameInterface {
    protected List<Hero> heroes;
    protected List<Monster> monsters;
    protected List<Item> items;
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
            items.addAll(DataLoader.loadWeapons("resources/Weaponry.csv"));
            items.addAll(DataLoader.loadArmor("resources/Armory.csv"));
            items.addAll(DataLoader.loadPotions("resources/Potions.csv"));
            items.addAll(DataLoader.loadSpells("resources/FireSpells.csv", "Fire"));
            items.addAll(DataLoader.loadSpells("resources/IceSpells.csv", "Ice"));
            items.addAll(DataLoader.loadSpells("resources/LightningSpells.csv", "Lightning"));

        } catch (IOException e) {
            output.printError("Error loading game data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    protected abstract void start();

    protected abstract void startGame();

    protected abstract void resetGame();

    protected abstract void showInstructions();

    protected abstract void gameLoop();

    protected abstract void showInfoMenu();

}
