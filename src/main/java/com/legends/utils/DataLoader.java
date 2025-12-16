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
     * Loads heroes from a CSV file using a factory.
     *
     * @param filename The name of the CSV file.
     * @param factory  The factory to create heroes.
     * @return A list of loaded heroes.
     * @throws IOException If an I/O error occurs.
     */
    public static List<Hero> loadHeroes(String filename, HeroFactory factory) throws IOException {
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

                heroes.add(factory.createHero(name, mana, strength, agility, dexterity, money, experience));
            }
        }
        return heroes;
    }

    /**
     * Loads monsters from a CSV file using a factory.
     *
     * @param filename The name of the CSV file.
     * @param factory  The factory to create monsters.
     * @return A list of loaded monsters.
     * @throws IOException If an I/O error occurs.
     */
    public static List<Monster> loadMonsters(String filename, MonsterFactory factory) throws IOException {
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

                // Pass null for AI as these are template monsters
                monsters.add(factory.createMonster(name, level, damage, defense, dodgeChance, null));
            }
        }
        return monsters;
    }

    /**
     * Loads weapons from a CSV file.
     *
     * @param filename The name of the CSV file.
     * @param factory The factory object to create the weapons.
     * @return A list of loaded weapons.
     * @throws IOException If an I/O error occurs.
     */
    public static List<Weapon> loadWeapons(String filename, ItemFactory factory) throws IOException {
        if (factory == null) throw new IllegalArgumentException("ItemFactory cannot be null");
        List<Weapon> weapons = new ArrayList<>();
        try (InputStream is = DataLoader.class.getResourceAsStream("/" + filename)) {
            if (is == null) throw new IOException("Resource not found: " + filename);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String line;
                br.readLine(); // skip header
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.trim().split(",");
                    if (parts.length < 5) continue; // defensive
                    String name = parts[0].trim();
                    int cost = Integer.parseInt(parts[1].trim());
                    int level = Integer.parseInt(parts[2].trim());
                    int damage = Integer.parseInt(parts[3].trim());
                    int hands = Integer.parseInt(parts[4].trim());
                    weapons.add(factory.createWeapon(name, cost, level, damage, hands));
                }
            }
        }
        return weapons;
    }

    /**
     * Loads armor from a CSV file.
     *
     * @param filename The name of the CSV file.
     * @param factory The factory object to create the armors.
     * @return A list of loaded armor.
     * @throws IOException If an I/O error occurs.
     */
    public static List<Armor> loadArmor(String filename, ItemFactory factory) throws IOException {
        if (factory == null) throw new IllegalArgumentException("ItemFactory cannot be null");
        List<Armor> armors = new ArrayList<>();
        try (InputStream is = DataLoader.class.getResourceAsStream("/" + filename)) {
            if (is == null) throw new IOException("Resource not found: " + filename);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String line;
                br.readLine();
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.trim().split(",");
                    if (parts.length < 4) continue;
                    String name = parts[0].trim();
                    int cost = Integer.parseInt(parts[1].trim());
                    int level = Integer.parseInt(parts[2].trim());
                    int damageReduction = Integer.parseInt(parts[3].trim());
                    armors.add(factory.createArmor(name, cost, level, damageReduction));
                }
            }
        }
        return armors;
    }

    /**
     * Loads potions from a CSV file.
     *
     * @param filename The name of the CSV file.
     * @param factory factory object to create Potions.
     * @return A list of loaded potions.
     * @throws IOException If an I/O error occurs.
     */
    public static List<Potion> loadPotions(String filename, ItemFactory factory) throws IOException {
        if (factory == null) throw new IllegalArgumentException("ItemFactory cannot be null");
        List<Potion> potions = new ArrayList<>();
        try (InputStream is = DataLoader.class.getResourceAsStream("/" + filename)) {
            if (is == null) throw new IOException("Resource not found: " + filename);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String line;
                br.readLine();
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.trim().split(",");
                    if (parts.length < 5) continue;
                    String name = parts[0].trim();
                    int cost = Integer.parseInt(parts[1].trim());
                    int level = Integer.parseInt(parts[2].trim());
                    int attributeIncrease = Integer.parseInt(parts[3].trim());
                    String attributeAffected = parts[4].trim();
                    if ("Mana".equalsIgnoreCase(attributeAffected)) {
                        attributeIncrease /= 3;
                    }
                    potions.add(factory.createPotion(name, cost, level, attributeIncrease, attributeAffected));
                }
            }
        }
        return potions;
    }

    /**
     * Loads spells from a CSV file.
     *
     * @param filename The name of the CSV file.
     * @param type     The type of spell (Fire, Ice, Lightning).
     * @param factory  factory object for creating spells.
     * @return A list of loaded spells.
     * @throws IOException If an I/O error occurs.
     */
    public static List<Spell> loadSpells(String filename, String type, ItemFactory factory) throws IOException {
        if (factory == null) throw new IllegalArgumentException("ItemFactory cannot be null");
        List<Spell> spells = new ArrayList<>();
        SpellType spellType = SpellType.fromString(type);
        try (InputStream is = DataLoader.class.getResourceAsStream("/" + filename)) {
            if (is == null) throw new IOException("Resource not found: " + filename);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String line;
                br.readLine();
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.trim().split(",");
                    if (parts.length < 5) continue;
                    String name = parts[0].trim();
                    int cost = Integer.parseInt(parts[1].trim());
                    int level = Integer.parseInt(parts[2].trim());
                    int damage = Integer.parseInt(parts[3].trim()) / 10;
                    int manaCost = Integer.parseInt(parts[4].trim()) / 3;
                    spells.add(factory.createSpell(name, cost, level, damage, manaCost, spellType));
                }
            }
        }
        return spells;
    }
}
