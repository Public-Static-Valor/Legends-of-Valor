package com.legends.model;

public class Spirit extends Monster {
    public Spirit(String name, int level, int damage, int defense, int dodgeChance) {
        // Spirits have increased dodge ability
        super(name, level, damage, defense, (int)(dodgeChance * 1.1));
    }
}
