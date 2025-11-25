package com.legends.model;

import com.legends.io.Output;

/**
 * Abstract base class for all spells.
 * Spells have damage, mana cost, and a duration (though duration is not fully utilized in this simplified version).
 */
public abstract class Spell extends Item {
    protected int damage;
    protected int manaCost;
    protected int duration;

    /**
     * Constructs a new Spell.
     *
     * @param name          The name of the spell.
     * @param cost          The cost of the spell.
     * @param requiredLevel The level required to use the spell.
     * @param damage        The damage dealt by the spell.
     * @param manaCost      The mana cost of the spell.
     */
    public Spell(String name, int cost, int requiredLevel, int damage, int manaCost) {
        super(name, cost, requiredLevel);
        this.damage = damage;
        this.manaCost = manaCost;
        this.duration = 3; // Default duration
    }

    /**
     * Gets the damage dealt by the spell.
     *
     * @return The damage.
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Gets the mana cost of the spell.
     *
     * @return The mana cost.
     */
    public int getManaCost() {
        return manaCost;
    }

    /**
     * Gets the duration of the spell effect.
     *
     * @return The duration.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Applies the spell's special effect to the target monster.
     *
     * @param target The target monster.
     * @param output The output interface for messages.
     */
    public abstract void applyEffect(Monster target, Output output);

    /**
     * Gets the description of the spell's effect.
     *
     * @return The effect description.
     */
    public abstract String getEffectDescription();
}
