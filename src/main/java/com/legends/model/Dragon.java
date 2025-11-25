package com.legends.model;

/**
 * Represents a Dragon monster.
 * Dragons have increased base damage.
 */
public class Dragon extends Monster {
    /**
     * Constructs a new Dragon.
     *
     * @param name        The name of the monster.
     * @param level       The level of the monster.
     * @param damage      The base damage of the monster.
     * @param defense     The base defense of the monster.
     * @param dodgeChance The dodge chance of the monster.
     */
    public Dragon(String name, int level, int damage, int defense, int dodgeChance) {
        // Dragons have increased base damage
        super(name, level, (int)(damage * 1.1), defense, dodgeChance);
    }
}
