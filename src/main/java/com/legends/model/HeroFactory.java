package com.legends.model;

/**
 * Interface for creating Hero objects.
 * Implements the Factory pattern for Hero creation.
 */
public interface HeroFactory {
    /**
     * Creates a new Hero instance.
     *
     * @param name       The name of the hero.
     * @param mana       The starting mana.
     * @param strength   The starting strength.
     * @param agility    The starting agility.
     * @param dexterity  The starting dexterity.
     * @param money      The starting money.
     * @param experience The starting experience.
     * @return A new Hero instance.
     */
    Hero createHero(String name, int mana, int strength, int agility, int dexterity, int money, int experience);
}
