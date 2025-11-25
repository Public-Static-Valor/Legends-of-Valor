package com.legends.model;

/**
 * Represents a Sorcerer hero.
 * Sorcerers favor agility and dexterity when leveling up.
 */
public class Sorcerer extends Hero {
    /**
     * Constructs a new Sorcerer.
     *
     * @param name       The name of the sorcerer.
     * @param mana       The starting mana.
     * @param strength   The starting strength.
     * @param agility    The starting agility.
     * @param dexterity  The starting dexterity.
     * @param money      The starting money.
     * @param experience The starting experience.
     */
    public Sorcerer(String name, int mana, int strength, int agility, int dexterity, int money, int experience) {
        super(name, mana, strength, agility, dexterity, money, experience, "Sorcerer");
    }

    /**
     * Levels up the sorcerer, increasing stats with a focus on agility and dexterity.
     */
    @Override
    public void levelUp() {
        level++;
        hp = level * 100;
        mana = (int) (mana * 1.1);
        strength = (int) (strength * 1.05);
        agility = (int) (agility * 1.1); // Sorcerers favor agility
        dexterity = (int) (dexterity * 1.1); // And dexterity
    }
}
