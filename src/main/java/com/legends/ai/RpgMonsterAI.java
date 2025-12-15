package com.legends.ai;

import com.legends.model.*;
import com.legends.board.ValorBoard;
import com.legends.io.Output;
import com.legends.utils.audio.SoundManager;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * AI strategy for the Monsters and Heroes game.
 * Attacks heroes based on weighted probability (lower HP = higher chance).
 */
public class RpgMonsterAI implements MonsterAI {
    private static final long serialVersionUID = 1L;

    public RpgMonsterAI() {}

    @Override
    public void takeTurn(Monster monster, ValorBoard board, Output output) {
        // Not used in Monsters and Heroes game
    }

    @Override
    public void takeBattleTurn(Monster monster, List<Hero> heroes, Output output) {
        // AI: Attack hero with lower HP with priority
        List<Hero> aliveHeroes = new ArrayList<>();
        for (Hero h : heroes) {
            if (h.isAlive())
                aliveHeroes.add(h);
        }

        if (aliveHeroes.isEmpty())
            return;

        Hero target = selectWeightedTarget(aliveHeroes);

        Random rand = new Random();
        // Dodge calculation
        if (rand.nextInt(100) < (target.getAgility() * 0.01)) { // Agility based dodge
            SoundManager.getInstance().playDodgeSound();
            output.printlnGreen(target.getName() + " dodged " + monster.getName() + "'s attack!");
        } else {
            SoundManager.getInstance().playAttackSound();
            double attack = monster.getDamage();
            double defense = 0;
            if (target.getEquippedArmor() != null) {
                defense = target.getEquippedArmor().getDamageReduction();
            }

            int damage = calculateDamage(attack, defense);

            target.takeDamage(damage);
            SoundManager.getInstance().playDamageSound();
            output.printlnRed(monster.getName() + " attacked " + target.getName() + " for " + damage + " damage.");

            if (!target.isAlive()) {
                SoundManager.getInstance().playHeroDeathSound();
            }
        }
    }

    private Hero selectWeightedTarget(List<Hero> heroes) {
        double totalWeight = 0;
        double[] weights = new double[heroes.size()];

        for (int i = 0; i < heroes.size(); i++) {
            // Weight is inversely proportional to HP. Adding a small epsilon to avoid
            // division by zero
            weights[i] = 1.0 / Math.max(1, heroes.get(i).getHp());
            totalWeight += weights[i];
        }

        Random rand = new Random();
        double value = rand.nextDouble() * totalWeight;

        for (int i = 0; i < heroes.size(); i++) {
            value -= weights[i];
            if (value <= 0) {
                return heroes.get(i);
            }
        }

        return heroes.get(heroes.size() - 1);
    }

    private int calculateDamage(double attack, double defense) {
        // Formula: (Attack * 0.05) * (Attack / (Attack + Defense))
        double dmg = (attack * 0.05) * (attack / (attack + defense));
        return (int) Math.max(1, dmg);
    }
}
