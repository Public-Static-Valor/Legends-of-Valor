package com.legends.model;

/**
 * Abstract base class for all items in the game.
 * Items have a name, cost, and required level.
 */
public abstract class Item {
    protected String name;
    protected int cost;
    protected int requiredLevel;

    /**
     * Constructs a new Item.
     *
     * @param name          The name of the item.
     * @param cost          The cost of the item.
     * @param requiredLevel The level required to use the item.
     */
    public Item(String name, int cost, int requiredLevel) {
        this.name = name;
        this.cost = cost;
        this.requiredLevel = requiredLevel;
    }

    /**
     * Gets the name of the item.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the cost of the item.
     *
     * @return The cost.
     */
    public int getCost() {
        return cost;
    }

    /**
     * Gets the required level to use the item.
     *
     * @return The required level.
     */
    public int getRequiredLevel() {
        return requiredLevel;
    }
}
