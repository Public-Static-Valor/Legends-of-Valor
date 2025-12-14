package com.legends.model;

/**
 * Represents a Warrior hero.
 * Warriors favor strength and agility when leveling up.
 */
public class Warrior extends Hero {
    /**
     * Constructs a new Warrior.
     *
     * @param name       The name of the warrior.
     * @param mana       The starting mana.
     * @param strength   The starting strength.
     * @param agility    The starting agility.
     * @param dexterity  The starting dexterity.
     * @param money      The starting money.
     * @param experience The starting experience.
     */
    public Warrior(String name, int mana, int strength, int agility, int dexterity, int money, int experience) {
        super(name, mana, strength, agility, dexterity, money, experience, "Warrior");
    }

    /**
     * Levels up the warrior, increasing stats with a focus on strength and agility.
     */
    @Override
    protected void applyHeroTypeBonuses() {
        strength = (int) (strength * 1.1); // Warriors favor strength
        agility = (int) (agility * 1.1); // And agility
        dexterity = (int) (dexterity * 1.05);
    }
}
