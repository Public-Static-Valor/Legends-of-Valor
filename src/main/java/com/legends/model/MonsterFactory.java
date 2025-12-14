package com.legends.model;

import com.legends.ai.MonsterAI;

/**
 * We will implement the MonsterFactory to create different types of monsters
 */
public interface MonsterFactory {
    public Monster createMonster(String name, int level, int damage, int defense, int dodgeChance, MonsterAI ai);
}
