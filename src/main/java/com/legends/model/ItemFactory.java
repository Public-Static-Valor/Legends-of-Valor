package com.legends.model;

/**
 * Abstract factory for creating items.
 * This centralizes creation logic for each item family.
 */
public interface ItemFactory {
    Weapon createWeapon(String name, int cost, int requiredLevel, int damage, int requiredHands);
    Armor  createArmor(String name, int cost, int requiredLevel, int damageReduction);
    Potion createPotion(String name, int cost, int requiredLevel, int attributeIncrease, String attributeAffected);
    Spell  createSpell(String name, int cost, int requiredLevel, int damage, int manaCost, SpellType type);
}
