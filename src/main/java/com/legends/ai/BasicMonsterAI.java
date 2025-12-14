package com.legends.ai;

import com.legends.model.*;
import com.legends.gameFiles.ValorBoard;
import com.legends.io.Output;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic strategy for this game
 */
public class BasicMonsterAI implements MonsterAI {
    /**
     * Default constructor
     */
    public BasicMonsterAI() {}

    @Override
    public void takeTurn(Monster monster, ValorBoard board, Output output) {
        if (!monster.isAlive()) return;

        // Get list of eligible heroes to attack
        List<Hero> heroes = board.getHeroes();
        List<Hero> heroesInRange = new ArrayList<>();
        for (Hero hero : heroes) {
            if (hero.isAlive() && monster.inRange(hero)) {
                heroesInRange.add(hero);
            }
        }

        // Attack if you can
        if (!heroesInRange.isEmpty()) {
            attack(monster, heroesInRange, output);
        }
        else board.moveMonster(monster, output);
    }

    /**
     * Attack the hero with the lowest HP.
     */
    private void attack(Monster monster, List<Hero> heroes, Output output) {
        Hero target = heroes.get(0);
        for (Hero h : heroes) {
            if (h.getHp() < target.getHp()) {
                target = h;
            }
        }

        int damage = calculateDamage(monster, target);
        target.takeDamage(damage);

        output.printlnRed(
                monster.getName() + " attacked " +
                        target.getName() + " for " + damage + " damage!"
        );

        if (!target.isAlive()) {
            output.printlnRed(target.getName() + " has been defeated!");
        }
    }

    private int calculateDamage(Monster monster, Hero hero) {
        double attack = monster.getDamage();
        Armor equippedArmor = hero.getEquippedArmor();
        double defense = equippedArmor != null
                ? equippedArmor.getDamageReduction()
                : 0;

        double dmg = (attack * 0.05) * (attack / (attack + defense));
        return (int) Math.max(1, dmg);
    }
}
