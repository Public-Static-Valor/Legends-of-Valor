package com.legends.model;

/**
 * Factory for creating Warrior heroes.
 */
public class WarriorFactory implements HeroFactory {
    @Override
    public Hero createHero(String name, int mana, int strength, int agility, int dexterity, int money, int experience) {
        return new Warrior(name, mana, strength, agility, dexterity, money, experience);
    }
}
