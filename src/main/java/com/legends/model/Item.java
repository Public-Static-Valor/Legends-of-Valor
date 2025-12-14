package com.legends.model;

import java.io.Serializable;

/**
 * Abstract base class for all items in the game.
 * Items have a name, cost, and required level.
 */
public abstract class Item implements Serializable {
    private static final long serialVersionUID = 1L;
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
     * Sets the cost of the item.
     *
     * @param cost The new cost.
     */
    public void setCost(int cost) {
        this.cost = cost;
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
