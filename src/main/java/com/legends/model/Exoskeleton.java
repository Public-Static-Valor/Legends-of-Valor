package com.legends.model;

public class Exoskeleton extends Monster {
    public Exoskeleton(String name, int level, int damage, int defense, int dodgeChance) {
        // Exoskeletons have increased defense
        super(name, level, damage, (int)(defense * 1.1), dodgeChance);
    }
}
