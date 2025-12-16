package com.legends.market;

import com.legends.io.Input;
import com.legends.io.Output;
import com.legends.model.*;
import com.legends.ui.StyledOutput;
import com.legends.utils.audio.SoundManager;
import java.io.Serializable;
import java.util.ArrayList;
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

    /**
     * Handles buying items from the market.
     */
    default void buyItem(Hero hero, Input input, Output output, StyledOutput styledOutput) {
        output.println("\nSelect category:");
        output.println("1. Weapons");
        output.println("2. Armor");
        output.println("3. Potions");
        output.println("4. Spells");
        output.print("Choose category: ");

        String category = input.readLine();
        List<Item> availableItems = new ArrayList<>();
        List<Item> marketInventory = getInventory();

        switch (category) {
            case "1":
                for (Item item : marketInventory) {
                    if (item instanceof Weapon)
                        availableItems.add(item);
                }
                break;
            case "2":
                for (Item item : marketInventory) {
                    if (item instanceof Armor)
                        availableItems.add(item);
                }
                break;
            case "3":
                for (Item item : marketInventory) {
                    if (item instanceof Potion)
                        availableItems.add(item);
                }
                break;
            case "4":
                for (Item item : marketInventory) {
                    if (item instanceof Spell)
                        availableItems.add(item);
                }
                break;
            default:
                output.println("Invalid category.");
                return;
        }

        if (availableItems.isEmpty()) {
            output.println("No items available in this category.");
            return;
        }

        output.println("\nAvailable items:");
        for (int i = 0; i < availableItems.size(); i++) {
            Item item = availableItems.get(i);
            output.println((i + 1) + ". " + item.getName() + " (Cost: " + item.getCost() +
                    ", Level: " + item.getRequiredLevel() + ")");
        }
        output.print("Choose item (or 0 to cancel): ");

        try {
            String in = input.readLine();
            int choice = Integer.parseInt(in);
            if (choice == 0)
                return;
            if (choice < 1 || choice > availableItems.size()) {
                output.println("Invalid choice.");
                return;
            }

            Item item = availableItems.get(choice - 1);
            if (hero.getMoney() < item.getCost()) {
                output.println("Not enough gold!");
            } else if (hero.getLevel() < item.getRequiredLevel()) {
                output.println("Level too low!");
            } else {
                hero.setMoney(hero.getMoney() - item.getCost());
                hero.addItem(item);
                removeItem(item);
                SoundManager.getInstance().playBuySound();
                styledOutput.printBuy(hero.getName(), item.getName(), item.getCost());
            }
        } catch (NumberFormatException e) {
            output.println("Invalid input.");
        }
    }

    /**
     * Handles selling items to the market.
     */
    default void sellItem(Hero hero, Input input, Output output, StyledOutput styledOutput) {
        if (hero.getInventory().isEmpty()) {
            output.println("No items to sell!");
            return;
        }

        output.println("\nYour inventory:");
        for (int i = 0; i < hero.getInventory().size(); i++) {
            Item item = hero.getInventory().get(i);
            int sellPrice = item.getCost() / 2;
            output.println((i + 1) + ". " + item.getName() + " (Sell for: " + sellPrice + " gold)");
        }
        output.print("Choose item to sell (or 0 to cancel): ");

        try {
            String in = input.readLine();
            int choice = Integer.parseInt(in);
            if (choice == 0)
                return;
            if (choice < 1 || choice > hero.getInventory().size()) {
                output.println("Invalid choice.");
                return;
            }

            Item item = hero.getInventory().get(choice - 1);
            int sellPrice = item.getCost() / 2;
            hero.setMoney(hero.getMoney() + sellPrice);
            hero.removeItem(item);
            addItem(item);
            SoundManager.getInstance().playSellSound();
            styledOutput.printSell(hero.getName(), item.getName(), sellPrice);
        } catch (NumberFormatException e) {
            output.println("Invalid input.");
        }
    }
}
