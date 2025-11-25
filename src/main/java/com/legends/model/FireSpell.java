package com.legends.model;

import com.legends.io.Output;

/**
 * Represents a Fire spell.
 * Reduces the target's defense.
 */
public class FireSpell extends Spell {
    /**
     * Constructs a new FireSpell.
     *
     * @param name          The name of the spell.
     * @param cost          The cost of the spell.
     * @param requiredLevel The level required to use the spell.
     * @param damage        The damage dealt by the spell.
     * @param manaCost      The mana cost of the spell.
     */
    public FireSpell(String name, int cost, int requiredLevel, int damage, int manaCost) {
        super(name, cost, requiredLevel, damage, manaCost);
    }

    /**
     * Applies the spell's effect to the target monster.
     * Reduces the target's defense by 10%.
     *
     * @param target The target monster.
     * @param output The output interface for messages.
     */
    @Override
    public void applyEffect(Monster target, Output output) {
        int currentDefense = target.getDefense();
        int reduction = (int) (currentDefense * 0.1); // Reduce defense by 10%
        target.setDefense(currentDefense - reduction);
        if (output != null) output.println(target.getName() + "'s defense was reduced by " + reduction + "!");
    }

    /**
     * Gets the description of the spell's effect.
     *
     * @return The effect description.
     */
    @Override
    public String getEffectDescription() {
        return "Reduces target's defense";
    }
}
