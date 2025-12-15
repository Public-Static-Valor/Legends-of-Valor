package com.legends.market;

import com.legends.model.Item;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * A dynamic market that has a unique inventory.
 * Used in Monsters and Heroes.
 */
public class DynamicMarket implements Market {
    private static final long serialVersionUID = 1L;
    private List<Item> inventory;

    public DynamicMarket(List<Item> allItems) {
        this.inventory = new ArrayList<>();
        if (allItems != null && !allItems.isEmpty()) {
            List<Item> shuffled = new ArrayList<>(allItems);
            Collections.shuffle(shuffled);
            int subsetSize = 5 + new Random().nextInt(6); // 5-10 items
            for (int i = 0; i < Math.min(subsetSize, shuffled.size()); i++) {
                Item clone = cloneItem(shuffled.get(i));
                if (clone != null) {
                    inventory.add(clone);
                }
            }
        }
    }

    @Override
    public List<Item> getInventory() {
        return inventory;
    }

    @Override
    public void addItem(Item item) {
        // When hero sells item, we add it to inventory
        Item clone = cloneItem(item);
        if (clone != null) {
            inventory.add(clone);
        }
    }

    @Override
    public boolean removeItem(Item item) {
        return inventory.remove(item);
    }

    private Item cloneItem(Item item) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(item);
            oos.flush();
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            return (Item) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
