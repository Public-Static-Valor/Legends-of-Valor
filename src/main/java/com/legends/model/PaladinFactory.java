package com.legends.model;

/**
 * Factory for creating Paladin heroes.
 */
public class PaladinFactory implements HeroFactory {
    @Override
    public Hero createHero(String name, int mana, int strength, int agility, int dexterity, int money, int experience) {
        return new Paladin(name, mana, strength, agility, dexterity, money, experience);
    }
}
