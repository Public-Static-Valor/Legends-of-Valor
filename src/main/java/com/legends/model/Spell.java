package com.legends.model;

public abstract class Spell extends Item {
    protected int damage;
    protected int manaCost;
    protected int duration;

    public Spell(String name, int cost, int requiredLevel, int damage, int manaCost) {
        super(name, cost, requiredLevel);
        this.damage = damage;
        this.manaCost = manaCost;
        this.duration = 3; // Default duration
    }

    public int getDamage() {
        return damage;
    }

    public int getManaCost() {
        return manaCost;
    }

    public int getDuration() {
        return duration;
    }

    public abstract void applyEffect(Monster target);
    public abstract String getEffectDescription();
}
