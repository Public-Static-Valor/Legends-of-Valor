package com.legends.model;

import com.legends.ai.MonsterAI;

/**
 * Creates dragon objects
 */
public class DragonFactory implements MonsterFactory {
    public DragonFactory() {}

    @Override
    public Monster createMonster(String name, int level, int damage, int defense, int dodgeChance, MonsterAI ai){
        Dragon m = new Dragon(name, level, damage, defense, dodgeChance);
        m.setAI(ai);
        m.setHp(m.getLevel() * 100);
        return m;
    }
}
