package com.legends.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents an inventory of items.
 */
public class Inventory implements Serializable, Iterable<Item> {
    private static final long serialVersionUID = 1L;
    private List<Item> items;

    /**
     * Constructs a new empty Inventory.
     */
    public Inventory() {
        this.items = new ArrayList<>();
    }

    /**
     * Adds an item to the inventory.
     *
     * @param item The item to add.
     */
    public void add(Item item) {
        items.add(item);
    }

    /**
     * Removes an item from the inventory.
     *
     * @param item The item to remove.
     * @return True if the item was removed, false otherwise.
     */
    public boolean remove(Item item) {
        return items.remove(item);
    }

    /**
     * Gets the list of items in the inventory.
     *
     * @return The list of items.
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * Checks if the inventory is empty.
     *
     * @return True if empty, false otherwise.
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Gets the number of items in the inventory.
     *
     * @return The size of the inventory.
     */
    public int size() {
        return items.size();
    }

    /**
     * Gets an item at a specific index.
     *
     * @param index The index of the item.
     * @return The item.
     */
    public Item get(int index) {
        return items.get(index);
    }

    /**
     * Checks if the inventory contains a specific item.
     *
     * @param item The item to check.
     * @return True if the item is in the inventory.
     */
    public boolean contains(Item item) {
        return items.contains(item);
    }

    @Override
    public Iterator<Item> iterator() {
        return items.iterator();
    }

    @Override
    public String toString() {
        return items.toString();
    }
}
