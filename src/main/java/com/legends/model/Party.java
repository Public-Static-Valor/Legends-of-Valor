package com.legends.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a party of heroes.
 * Manages the list of heroes in the party.
 */
public class Party {
    private List<Hero> heroes;

    /**
     * Constructs a new empty Party.
     */
    public Party() {
        this.heroes = new ArrayList<>();
    }

    /**
     * Adds a hero to the party.
     *
     * @param hero The hero to add.
     */
    public void addHero(Hero hero) {
        this.heroes.add(hero);
    }

    /**
     * Gets an unmodifiable list of heroes in the party.
     *
     * @return The list of heroes.
     */
    public List<Hero> getHeroes() {
        return Collections.unmodifiableList(heroes);
    }

    /**
     * Gets a hero at a specific index.
     *
     * @param index The index of the hero.
     * @return The hero at the index.
     */
    public Hero getHero(int index) {
        return heroes.get(index);
    }

    /**
     * Checks if the party contains a specific hero.
     *
     * @param hero The hero to check.
     * @return True if the hero is in the party.
     */
    public boolean contains(Hero hero) {
        return heroes.contains(hero);
    }

    /**
     * Gets the number of heroes in the party.
     *
     * @return The party size.
     */
    public int getSize() {
        return heroes.size();
    }

    /**
     * Checks if the party is empty.
     *
     * @return True if the party is empty.
     */
    public boolean isEmpty() {
        return heroes.isEmpty();
    }

    /**
     * Gets the leader of the party (the first hero).
     *
     * @return The leader hero, or null if empty.
     */
    public Hero getLeader() {
        if (heroes.isEmpty()) {
            return null;
        }
        return heroes.get(0);
    }

    /**
     * Sets the location of all heroes in the party.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public void setLocation(int x, int y) {
        for (Hero hero : heroes) {
            hero.setX(x);
            hero.setY(y);
        }
    }
}
