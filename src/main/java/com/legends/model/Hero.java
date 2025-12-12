package com.legends.model;

import com.legends.io.Output;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for all hero types.
 * Manages hero stats, inventory, and equipment.
 */
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
    protected boolean isMainHandTwoHandedGrip;
    protected int lane; // For Legends of Valor: which lane (0-2) the hero is assigned to

    /**
     * Constructs a new Hero.
     *
     * @param name       The name of the hero.
     * @param mana       The starting mana.
     * @param strength   The starting strength.
     * @param agility    The starting agility.
     * @param dexterity  The starting dexterity.
     * @param money      The starting money.
     * @param experience The starting experience.
     * @param heroClass  The class of the hero.
     */
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
        this.lane = -1; // Not assigned to a lane by default
    }

    /**
     * Levels up the hero, increasing stats.
     * Implementation depends on the hero subclass.
     */
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
    public int getLane() { return lane; }
    public void setLane(int lane) { this.lane = lane; }
    public void setExperience(int experience) { this.experience = experience; }

    /**
     * Adds experience points to the hero and checks for level up.
     *
     * @param amount The amount of experience to gain.
     * @param output The output interface for messages.
     */
    public void gainExperience(int amount, Output output) {
        this.experience += amount;
        int xpNeeded = this.level * 10;
        while (this.experience >= xpNeeded) {
            this.experience -= xpNeeded;
            levelUp();
            if (output != null) output.printlnGreen(this.name + " leveled up to Level " + this.level + "!");
            xpNeeded = this.level * 10;
            this.hp = this.level * 100;
        }
    }
    
    /**
     * Equips a weapon to the main hand.
     *
     * @param weapon The weapon to equip.
     * @return True if equipped successfully.
     */
    public boolean equipMainHand(Weapon weapon) {
        return equipMainHand(weapon, weapon.getRequiredHands() == 2);
    }

    /**
     * Equips a weapon to the main hand with a specific grip.
     *
     * @param weapon        The weapon to equip.
     * @param twoHandedGrip Whether to use a two-handed grip.
     * @return True if equipped successfully.
     */
    public boolean equipMainHand(Weapon weapon, boolean twoHandedGrip) {
        if (this.mainHandWeapon != null) {
            this.inventory.add(this.mainHandWeapon);
        }
        
        if (weapon.getRequiredHands() == 2 || twoHandedGrip) {
            if (this.offHandWeapon != null) {
                this.inventory.add(this.offHandWeapon);
                this.offHandWeapon = null;
            }
        }
        
        this.mainHandWeapon = weapon;
        this.isMainHandTwoHandedGrip = twoHandedGrip;
        this.inventory.remove(weapon);
        return true;
    }

    /**
     * Equips a weapon to the off hand.
     *
     * @param weapon The weapon to equip.
     * @param output The output interface for error messages.
     * @return True if equipped successfully, false otherwise.
     */
    public boolean equipOffHand(Weapon weapon, Output output) {
        if (weapon.getRequiredHands() == 2) {
            if (output != null) output.println("Cannot equip 2-handed weapon in off-hand.");
            return false;
        }
        if (this.mainHandWeapon != null && (this.mainHandWeapon.getRequiredHands() == 2 || this.isMainHandTwoHandedGrip)) {
            if (output != null) output.println("Cannot equip off-hand weapon while holding a 2-handed weapon or using 2-handed grip.");
            return false;
        }
        
        if (this.offHandWeapon != null) {
            this.inventory.add(this.offHandWeapon);
        }
        this.offHandWeapon = weapon;
        this.inventory.remove(weapon);
        return true;
    }

    /**
     * Equips armor to the hero.
     *
     * @param armor The armor to equip.
     */
    public void equipArmor(Armor armor) {
        if (this.equippedArmor != null) {
            this.inventory.add(this.equippedArmor);
        }
        this.equippedArmor = armor;
        this.inventory.remove(armor);
    }

    /**
     * Gets the weapon equipped in the main hand.
     *
     * @return The main hand weapon.
     */
    public Weapon getMainHandWeapon() {
        return mainHandWeapon;
    }

    /**
     * Checks if the main hand weapon is held with a two-handed grip.
     *
     * @return True if using a two-handed grip.
     */
    public boolean isMainHandTwoHandedGrip() {
        return isMainHandTwoHandedGrip;
    }

    /**
     * Gets the weapon equipped in the off hand.
     *
     * @return The off hand weapon.
     */
    public Weapon getOffHandWeapon() {
        return offHandWeapon;
    }

    /**
     * Gets the equipped armor.
     *
     * @return The equipped armor.
     */
    public Armor getEquippedArmor() {
        return equippedArmor;
    }

    /**
     * Gets the hero's inventory.
     *
     * @return The list of items in the inventory.
     */
    public List<Item> getInventory() {
        return inventory;
    }

    /**
     * Adds an item to the inventory.
     *
     * @param item The item to add.
     */
    public void addItem(Item item) {
        inventory.add(item);
    }
    
    /**
     * Removes an item from the inventory.
     *
     * @param item The item to remove.
     */
    public void removeItem(Item item) {
        inventory.remove(item);
    }

    /**
     * Gets the hero's class name.
     *
     * @return The hero class.
     */
    public String getHeroClass() {
        return heroClass;
    }

    /**
     * Applies the effects of a potion to the hero.
     *
     * @param potion The potion to apply.
     */
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

    /**
     * Uses a potion, applying its effects and removing it from inventory.
     *
     * @param potion The potion to use.
     */
    public void usePotion(Potion potion) {
        applyPotion(potion);
        removeItem(potion);
    }

    @Override
    public String toString() {
        String weaponStr = (mainHandWeapon != null ? mainHandWeapon.getName() : "None");
        if (isMainHandTwoHandedGrip && mainHandWeapon != null && mainHandWeapon.getRequiredHands() == 1) {
            weaponStr += " (2H Grip)";
        }
        if (offHandWeapon != null) {
            weaponStr += " & " + offHandWeapon.getName();
        }
        return name + " (Lvl " + level + ") HP:" + hp + " MP:" + mana + " Wpn:" + weaponStr;
    }
}
