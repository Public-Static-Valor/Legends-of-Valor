package com.legends.model;

/**
 * Represents a Weapon item.
 * Weapons have damage and a required number of hands to use.
 */
public class Weapon extends Item {
    private int damage;
    private int requiredHands;

    /**
     * Constructs a new Weapon.
     *
     * @param name          The name of the weapon.
     * @param cost          The cost of the weapon.
     * @param requiredLevel The level required to use the weapon.
     * @param damage        The damage dealt by the weapon.
     * @param requiredHands The number of hands required to use the weapon.
     */
    public Weapon(String name, int cost, int requiredLevel, int damage, int requiredHands) {
        super(name, cost, requiredLevel);
        this.damage = damage;
        this.requiredHands = requiredHands;
    }

    /**
     * Gets the damage dealt by the weapon.
     *
     * @return The damage.
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Gets the number of hands required to use the weapon.
     *
     * @return The required hands.
     */
    public int getRequiredHands() {
        return requiredHands;
    }
}
