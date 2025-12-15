package com.legends.model;

import com.legends.ai.MonsterAI;
import com.legends.board.ValorBoard;
import com.legends.io.Output;

/**
 * Abstract base class for all monsters.
 * Monsters have damage, defense, and dodge chance stats.
 */
public abstract class Monster extends Entity {
    protected MonsterAI ai;
    protected int damage;
    protected int defense;
    protected int dodgeChance;
    protected int lane; // For Legends of Valor: which lane (0-2) the monster is in
    protected int maxHp;

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
        this.lane = -1; // Not assigned to a lane by default
        this.maxHp = level * 100;
    }

    public void setAI(MonsterAI ai) {
        this.ai = ai;
    }

    public void takeTurn(ValorBoard valorBoard, Output output) {
        if (ai != null) {
            ai.takeTurn(this, valorBoard, output);
        }
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

    public int getLane() {
        return lane;
    }

    public void setLane(int lane) {
        this.lane = lane;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    @Override
    public String toString() {
        String hpBar = com.legends.io.ConsoleOutput.createProgressBar(hp, maxHp, com.legends.io.ConsoleOutput.ANSI_RED);
        return name + " (Lvl " + level + ") HP:" + hpBar + " Dmg:" + damage + " Def:" + defense;
    }
}
