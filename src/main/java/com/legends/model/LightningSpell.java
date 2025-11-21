package com.legends.model;

public class LightningSpell extends Spell {
    public LightningSpell(String name, int cost, int requiredLevel, int damage, int manaCost) {
        super(name, cost, requiredLevel, damage, manaCost);
    }

    @Override
    public void applyEffect(Monster target) {
        int currentDodge = target.getDodgeChance();
        int reduction = (int) (currentDodge * 0.1); // Reduce dodge chance by 10%
        target.setDodgeChance(currentDodge - reduction);
        System.out.println(target.getName() + "'s dodge chance was reduced by " + reduction + "!");
    }

    @Override
    public String getEffectDescription() {
        return "Reduces target's dodge chance";
    }
}
