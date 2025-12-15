package com.legends.market;

import com.legends.model.Item;
import java.io.Serializable;
import java.util.List;

/**
 * Interface for Market behavior.
 */
public interface Market extends Serializable {
    /**
     * Gets the list of items available in the market.
     * 
     * @return List of items.
     */
    List<Item> getInventory();

    /**
     * Adds an item to the market inventory.
     * 
     * @param item The item to add.
     */
    void addItem(Item item);

    /**
     * Removes an item from the market inventory.
     * 
     * @param item The item to remove.
     * @return True if removed, false otherwise.
     */
    boolean removeItem(Item item);
}
