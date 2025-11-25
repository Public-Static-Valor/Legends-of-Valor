package com.legends.model;

/**
 * Represents a Potion item.
 * Potions increase a specific attribute of a hero.
 */
public class Potion extends Item {
    private int attributeIncrease;
    private String attributeAffected;

    /**
     * Constructs a new Potion.
     *
     * @param name              The name of the potion.
     * @param cost              The cost of the potion.
     * @param requiredLevel     The level required to use the potion.
     * @param attributeIncrease The amount the attribute is increased.
     * @param attributeAffected The attribute affected by the potion.
     */
    public Potion(String name, int cost, int requiredLevel, int attributeIncrease, String attributeAffected) {
        super(name, cost, requiredLevel);
        this.attributeIncrease = attributeIncrease;
        this.attributeAffected = attributeAffected;
    }

    /**
     * Gets the amount the attribute is increased.
     *
     * @return The attribute increase amount.
     */
    public int getAttributeIncrease() {
        return attributeIncrease;
    }

    /**
     * Gets the attribute affected by the potion.
     *
     * @return The attribute name.
     */
    public String getAttributeAffected() {
        return attributeAffected;
    }
}
