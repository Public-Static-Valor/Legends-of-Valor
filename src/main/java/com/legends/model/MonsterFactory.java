package com.legends.model;

import com.legends.ai.MonsterAI;
import java.io.Serializable;

/**
 * We will implement the MonsterFactory to create different types of monsters
 */
public interface MonsterFactory extends Serializable {
    public static final long serialVersionUID = 1L;

    public Monster createMonster(String name, int level, int damage, int defense, int dodgeChance, MonsterAI ai);
}
