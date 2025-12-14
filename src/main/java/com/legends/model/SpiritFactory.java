package com.legends.model;

import com.legends.ai.MonsterAI;

/**
 * Creates Spirit objects
 */
public class SpiritFactory implements MonsterFactory {
    public SpiritFactory() {}

    @Override
    public Monster createMonster(String name, int level, int damage, int defense, int dodgeChance, MonsterAI ai){
        Spirit m = new Spirit(name, level, damage, defense, dodgeChance);
        m.setAI(ai);
        m.setHp(m.getLevel() * 100);
        return m;
    }
}
