package com.legends.model;

public class Warrior extends Hero {
    public Warrior(String name, int mana, int strength, int agility, int dexterity, int money, int experience) {
        super(name, mana, strength, agility, dexterity, money, experience);
    }

    @Override
    public void levelUp() {
        level++;
        hp = level * 100;
        mana = (int) (mana * 1.1);
        strength = (int) (strength * 1.1); // Warriors favor strength
        agility = (int) (agility * 1.1); // And agility
        dexterity = (int) (dexterity * 1.05);
    }
}
