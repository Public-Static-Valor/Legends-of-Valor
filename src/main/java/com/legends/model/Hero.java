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
    protected Weapon mainHandWeapon;
    protected Weapon offHandWeapon;
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
    
    public boolean equipMainHand(Weapon weapon) {
        if (weapon.getRequiredHands() == 2) {
            this.offHandWeapon = null;
        }
        this.mainHandWeapon = weapon;
        return true;
    }

    public boolean equipOffHand(Weapon weapon) {
        if (weapon.getRequiredHands() == 2) {
            System.out.println("Cannot equip 2-handed weapon in off-hand.");
            return false;
        }
        if (this.mainHandWeapon != null && this.mainHandWeapon.getRequiredHands() == 2) {
            System.out.println("Cannot equip off-hand weapon while holding a 2-handed weapon.");
            return false;
        }
        this.offHandWeapon = weapon;
        return true;
    }

    public void equipArmor(Armor armor) {
        this.equippedArmor = armor;
    }

    public Weapon getMainHandWeapon() {
        return mainHandWeapon;
    }

    public Weapon getOffHandWeapon() {
        return offHandWeapon;
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
        String weaponStr = (mainHandWeapon != null ? mainHandWeapon.getName() : "None");
        if (offHandWeapon != null) {
            weaponStr += " & " + offHandWeapon.getName();
        }
        return name + " (Lvl " + level + ") HP:" + hp + " MP:" + mana + " Wpn:" + weaponStr;
    }
}
