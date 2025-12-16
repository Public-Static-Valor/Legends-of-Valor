package com.legends.game;

import com.legends.io.Input;
import com.legends.io.Output;
import com.legends.model.*;
import com.legends.ui.StyledOutput;


/**
 * Abstract base class for RPG style games.
 * Provides common functionality for RPG games like factories and styled output.
 */
public abstract class RPGGame extends GameInterface {
    private static final long serialVersionUID = 1L;
    
    protected transient StyledOutput styledOutput;
    protected SpiritFactory spiritFactory;
    protected DragonFactory dragonFactory;
    protected ExoskeletonFactory exoskeletonFactory;

    public RPGGame(Input input, Output output) {
        super(input, output);
        this.styledOutput = new StyledOutput(output);
        this.spiritFactory = new SpiritFactory();
        this.dragonFactory = new DragonFactory();
        this.exoskeletonFactory = new ExoskeletonFactory();
    }

    /**
     * Displays the final statistics for a list of heroes.
     *
     * @param heroes The list of heroes to display stats for.
     */
    protected void printHeroFinalStats(java.util.List<Hero> heroes) {
        for (Hero hero : heroes) {
            output.println(hero.getName() + ": Level " + hero.getLevel() +
                    ", Gold: " + hero.getMoney() + ", XP: " + hero.getExperience() +
                    ", Total Gold Earned: " + hero.getTotalGoldEarned() +
                    ", Total XP Earned: " + hero.getTotalXpEarned());
        }
    }

    /**
     * Allows the user to select heroes from a list.
     *
     * @param availableHeroes The list of available heroes.
     * @param count           The number of heroes to select.
     * @return The list of selected heroes.
     */
    protected java.util.List<Hero> selectHeroes(java.util.List<Hero> availableHeroes, int count) {
        java.util.List<Hero> selected = new java.util.ArrayList<>();
        // Create a copy for display/selection purposes so we can remove selected ones
        java.util.List<Hero> selectableHeroes = new java.util.ArrayList<>(availableHeroes);

        output.println("\n--- Choose your Heroes ---");
        
        for (int i = 0; i < count; i++) {
            output.println("\nSelect Hero " + (i + 1) + ":");
            for (int j = 0; j < selectableHeroes.size(); j++) {
                Hero h = selectableHeroes.get(j);
                output.println((j + 1) + ". " + h.getName() + " (" + h.getHeroClass() +
                        ") Lvl " + h.getLevel() + 
                        ", HP: " + h.getHp() + 
                        ", Mana: " + h.getMana() +
                        ", Str: " + h.getStrength() +
                        ", Agi: " + h.getAgility() +
                        ", Dex: " + h.getDexterity() +
                        ", Money: " + h.getMoney() +
                        ", Exp: " + h.getExperience());
            }

            int choice = -1;
            while (choice < 1 || choice > selectableHeroes.size()) {
                output.print("Enter choice: ");
                try {
                    String in = input.readLine();
                    choice = Integer.parseInt(in);
                    if (choice < 1 || choice > selectableHeroes.size()) {
                        output.println("Invalid choice.");
                    }
                } catch (NumberFormatException e) {
                    output.println("Invalid input. Please enter a number.");
                }
            }
            
            Hero selectedHero = selectableHeroes.get(choice - 1);
            selected.add(selectedHero);
            selectableHeroes.remove(choice - 1);
            output.println("You selected: " + selectedHero.getName());
        }
        return selected;
    }

    /**
     * Handles the action of a hero using a potion.
     *
     * @param hero The hero using the potion.
     * @return True if a potion was used, false otherwise.
     */
    protected boolean handlePotionUse(Hero hero) {
        java.util.List<Potion> potions = new java.util.ArrayList<>();
        for (Item item : hero.getInventory()) {
            if (item instanceof Potion) {
                potions.add((Potion) item);
            }
        }

        if (potions.isEmpty()) {
            output.println("No potions available!");
            return false;
        }

        output.println("\nSelect potion:");
        for (int i = 0; i < potions.size(); i++) {
            Potion p = potions.get(i);
            output.println((i + 1) + ". " + p.getName() + " (+" + p.getAttributeIncrease() +
                    " " + p.getAttributeAffected() + ")");
        }
        output.print("Choose potion (or 0 to cancel): ");

        try {
            int choice = Integer.parseInt(input.readLine());
            if (choice == 0)
                return false;
            if (choice < 1 || choice > potions.size()) {
                output.println("Invalid choice.");
                return false;
            }

            Potion potion = potions.get(choice - 1);
            hero.usePotion(potion);
            styledOutput.printPotionUse(hero.getName(), potion.getName());
            return true;

        } catch (NumberFormatException e) {
            output.println("Invalid input.");
            return false;
        }
    }
}

