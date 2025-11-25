package com.legends.model;

/**
 * Represents an Exoskeleton monster.
 * Exoskeletons have increased defense.
 */
public class Exoskeleton extends Monster {
    /**
     * Constructs a new Exoskeleton.
     *
     * @param name        The name of the monster.
     * @param level       The level of the monster.
     * @param damage      The base damage of the monster.
     * @param defense     The base defense of the monster.
     * @param dodgeChance The dodge chance of the monster.
     */
    public Exoskeleton(String name, int level, int damage, int defense, int dodgeChance) {
        // Exoskeletons have increased defense
        super(name, level, damage, (int)(defense * 1.1), dodgeChance);
    }
}
