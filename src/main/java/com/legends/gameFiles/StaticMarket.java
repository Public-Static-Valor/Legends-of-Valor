package com.legends.gameFiles;

import com.legends.model.Item;
import java.util.ArrayList;
import java.util.List;

/**
 * A static market that always contains all items.
 * Used in Legends of Valor.
 */
public class StaticMarket implements Market {
    private static final long serialVersionUID = 1L;
    private List<Item> allItems;

    public StaticMarket(List<Item> allItems) {
        this.allItems = allItems;
    }

    @Override
    public List<Item> getInventory() {
        // Return a new list containing all items so the caller can't modify the source
        return new ArrayList<>(allItems);
    }

    @Override
    public void addItem(Item item) {
        // Static market accepts items but they don't change the static inventory
        // effectively, selling to a static market just gives you money
    }

    @Override
    public boolean removeItem(Item item) {
        // Static market has infinite supply
        return true;
    }
}
