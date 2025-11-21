package com.legends.game;

import com.legends.model.*;
import com.legends.io.Input;
import com.legends.io.Output;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Battle {
    private Party party;
    private List<Monster> monsters;
    private Input input;
    private Output output;
    private boolean battleRunning;

    public Battle(Party party, List<Monster> monsters, Input input, Output output) {
        this.party = party;
        this.monsters = monsters;
        this.input = input;
        this.output = output;
        this.battleRunning = true;
    }

    public String start() {
        output.println("\n--- Battle Started! ---");
        
        while (battleRunning) {
            // Check win/loss conditions
            if (areAllHeroesFainted()) {
                output.println("All heroes have fainted! Game Over.");
                battleRunning = false;

                // If all heroes fainted, exit the game gracefully
                return "Defeat";
            }
            if (monsters.isEmpty()) {
                output.println("All monsters defeated! Victory!");
                distributeRewards();
                battleRunning = false;
                return "Victory";
            }

            output.println("\n--- New Round ---");
            showBattleStatus();

            // Heroes Turn
            for (Hero hero : party.getHeroes()) {
                if (hero.isAlive() && !monsters.isEmpty()) {
                    takeHeroTurn(hero);
                }
            }

            // Monsters Turn
            for (Monster monster : monsters) {
                if (monster.isAlive() && !areAllHeroesFainted()) {
                    takeMonsterTurn(monster);
                }
            }
            
            // Cleanup dead monsters
            monsters.removeIf(m -> !m.isAlive());
            
            // Regenerate some HP/Mana for heroes
            for (Hero h : party.getHeroes()) {
                if (h.isAlive()) {
                    h.setHp((int)(h.getHp() * 1.1)); // Regenerate 10% HP
                    h.setMana((int)(h.getMana() * 1.1)); // Regenerate 10% Mana
                }
            }
        }
        return "";
    }

    private void showBattleStatus() {
        output.println("Heroes:");
        for (Hero h : party.getHeroes()) {
            if (h.isAlive()) output.println(h.toString());
            else output.println(h.getName() + " (Fainted)");
        }
        output.println("Monsters:");
        for (int i = 0; i < monsters.size(); i++) {
            Monster m = monsters.get(i);
            output.println((i + 1) + ". " + m.toString());
        }
    }

    private void takeHeroTurn(Hero hero) {
        output.println("\n" + hero.getName() + "'s turn.");
        output.println("1. Attack");
        output.println("2. Cast Spell");
        output.println("3. Use Potion");
        output.println("4. Change Equipment");
        output.print("Choose action: ");

        String choice = input.readLine();
        switch (choice) {
            case "1":
                performAttack(hero);
                break;
            case "2":
                performCastSpell(hero);
                break;
            case "3":
                performUsePotion(hero);
                break;
            case "4":
                performChangeEquipment(hero);
                break;
            default:
                output.println("Invalid action. Turn skipped.");
        }
    }

    private void performAttack(Hero hero) {
        Monster target = selectMonsterTarget();
        if (target == null) return;

        // Calculate damage
        int damage = hero.getStrength();
        if (hero.getMainHandWeapon() != null) {
            damage += hero.getMainHandWeapon().getDamage();
        }
        
        // Apply scaling
        damage = (int) (damage * 0.05);

        // Apply dodge chance
        Random rand = new Random();
        if (rand.nextInt(100) < target.getDodgeChance()) {
            output.println(target.getName() + " dodged the attack!");
        } else {
            int defense = (int) (target.getDefense() * 0.05);
            int actualDamage = Math.max(0, damage - defense); // Simple defense
            target.takeDamage(actualDamage);
            output.println(hero.getName() + " dealt " + actualDamage + " damage to " + target.getName());
        }
    }

    private void performCastSpell(Hero hero) {
        // Filter spells from inventory
        List<Spell> spells = new ArrayList<>();
        for (Item i : hero.getInventory()) {
            if (i instanceof Spell) spells.add((Spell) i);
        }

        if (spells.isEmpty()) {
            output.println("No spells available.");
            return;
        }

        output.println("Select Spell:");
        for (int i = 0; i < spells.size(); i++) {
            Spell s = spells.get(i);
            output.println((i + 1) + ". " + s.getName() + " (MP: " + s.getManaCost() + ", Dmg: " + s.getDamage() + ")");
        }
        output.println((spells.size() + 1) + ". Cancel");

        int idx = -1;
        try {
            idx = Integer.parseInt(input.readLine()) - 1;
        } catch (NumberFormatException e) {
            output.println("Invalid input.");
            return;
        }

        if (idx < 0 || idx >= spells.size()) return;

        Spell spell = spells.get(idx);
        if (hero.getMana() < spell.getManaCost()) {
            output.println("Not enough mana!");
            return;
        }

        Monster target = selectMonsterTarget();
        if (target == null) return;

        hero.setMana(hero.getMana() - spell.getManaCost());
        int damage = spell.getDamage() + (hero.getDexterity() / 10); 
        
        // Apply scaling
        damage = (int) (damage * 0.05);

        target.takeDamage(damage);
        output.println(hero.getName() + " cast " + spell.getName() + " on " + target.getName() + " for " + damage + " damage.");
        spell.applyEffect(target, output); // Apply side effects based on spell type
    }

    private void performUsePotion(Hero hero) {
        List<Potion> potions = new ArrayList<>();
        for (Item i : hero.getInventory()) {
            if (i instanceof Potion) potions.add((Potion) i);
        }

        if (potions.isEmpty()) {
            output.println("No potions available.");
            return;
        }

        output.println("Select Potion:");
        for (int i = 0; i < potions.size(); i++) {
            Potion p = potions.get(i);
            output.println((i + 1) + ". " + p.getName());
        }
        output.println((potions.size() + 1) + ". Cancel");

        int idx = -1;
        try {
            idx = Integer.parseInt(input.readLine()) - 1;
        } catch (NumberFormatException e) {
            output.println("Invalid input.");
            return;
        }

        if (idx < 0 || idx >= potions.size()) return;

        Potion potion = potions.get(idx);
        hero.usePotion(potion);
        output.println(hero.getName() + " used " + potion.getName());
    }

    private void performChangeEquipment(Hero hero) {
        // Implement equipment change logic here
        output.println("Equipment change not fully implemented in battle yet.");
    }

    private Monster selectMonsterTarget() {
        output.println("Select Target:");
        for (int i = 0; i < monsters.size(); i++) {
            output.println((i + 1) + ". " + monsters.get(i).getName() + " (HP: " + monsters.get(i).getHp() + ")");
        }
        try {
            int idx = Integer.parseInt(input.readLine()) - 1;
            if (idx >= 0 && idx < monsters.size()) {
                return monsters.get(idx);
            }
        } catch (NumberFormatException e) {}
        output.println("Invalid target.");
        return null;
    }

    private void takeMonsterTurn(Monster monster) {
        // AI: Attack hero with lower HP priority
        List<Hero> aliveHeroes = new ArrayList<>();
        for (Hero h : party.getHeroes()) {
            if (h.isAlive()) aliveHeroes.add(h);
        }

        if (aliveHeroes.isEmpty()) return;

        Hero target = selectWeightedTarget(aliveHeroes);

        Random rand = new Random();
        // Dodge calculation
        if (rand.nextInt(100) < (target.getAgility() * 0.002)) { // Agility based dodge
             output.println(target.getName() + " dodged " + monster.getName() + "'s attack!");
        } else {
            int damage = (int) (monster.getDamage() * 0.05);
            int defense = 0;
            if (target.getEquippedArmor() != null) {
                defense = (int) (target.getEquippedArmor().getDamageReduction() * 0.05);
            }
            damage = Math.max(0, damage - defense);
            
            target.takeDamage(damage);
            output.println(monster.getName() + " attacked " + target.getName() + " for " + damage + " damage.");
        }
    }

    private Hero selectWeightedTarget(List<Hero> heroes) {
        double totalWeight = 0;
        double[] weights = new double[heroes.size()];

        for (int i = 0; i < heroes.size(); i++) {
            // Weight is inversely proportional to HP. Adding a small epsilon to avoid division by zero
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

    private boolean areAllHeroesFainted() {
        for (Hero h : party.getHeroes()) {
            if (h.isAlive()) return false;
        }
        return true;
    }

    private void distributeRewards() {
        int totalXp = monsters.size() * 100; // Simplified XP
        int totalGold = monsters.size() * 50; // Simplified Gold

        for (Hero h : party.getHeroes()) {
            if (h.isAlive()) {
                h.setMoney(h.getMoney() + totalGold);
                h.gainExperience(totalXp, output);
            } else {
                // Revive fainted heroes with 50% HP
                h.setHp(h.getLevel() * 50); 
                output.println(h.getName() + " has been revived.");
            }
        }
        output.println("Party gained " + totalGold + " gold and " + totalXp + " XP each.");
    }
}
