package com.legends.model;

/**
 * Represents a Paladin hero.
 * Paladins favor strength and dexterity when leveling up.
 */
public class Paladin extends Hero {
    /**
     * Constructs a new Paladin.
     *
     * @param name       The name of the paladin.
     * @param mana       The starting mana.
     * @param strength   The starting strength.
     * @param agility    The starting agility.
     * @param dexterity  The starting dexterity.
     * @param money      The starting money.
     * @param experience The starting experience.
     */
    public Paladin(String name, int mana, int strength, int agility, int dexterity, int money, int experience) {
        super(name, mana, strength, agility, dexterity, money, experience, "Paladin");
    }

    /**
     * Levels up the paladin, increasing stats with a focus on strength and dexterity.
     */
    @Override
    public void levelUp() {
        level++;
        hp = level * 100;
        mana = (int) (mana * 1.1);
        strength = (int) (strength * 1.1); // Paladins favor strength
        agility = (int) (agility * 1.05);
        dexterity = (int) (dexterity * 1.1); // And dexterity
    }
}
