package com.legends.model;

import com.legends.io.Output;
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
    protected String heroClass;

    public Hero(String name, int mana, int strength, int agility, int dexterity, int money, int experience, String heroClass) {
        super(name, 1); // Heroes start at level 1 usually, but file has starting experience
        this.mana = mana;
        this.strength = strength;
        this.agility = agility;
        this.dexterity = dexterity;
        this.money = money;
        this.experience = experience;
        this.inventory = new ArrayList<>();
        this.heroClass = heroClass;
        this.level = 1; // Default
        this.hp = this.level * 100;
    }

    // Level up logic implemented in subclasses
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

    public void gainExperience(int amount, Output output) {
        this.experience += amount;
        int xpNeeded = this.level * 10;
        while (this.experience >= xpNeeded) {
            this.experience -= xpNeeded;
            levelUp();
            if (output != null) output.println(this.name + " leveled up to Level " + this.level + "!");
            xpNeeded = this.level * 10;
            this.hp = this.level * 100;
        }
    }
    
    public boolean equipMainHand(Weapon weapon) {
        if (this.mainHandWeapon != null) {
            this.inventory.add(this.mainHandWeapon);
        }
        
        if (weapon.getRequiredHands() == 2) {
            if (this.offHandWeapon != null) {
                this.inventory.add(this.offHandWeapon);
                this.offHandWeapon = null;
            }
        }
        
        this.mainHandWeapon = weapon;
        this.inventory.remove(weapon);
        return true;
    }

    public boolean equipOffHand(Weapon weapon, Output output) {
        if (weapon.getRequiredHands() == 2) {
            if (output != null) output.println("Cannot equip 2-handed weapon in off-hand.");
            return false;
        }
        if (this.mainHandWeapon != null && this.mainHandWeapon.getRequiredHands() == 2) {
            if (output != null) output.println("Cannot equip off-hand weapon while holding a 2-handed weapon.");
            return false;
        }
        
        if (this.offHandWeapon != null) {
            this.inventory.add(this.offHandWeapon);
        }
        this.offHandWeapon = weapon;
        this.inventory.remove(weapon);
        return true;
    }

    public void equipArmor(Armor armor) {
        if (this.equippedArmor != null) {
            this.inventory.add(this.equippedArmor);
        }
        this.equippedArmor = armor;
        this.inventory.remove(armor);
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

    public String getHeroClass() {
        return heroClass;
    }

    public void applyPotion(Potion potion) {
        String attr = potion.getAttributeAffected();
        int amount = potion.getAttributeIncrease();
        
        switch (attr) {
            case "Health":
                this.hp += amount;
                break;
            case "Mana":
                this.mana += amount;
                break;
            case "Strength":
                this.strength += amount;
                break;
            case "Dexterity":
                this.dexterity += amount;
                break;
            case "Agility":
                this.agility += amount;
                break;
            default:
                break;
        }
    }

    public void usePotion(Potion potion) {
        applyPotion(potion);
        removeItem(potion);
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
