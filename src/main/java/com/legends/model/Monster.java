package com.legends.model;

public abstract class Monster extends Entity {
    protected int damage;
    protected int defense;
    protected int dodgeChance;

    public Monster(String name, int level, int damage, int defense, int dodgeChance) {
        super(name, level);
        this.damage = damage;
        this.defense = defense;
        this.dodgeChance = dodgeChance;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getDodgeChance() {
        return dodgeChance;
    }

    public void setDodgeChance(int dodgeChance) {
        this.dodgeChance = dodgeChance;
    }

    @Override
    public String toString() {
        return name + " (Lvl " + level + ") HP:" + hp + " Dmg:" + damage + " Def:" + defense;
    }
}
