package com.legends.utils;

import com.legends.model.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {

    public static List<Hero> loadHeroes(String filename, String type) throws IOException {
        List<Hero> heroes = new ArrayList<>();
        try (InputStream is = DataLoader.class.getResourceAsStream("/" + filename);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.trim().split("\\s+");
                String name = parts[0];
                int mana = Integer.parseInt(parts[1]);
                int strength = Integer.parseInt(parts[2]);
                int agility = Integer.parseInt(parts[3]);
                int dexterity = Integer.parseInt(parts[4]);
                int money = Integer.parseInt(parts[5]);
                int experience = Integer.parseInt(parts[6]);

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

    public static List<Monster> loadMonsters(String filename, String type) throws IOException {
        List<Monster> monsters = new ArrayList<>();
        try (InputStream is = DataLoader.class.getResourceAsStream("/" + filename);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.trim().split("\\s+");
                String name = parts[0];
                int level = Integer.parseInt(parts[1]);
                int damage = Integer.parseInt(parts[2]);
                int defense = Integer.parseInt(parts[3]);
                int dodgeChance = Integer.parseInt(parts[4]);

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

    public static List<Weapon> loadWeapons(String filename) throws IOException {
        List<Weapon> weapons = new ArrayList<>();
        try (InputStream is = DataLoader.class.getResourceAsStream("/" + filename);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.trim().split("\\s+");
                String name = parts[0];
                int cost = Integer.parseInt(parts[1]);
                int level = Integer.parseInt(parts[2]);
                int damage = Integer.parseInt(parts[3]);
                int hands = Integer.parseInt(parts[4]);
                weapons.add(new Weapon(name, cost, level, damage, hands));
            }
        }
        return weapons;
    }

    public static List<Armor> loadArmor(String filename) throws IOException {
        List<Armor> armors = new ArrayList<>();
        try (InputStream is = DataLoader.class.getResourceAsStream("/" + filename);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.trim().split("\\s+");
                String name = parts[0];
                int cost = Integer.parseInt(parts[1]);
                int level = Integer.parseInt(parts[2]);
                int damageReduction = Integer.parseInt(parts[3]);
                armors.add(new Armor(name, cost, level, damageReduction));
            }
        }
        return armors;
    }

    public static List<Potion> loadPotions(String filename) throws IOException {
        List<Potion> potions = new ArrayList<>();
        try (InputStream is = DataLoader.class.getResourceAsStream("/" + filename);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.trim().split("\\s+");
                String name = parts[0];
                int cost = Integer.parseInt(parts[1]);
                int level = Integer.parseInt(parts[2]);
                int attributeIncrease = Integer.parseInt(parts[3]);
                String attributeAffected = parts[4];
                potions.add(new Potion(name, cost, level, attributeIncrease, attributeAffected));
            }
        }
        return potions;
    }

    public static List<Spell> loadSpells(String filename, String type) throws IOException {
        List<Spell> spells = new ArrayList<>();
        try (InputStream is = DataLoader.class.getResourceAsStream("/" + filename);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.trim().split("\\s+");
                String name = parts[0];
                int cost = Integer.parseInt(parts[1]);
                int level = Integer.parseInt(parts[2]);
                int damage = Integer.parseInt(parts[3]);
                int manaCost = Integer.parseInt(parts[4]);

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
