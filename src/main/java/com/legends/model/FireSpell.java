package com.legends.model;

public class FireSpell extends Spell {
    public FireSpell(String name, int cost, int requiredLevel, int damage, int manaCost) {
        super(name, cost, requiredLevel, damage, manaCost);
    }

    @Override
    public void applyEffect(Monster target) {
        int currentDefense = target.getDefense();
        int reduction = (int) (currentDefense * 0.1); // Reduce defense by 10%
        target.setDefense(currentDefense - reduction);
        System.out.println(target.getName() + "'s defense was reduced by " + reduction + "!");
    }

    @Override
    public String getEffectDescription() {
        return "Reduces target's defense";
    }
}
