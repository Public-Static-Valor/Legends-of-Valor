package com.legends.model;

/**
 * Represents an armor item.
 * Armor provides damage reduction to the wearer.
 */
public class Armor extends Item {
    private int damageReduction;

    /**
     * Constructs a new Armor item.
     *
     * @param name            The name of the armor.
     * @param cost            The cost of the armor.
     * @param requiredLevel   The level required to equip the armor.
     * @param damageReduction The amount of damage reduction provided.
     */
    public Armor(String name, int cost, int requiredLevel, int damageReduction) {
        super(name, cost, requiredLevel);
        this.damageReduction = damageReduction;
    }

    /**
     * Gets the damage reduction value.
     *
     * @return The damage reduction.
     */
    public int getDamageReduction() {
        return damageReduction;
    }
}
