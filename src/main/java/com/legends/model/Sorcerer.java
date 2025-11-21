package com.legends.model;

public class Sorcerer extends Hero {
    public Sorcerer(String name, int mana, int strength, int agility, int dexterity, int money, int experience) {
        super(name, mana, strength, agility, dexterity, money, experience, "Sorcerer");
    }

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
