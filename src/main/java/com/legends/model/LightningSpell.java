package com.legends.model;

import com.legends.io.Output;

/**
 * Represents a Lightning spell.
 * Reduces the target's dodge chance.
 */
public class LightningSpell extends Spell {
    /**
     * Constructs a new LightningSpell.
     *
     * @param name          The name of the spell.
     * @param cost          The cost of the spell.
     * @param requiredLevel The level required to use the spell.
     * @param damage        The damage dealt by the spell.
     * @param manaCost      The mana cost of the spell.
     */
    public LightningSpell(String name, int cost, int requiredLevel, int damage, int manaCost) {
        super(name, cost, requiredLevel, damage, manaCost);
    }

    /**
     * Applies the spell's effect to the target monster.
     * Reduces the target's dodge chance by 10%.
     *
     * @param target The target monster.
     * @param output The output interface for messages.
     */
    @Override
    public void applyEffect(Monster target, Output output) {
        int currentDodge = target.getDodgeChance();
        int reduction = (int) (currentDodge * 0.1); // Reduce dodge chance by 10%
        target.setDodgeChance(currentDodge - reduction);
        if (output != null) output.println(target.getName() + "'s dodge chance was reduced by " + reduction + "!");
    }

    /**
     * Gets the description of the spell's effect.
     *
     * @return The effect description.
     */
    @Override
    public String getEffectDescription() {
        return "Reduces target's dodge chance";
    }
}
