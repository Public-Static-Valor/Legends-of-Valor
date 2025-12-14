package com.legends.model;

/**
 * Default concrete factory that creates the 'real' game item objects.
 */
public class DefaultItemFactory implements ItemFactory {

    @Override
    public Weapon createWeapon(String name, int cost, int requiredLevel, int damage, int requiredHands) {
        return new Weapon(name, cost, requiredLevel, damage, requiredHands);
    }

    @Override
    public Armor createArmor(String name, int cost, int requiredLevel, int damageReduction) {
        return new Armor(name, cost, requiredLevel, damageReduction);
    }

    @Override
    public Potion createPotion(String name, int cost, int requiredLevel, int attributeIncrease, String attributeAffected) {
        return new Potion(name, cost, requiredLevel, attributeIncrease, attributeAffected);
    }

    @Override
    public Spell createSpell(String name, int cost, int requiredLevel, int damage, int manaCost, SpellType type) {
        switch (type) {
            case FIRE:
                return new FireSpell(name, cost, requiredLevel, damage, manaCost);
            case ICE:
                return new IceSpell(name, cost, requiredLevel, damage, manaCost);
            case LIGHTNING:
                return new LightningSpell(name, cost, requiredLevel, damage, manaCost);
            default:
                throw new IllegalArgumentException("Unsupported spell type: " + type);
        }
    }
}
