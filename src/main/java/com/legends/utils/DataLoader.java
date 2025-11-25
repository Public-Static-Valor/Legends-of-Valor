package com.legends.utils;

import com.legends.model.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for loading game data from CSV files.
 */
public class DataLoader {

    /**
     * Loads heroes from a CSV file.
     *
     * @param filename The name of the CSV file.
     * @param type     The type of hero (Paladin, Sorcerer, Warrior).
     * @return A list of loaded heroes.
     * @throws IOException If an I/O error occurs.
     */
    public static List<Hero> loadHeroes(String filename, String type) throws IOException {
        List<Hero> heroes = new ArrayList<>();
        try (InputStream is = DataLoader.class.getResourceAsStream("/" + filename);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.trim().split(",");
                String name = parts[0].trim();
                int mana = Integer.parseInt(parts[1].trim()) / 3;
                int strength = Integer.parseInt(parts[2].trim());
                int agility = Integer.parseInt(parts[3].trim());
                int dexterity = Integer.parseInt(parts[4].trim());
                int money = Integer.parseInt(parts[5].trim());
                int experience = Integer.parseInt(parts[6].trim());

                if (type.equals("Paladin")) {
                    heroes.add(new Paladin(name, mana, strength, agility, dexterity, money, experience));
                } else if (type.equals("Sorcerer")) {
                    heroes.add(new Sorcerer(name, mana, strength, agility, dexterity, money, experience));
                } else if (type.equals("Warrior")) {
                    heroes.add(new Warrior(name, mana, strength, agility, dexterity, money, experience));
                }
            }
        }
        return heroes;
    }

    /**
     * Loads monsters from a CSV file.
     *
     * @param filename The name of the CSV file.
     * @param type     The type of monster (Spirit, Dragon, Exoskeleton).
     * @return A list of loaded monsters.
     * @throws IOException If an I/O error occurs.
     */
    public static List<Monster> loadMonsters(String filename, String type) throws IOException {
        List<Monster> monsters = new ArrayList<>();
        try (InputStream is = DataLoader.class.getResourceAsStream("/" + filename);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.trim().split(",");
                String name = parts[0].trim();
                int level = Integer.parseInt(parts[1].trim());
                int damage = Integer.parseInt(parts[2].trim());
                int defense = Integer.parseInt(parts[3].trim());
                int dodgeChance = Integer.parseInt(parts[4].trim());

                if (type.equals("Spirit")) {
                    monsters.add(new Spirit(name, level, damage, defense, dodgeChance));
                } else if (type.equals("Dragon")) {
                    monsters.add(new Dragon(name, level, damage, defense, dodgeChance));
                } else if (type.equals("Exoskeleton")) {
                    monsters.add(new Exoskeleton(name, level, damage, defense, dodgeChance));
                }
            }
        }
        return monsters;
    }

    /**
     * Loads weapons from a CSV file.
     *
     * @param filename The name of the CSV file.
     * @return A list of loaded weapons.
     * @throws IOException If an I/O error occurs.
     */
    public static List<Weapon> loadWeapons(String filename) throws IOException {
        List<Weapon> weapons = new ArrayList<>();
        try (InputStream is = DataLoader.class.getResourceAsStream("/" + filename);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.trim().split(",");
                String name = parts[0].trim();
                int cost = Integer.parseInt(parts[1].trim());
                int level = Integer.parseInt(parts[2].trim());
                int damage = Integer.parseInt(parts[3].trim());
                int hands = Integer.parseInt(parts[4].trim());
                weapons.add(new Weapon(name, cost, level, damage, hands));
            }
        }
        return weapons;
    }

    /**
     * Loads armor from a CSV file.
     *
     * @param filename The name of the CSV file.
     * @return A list of loaded armor.
     * @throws IOException If an I/O error occurs.
     */
    public static List<Armor> loadArmor(String filename) throws IOException {
        List<Armor> armors = new ArrayList<>();
        try (InputStream is = DataLoader.class.getResourceAsStream("/" + filename);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.trim().split(",");
                String name = parts[0].trim();
                int cost = Integer.parseInt(parts[1].trim());
                int level = Integer.parseInt(parts[2].trim());
                int damageReduction = Integer.parseInt(parts[3].trim());
                armors.add(new Armor(name, cost, level, damageReduction));
            }
        }
        return armors;
    }

    /**
     * Loads potions from a CSV file.
     *
     * @param filename The name of the CSV file.
     * @return A list of loaded potions.
     * @throws IOException If an I/O error occurs.
     */
    public static List<Potion> loadPotions(String filename) throws IOException {
        List<Potion> potions = new ArrayList<>();
        try (InputStream is = DataLoader.class.getResourceAsStream("/" + filename);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.trim().split(",");
                String name = parts[0].trim();
                int cost = Integer.parseInt(parts[1].trim());
                int level = Integer.parseInt(parts[2].trim());
                int attributeIncrease = Integer.parseInt(parts[3].trim());
                String attributeAffected = parts[4].trim();
                if (attributeAffected.equals("Mana")) {
                    attributeIncrease /= 3;
                }
                potions.add(new Potion(name, cost, level, attributeIncrease, attributeAffected));
            }
        }
        return potions;
    }

    /**
     * Loads spells from a CSV file.
     *
     * @param filename The name of the CSV file.
     * @param type     The type of spell (Fire, Ice, Lightning).
     * @return A list of loaded spells.
     * @throws IOException If an I/O error occurs.
     */
    public static List<Spell> loadSpells(String filename, String type) throws IOException {
        List<Spell> spells = new ArrayList<>();
        try (InputStream is = DataLoader.class.getResourceAsStream("/" + filename);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.trim().split(",");
                String name = parts[0].trim();
                int cost = Integer.parseInt(parts[1].trim());
                int level = Integer.parseInt(parts[2].trim());
                int damage = Integer.parseInt(parts[3].trim()) / 10;
                int manaCost = Integer.parseInt(parts[4].trim()) / 3;

                if (type.equals("Fire")) {
                    spells.add(new FireSpell(name, cost, level, damage, manaCost));
                } else if (type.equals("Ice")) {
                    spells.add(new IceSpell(name, cost, level, damage, manaCost));
                } else if (type.equals("Lightning")) {
                    spells.add(new LightningSpell(name, cost, level, damage, manaCost));
                }
            }
        }
        return spells;
    }
}
