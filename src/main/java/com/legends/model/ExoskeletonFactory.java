package com.legends.model;

import com.legends.ai.MonsterAI;

/**
 * Creates Exoskeleton objects
 */
public class ExoskeletonFactory implements MonsterFactory {
    public ExoskeletonFactory() {}

    @Override
    public Monster createMonster(String name, int level, int damage, int defense, int dodgeChance, MonsterAI ai){
        Exoskeleton m = new Exoskeleton(name, level, damage, defense, dodgeChance);
        m.setAI(ai);
        m.setHp(m.getLevel() * 100);
        return m;
    }
}
