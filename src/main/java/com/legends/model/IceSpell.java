package com.legends.model;

import com.legends.io.Output;

/**
 * Represents an Ice spell.
 * Reduces the target's damage.
 */
public class IceSpell extends Spell {
    /**
     * Constructs a new IceSpell.
     *
     * @param name          The name of the spell.
     * @param cost          The cost of the spell.
     * @param requiredLevel The level required to use the spell.
     * @param damage        The damage dealt by the spell.
     * @param manaCost      The mana cost of the spell.
     */
    public IceSpell(String name, int cost, int requiredLevel, int damage, int manaCost) {
        super(name, cost, requiredLevel, damage, manaCost);
    }

    /**
     * Applies the spell's effect to the target monster.
     * Reduces the target's damage by 10%.
     *
     * @param target The target monster.
     * @param output The output interface for messages.
     */
    @Override
    public void applyEffect(Monster target, Output output) {
        int currentDamage = target.getDamage();
        int reduction = (int) (currentDamage * 0.1); // Reduce damage by 10%
        target.setDamage(currentDamage - reduction);
        if (output != null) output.println(target.getName() + "'s damage was reduced by " + reduction + "!");
    }

    /**
     * Gets the description of the spell's effect.
     *
     * @return The effect description.
     */
    @Override
    public String getEffectDescription() {
        return "Reduces target's damage";
    }
}
