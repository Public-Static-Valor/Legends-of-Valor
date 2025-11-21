package com.legends.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Hero extends Entity {
    protected int mana;
    protected int strength;
    protected int agility;
    protected int dexterity;
    protected int money;
    protected int experience;
    protected List<Item> inventory;
    protected Weapon equippedWeapon;
    protected Armor equippedArmor;

    public Hero(String name, int mana, int strength, int agility, int dexterity, int money, int experience) {
        super(name, 1); // Heroes start at level 1 usually, but file has starting experience
        this.mana = mana;
        this.strength = strength;
        this.agility = agility;
        this.dexterity = dexterity;
        this.money = money;
        this.experience = experience;
        this.inventory = new ArrayList<>();
        
        this.level = 1; // Default
        this.hp = this.level * 100;
    }

    // TODO: Level up logic based on experience to be added here
    public abstract void levelUp();

    // Getters and Setters
    public int getMana() { return mana; }
    public void setMana(int mana) { this.mana = mana; }
    public int getStrength() { return strength; }
    public void setStrength(int strength) { this.strength = strength; }
    public int getAgility() { return agility; }
    public void setAgility(int agility) { this.agility = agility; }
    public int getDexterity() { return dexterity; }
    public void setDexterity(int dexterity) { this.dexterity = dexterity; }
    public int getMoney() { return money; }
    public void setMoney(int money) { this.money = money; }
    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }
    
    public void equipWeapon(Weapon weapon) {
        this.equippedWeapon = weapon;
    }

    public void equipArmor(Armor armor) {
        this.equippedArmor = armor;
    }

    public Weapon getEquippedWeapon() {
        return equippedWeapon;
    }

    public Armor getEquippedArmor() {
        return equippedArmor;
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public void addItem(Item item) {
        inventory.add(item);
    }
    
    public void removeItem(Item item) {
        inventory.remove(item);
    }

    @Override
    public String toString() {
        return name + " (Lvl " + level + ") HP:" + hp + " MP:" + mana;
    }
}
