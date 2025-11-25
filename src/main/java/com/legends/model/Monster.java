package com.legends.model;

/**
 * Abstract base class for all monsters.
 * Monsters have damage, defense, and dodge chance stats.
 */
public abstract class Monster extends Entity {
    protected int damage;
    protected int defense;
    protected int dodgeChance;

    /**
     * Constructs a new Monster.
     *
     * @param name        The name of the monster.
     * @param level       The level of the monster.
     * @param damage      The base damage of the monster.
     * @param defense     The base defense of the monster.
     * @param dodgeChance The dodge chance of the monster.
     */
    public Monster(String name, int level, int damage, int defense, int dodgeChance) {
        super(name, level);
        this.damage = damage;
        this.defense = defense;
        this.dodgeChance = dodgeChance;
    }

    /**
     * Gets the monster's damage.
     *
     * @return The damage.
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Sets the monster's damage.
     *
     * @param damage The new damage value.
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * Gets the monster's defense.
     *
     * @return The defense.
     */
    public int getDefense() {
        return defense;
    }

    /**
     * Sets the monster's defense.
     *
     * @param defense The new defense value.
     */
    public void setDefense(int defense) {
        this.defense = defense;
    }

    /**
     * Gets the monster's dodge chance.
     *
     * @return The dodge chance.
     */
    public int getDodgeChance() {
        return dodgeChance;
    }

    /**
     * Sets the monster's dodge chance.
     *
     * @param dodgeChance The new dodge chance value.
     */
    public void setDodgeChance(int dodgeChance) {
        this.dodgeChance = dodgeChance;
    }

    @Override
    public String toString() {
        return name + " (Lvl " + level + ") HP:" + hp + " Dmg:" + damage + " Def:" + defense;
    }
}
