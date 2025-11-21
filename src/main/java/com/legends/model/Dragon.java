package com.legends.model;

public class Dragon extends Monster {
    public Dragon(String name, int level, int damage, int defense, int dodgeChance) {
        // Dragons have increased base damage
        super(name, level, (int)(damage * 1.1), defense, dodgeChance);
    }
}
