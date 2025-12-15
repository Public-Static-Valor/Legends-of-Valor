package com.legends.ai;

import com.legends.model.*;
import com.legends.board.ValorBoard;
import com.legends.io.Output;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic strategy for this game
 */
public class ValorMonsterAI implements MonsterAI {
    /**
     * Default constructor
     */
    public ValorMonsterAI() {
    }

    @Override
    public void takeTurn(Monster monster, ValorBoard board, Output output) {
        if (!monster.isAlive())
            return;

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
        } else
            board.moveMonster(monster, output);
    }

    @Override
    public void takeBattleTurn(Monster monster, List<Hero> heroes, Output output) {
        if (!monster.isAlive()) return;
        
        // Filter alive heroes
        List<Hero> aliveHeroes = new ArrayList<>();
        for (Hero h : heroes) {
            if (h.isAlive()) aliveHeroes.add(h);
        }
        
        if (!aliveHeroes.isEmpty()) {
            attack(monster, aliveHeroes, output);
        }
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

        // Attack logic is now handled by Entity.attack()
        monster.attack(target, output);
    }
}
