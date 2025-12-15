package com.legends.model;

import com.legends.ai.MonsterAI;
import com.legends.board.ValorBoard;
import com.legends.io.Output;
import com.legends.utils.audio.SoundManager;

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

    public void takeBattleTurn(java.util.List<Hero> heroes, Output output) {
        if (ai != null) {
            ai.takeBattleTurn(this, heroes, output);
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

    @Override
    public void attack(Entity target, Output output) {
        // Calculate damage
        double attackPower = this.damage;

        // Apply dodge chance
        double dodgeChance = 0;
        int defense = 0;

        if (target instanceof Hero) {
            Hero h = (Hero) target;
            dodgeChance = h.getAgility() * 0.0002;
            if (h.getEquippedArmor() != null) {
                defense = h.getEquippedArmor().getDamageReduction();
            }
        } else if (target instanceof Monster) {
            Monster m = (Monster) target;
            dodgeChance = m.getDodgeChance() * 0.01;
            defense = m.getDefense();
        }

        if (Math.random() < dodgeChance) {
            SoundManager.getInstance().playDodgeSound();
            if (output instanceof com.legends.ui.StyledOutput) {
                ((com.legends.ui.StyledOutput) output).printDodge(target.getName());
            } else {
                output.println(target.getName() + " dodged " + this.name + "'s attack!");
            }
        } else {
            SoundManager.getInstance().playAttackSound();
            int damage = calculateDamage(attackPower, defense);
            target.takeDamage(damage);
            SoundManager.getInstance().playDamageSound();

            if (output instanceof com.legends.ui.StyledOutput) {
                ((com.legends.ui.StyledOutput) output).printAttack(this.name, target.getName(), damage);
            } else {
                output.println(this.name + " attacked " + target.getName() + " for " + damage + " damage!");
            }

            if (!target.isAlive()) {
                if (output instanceof com.legends.ui.StyledOutput) {
                    ((com.legends.ui.StyledOutput) output).printDeath(target.getName());
                } else {
                    output.println(target.getName() + " has fainted!");
                }
            }
        }
    }
}
