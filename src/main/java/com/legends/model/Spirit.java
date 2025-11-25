package com.legends.model;

/**
 * Represents a Spirit monster.
 * Spirits have increased dodge ability.
 */
public class Spirit extends Monster {
    /**
     * Constructs a new Spirit.
     *
     * @param name        The name of the monster.
     * @param level       The level of the monster.
     * @param damage      The base damage of the monster.
     * @param defense     The base defense of the monster.
     * @param dodgeChance The dodge chance of the monster.
     */
    public Spirit(String name, int level, int damage, int defense, int dodgeChance) {
        // Spirits have increased dodge ability
        super(name, level, damage, defense, (int)(dodgeChance * 1.1));
    }
}
