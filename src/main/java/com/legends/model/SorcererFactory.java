package com.legends.model;

/**
 * Factory for creating Sorcerer heroes.
 */
public class SorcererFactory implements HeroFactory {
    @Override
    public Hero createHero(String name, int mana, int strength, int agility, int dexterity, int money, int experience) {
        return new Sorcerer(name, mana, strength, agility, dexterity, money, experience);
    }
}
