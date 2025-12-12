package com.legends.gameFiles;

import com.legends.model.*;
import com.legends.io.Input;
import com.legends.io.Output;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Manages a battle encounter between heroes and monsters.
 * Handles turn-based combat, actions, and win/loss conditions.
 */
public class Battle {
    private Party party;
    private List<Monster> monsters;
    private int initialMonsterCount;
    private int maxMonsterLevel;
    private Input input;
    private Output output;
    private boolean battleRunning;

    /**
     * Constructs a new Battle.
     *
     * @param party      The party of heroes.
     * @param monsters   The list of monsters to fight.
     * @param input      The input interface.
     * @param output     The output interface.
     * @param difficulty The difficulty level ("Normal" or "Hard").
     */
    public Battle(Party party, List<Monster> monsters, Input input, Output output, String difficulty) {
        this.party = party;
        this.monsters = monsters;
        this.initialMonsterCount = monsters.size();

        this.maxMonsterLevel = 0;
        for (Monster m : monsters) {
            if (m.getLevel() > maxMonsterLevel) {
                maxMonsterLevel = m.getLevel();
            }
            if (difficulty.equals("Hard")) {
                m.setDamage((int) (m.getDamage() * 1.2));
                m.setDefense((int) (m.getDefense() * 1.2));
                m.setHp((int) (m.getHp() * 1.2));
            }
        }

        this.input = input;
        this.output = output;
        this.battleRunning = true;
    }

    /**
     * Starts the battle loop.
     *
     * @return The result of the battle ("Victory" or "Defeat").
     */
    public String start() {
        output.println("\n--- Battle Started! ---");

        while (battleRunning) {
            // Check win/loss conditions
            if (areAllHeroesFainted()) {
                output.printlnRed("All heroes have fainted! Game Over.");
                battleRunning = false;

                // If all heroes fainted, exit the game gracefully
                return "Defeat";
            }
            if (monsters.isEmpty()) {
                output.printlnGreen("All monsters defeated! Victory!");
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
                    int hpRegen = (int) (h.getLevel() * 100 * 0.1);
                    int manaRegen = (int) (h.getMana() * 0.1);
                    h.setHp(h.getHp() + hpRegen);
                    h.setMana(h.getMana() + manaRegen);
                }
            }
        }
        return "";
    }

    /**
     * Displays the current status of heroes and monsters.
     */
    private void showBattleStatus() {
        output.println("Heroes:");
        for (Hero h : party.getHeroes()) {
            if (h.isAlive())
                output.println(h.toString());
            else
                output.println(h.getName() + " (Fainted)");
        }
        output.println("Monsters:");
        for (int i = 0; i < monsters.size(); i++) {
            Monster m = monsters.get(i);
            output.println((i + 1) + ". " + m.toString());
        }
    }

    /**
     * Handles the turn for a hero.
     * Allows the player to choose an action (Attack, Spell, Potion, Equipment).
     *
     * @param hero The hero taking the turn.
     */
    private void takeHeroTurn(Hero hero) {
        boolean turnTaken = false;
        while (!turnTaken) {
            output.println("\n" + hero.getName() + "'s turn.");
            output.println("1. Attack");
            output.println("2. Cast Spell");
            output.println("3. Use Potion");
            output.println("4. Change Equipment");
            output.print("Choose action: ");

            String choice = input.readLine();
            switch (choice) {
                case "1":
                    turnTaken = performAttack(hero);
                    break;
                case "2":
                    turnTaken = performCastSpell(hero);
                    break;
                case "3":
                    turnTaken = performUsePotion(hero);
                    break;
                case "4":
                    turnTaken = performChangeEquipment(hero);
                    break;
                default:
                    output.println("Invalid action. Please try again.");
            }
        }
    }

    /**
     * Performs a physical attack by the hero.
     *
     * @param hero The hero attacking.
     * @return True if the attack was performed, false if cancelled.
     */
    private boolean performAttack(Hero hero) {
        Monster target = selectMonsterTarget();
        if (target == null)
            return false;

        // Calculate damage
        double attack = hero.getStrength();
        if (hero.getMainHandWeapon() != null) {
            double weaponDamage = hero.getMainHandWeapon().getDamage();
            if (hero.isMainHandTwoHandedGrip() && hero.getMainHandWeapon().getRequiredHands() == 1) {
                weaponDamage *= 1.5; // 50% damage increase for 2-handed grip on 1-handed weapon
            }
            attack += weaponDamage;
        }

        // Apply dodge chance
        Random rand = new Random();
        double dodgeChance = target.getDodgeChance() * 0.01;

        // Reduce dodge chance based on hero dexterity
        // Assuming dexterity is in the hundreds/thousands.
        // Using a factor of 0.00025 means 400 dexterity reduces dodge chance by 10%
        // (0.1)
        double effectiveDodgeChance = dodgeChance - (hero.getDexterity() * 0.00025);
        if (effectiveDodgeChance < 0)
            effectiveDodgeChance = 0;

        if (rand.nextDouble() < effectiveDodgeChance) {
            output.printlnRed(target.getName() + " dodged the attack!");
        } else {
            int actualDamage = calculateDamage(attack, target.getDefense());
            target.takeDamage(actualDamage);
            output.printlnGreen(hero.getName() + " dealt " + actualDamage + " damage to " + target.getName());

            if (!target.isAlive()) {
                output.printlnGreen(target.getName() + " has been defeated!");
                monsters.remove(target);
            }
        }
        return true;
    }

    /**
     * Performs a spell cast by the hero.
     *
     * @param hero The hero casting the spell.
     * @return True if the spell was cast, false if cancelled.
     */
    private boolean performCastSpell(Hero hero) {
        // Filter spells from inventory
        List<Spell> spells = new ArrayList<>();
        for (Item i : hero.getInventory()) {
            if (i instanceof Spell)
                spells.add((Spell) i);
        }

        if (spells.isEmpty()) {
            output.println("No spells available.");
            return false;
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
            return false;
        }

        if (idx < 0 || idx >= spells.size())
            return false;

        Spell spell = spells.get(idx);
        if (hero.getMana() < spell.getManaCost()) {
            output.printlnRed("Not enough mana!");
            return false;
        }

        Monster target = selectMonsterTarget();
        if (target == null)
            return false;

        hero.setMana(hero.getMana() - spell.getManaCost());

        double spellDamage = spell.getDamage() + (hero.getDexterity() / 10000.0 * spell.getDamage());
        // Apply simple scaling for spells (treating as pure damage)
        int damage = (int) (spellDamage);

        target.takeDamage(damage);
        output.printlnGreen(hero.getName() + " cast " + spell.getName() + " on " + target.getName() + " for " + damage
                + " damage.");
        spell.applyEffect(target, output); // Apply side effects based on spell type

        if (!target.isAlive()) {
            output.printlnGreen(target.getName() + " has been defeated!");
            monsters.remove(target);
        }
        return true;
    }

    /**
     * Performs a potion use by the hero.
     *
     * @param hero The hero using the potion.
     * @return True if the potion was used, false if cancelled.
     */
    private boolean performUsePotion(Hero hero) {
        List<Potion> potions = new ArrayList<>();
        for (Item i : hero.getInventory()) {
            if (i instanceof Potion)
                potions.add((Potion) i);
        }

        if (potions.isEmpty()) {
            output.println("No potions available.");
            return false;
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
            return false;
        }

        if (idx < 0 || idx >= potions.size())
            return false;

        Potion potion = potions.get(idx);

        Hero target = selectHeroTarget();
        if (target == null)
            return false;

        target.applyPotion(potion);
        hero.removeItem(potion);
        output.printlnGreen(hero.getName() + " used " + potion.getName() + " on " + target.getName());
        return true;
    }

    /**
     * Selects a hero target for an action.
     *
     * @return The selected hero, or null if cancelled.
     */
    private Hero selectHeroTarget() {
        List<Hero> heroes = party.getHeroes();
        if (heroes.size() == 1) {
            return heroes.get(0);
        }

        output.println("Select Hero Target:");
        for (int i = 0; i < heroes.size(); i++) {
            Hero h = heroes.get(i);
            String status = h.isAlive() ? "HP: " + h.getHp() : "Fainted";
            output.println((i + 1) + ". " + h.getName() + " (" + status + ")");
        }
        try {
            int idx = Integer.parseInt(input.readLine()) - 1;
            if (idx >= 0 && idx < heroes.size()) {
                return heroes.get(idx);
            }
        } catch (NumberFormatException e) {
        }
        output.println("Invalid target.");
        return null;
    }

    /**
     * Performs an equipment change for the hero.
     *
     * @param hero The hero changing equipment.
     * @return True if equipment was changed, false if cancelled.
     */
    private boolean performChangeEquipment(Hero hero) {
        List<Item> equipment = new ArrayList<>();
        for (Item i : hero.getInventory()) {
            if (i instanceof Weapon || i instanceof Armor) {
                equipment.add(i);
            }
        }

        if (equipment.isEmpty()) {
            output.println("No equipment available in inventory.");
            return false;
        }

        output.println("Current Equipment:");
        output.println(
                "Main Hand: " + (hero.getMainHandWeapon() != null ? hero.getMainHandWeapon().getName() : "None"));
        output.println("Off Hand: " + (hero.getOffHandWeapon() != null ? hero.getOffHandWeapon().getName() : "None"));
        output.println("Armor: " + (hero.getEquippedArmor() != null ? hero.getEquippedArmor().getName() : "None"));

        output.println("\nSelect Equipment to Equip:");
        for (int i = 0; i < equipment.size(); i++) {
            Item item = equipment.get(i);
            String details = item.getName();
            if (item instanceof Weapon) {
                details += " (Dmg: " + ((Weapon) item).getDamage() + ", Hands: " + ((Weapon) item).getRequiredHands()
                        + ")";
            } else if (item instanceof Armor) {
                details += " (Def: " + ((Armor) item).getDamageReduction() + ")";
            }
            output.println((i + 1) + ". " + details);
        }
        output.println((equipment.size() + 1) + ". Cancel");

        int idx = -1;
        try {
            idx = Integer.parseInt(input.readLine()) - 1;
        } catch (NumberFormatException e) {
            output.println("Invalid input.");
            return false;
        }

        if (idx < 0 || idx >= equipment.size())
            return false;

        Item selectedItem = equipment.get(idx);
        if (selectedItem instanceof Weapon) {
            Weapon weapon = (Weapon) selectedItem;
            if (weapon.getRequiredHands() == 1) {
                output.println("Equip to: 1. Main Hand  2. Off Hand  3. Main Hand (2-Handed Grip)");
                String handChoice = input.readLine();
                if (handChoice.equals("1")) {
                    hero.equipMainHand(weapon, false);
                    output.println("Equipped " + weapon.getName() + " to Main Hand.");
                    return true;
                } else if (handChoice.equals("2")) {
                    if (hero.equipOffHand(weapon, output)) {
                        output.println("Equipped " + weapon.getName() + " to Off Hand.");
                        return true;
                    }
                    return false;
                } else if (handChoice.equals("3")) {
                    hero.equipMainHand(weapon, true);
                    output.println("Equipped " + weapon.getName() + " to Main Hand (2-Handed Grip).");
                    return true;
                } else {
                    output.println("Invalid choice.");
                    return false;
                }
            } else {
                // 2-handed
                hero.equipMainHand(weapon);
                output.println("Equipped " + weapon.getName() + " (2-Handed).");
                return true;
            }
        } else if (selectedItem instanceof Armor) {
            hero.equipArmor((Armor) selectedItem);
            output.println("Equipped " + selectedItem.getName());
            return true;
        }

        return false;
    }

    /**
     * Selects a monster target for an attack.
     *
     * @return The selected monster, or null if cancelled.
     */
    private Monster selectMonsterTarget() {
        if (monsters.size() == 1) {
            return monsters.get(0);
        }

        output.println("Select Target:");
        for (int i = 0; i < monsters.size(); i++) {
            output.println((i + 1) + ". " + monsters.get(i).getName() + " (HP: " + monsters.get(i).getHp() + ")");
        }
        try {
            int idx = Integer.parseInt(input.readLine()) - 1;
            if (idx >= 0 && idx < monsters.size()) {
                return monsters.get(idx);
            }
        } catch (NumberFormatException e) {
        }
        output.println("Invalid target.");
        return null;
    }

    /**
     * Handles the turn for a monster.
     * The monster attacks a hero, prioritizing those with lower HP.
     *
     * @param monster The monster taking the turn.
     */
    private void takeMonsterTurn(Monster monster) {
        // AI: Attack hero with lower HP with priority
        List<Hero> aliveHeroes = new ArrayList<>();
        for (Hero h : party.getHeroes()) {
            if (h.isAlive())
                aliveHeroes.add(h);
        }

        if (aliveHeroes.isEmpty())
            return;

        Hero target = selectWeightedTarget(aliveHeroes);

        Random rand = new Random();
        // Dodge calculation
        // PDF mentions 0.002, but I changed to 0.01 for better gameplay balance
        if (rand.nextInt(100) < (target.getAgility() * 0.01)) { // Agility based dodge
            output.printlnGreen(target.getName() + " dodged " + monster.getName() + "'s attack!");
        } else {
            double attack = monster.getDamage();
            double defense = 0;
            if (target.getEquippedArmor() != null) {
                defense = target.getEquippedArmor().getDamageReduction();
            }

            int damage = calculateDamage(attack, defense);

            target.takeDamage(damage);
            output.printlnRed(monster.getName() + " attacked " + target.getName() + " for " + damage + " damage.");
        }
    }

    /**
     * Calculates the damage dealt based on attack and defense.
     *
     * @param attack  The attack value.
     * @param defense The defense value.
     * @return The calculated damage.
     */
    private int calculateDamage(double attack, double defense) {
        // Formula: (Attack * 0.05) * (Attack / (Attack + Defense))
        // This scales the raw damage down and applies a mitigation factor based on
        // defense.
        if (attack + defense == 0)
            return 0;
        double damage = (attack * 0.05) * (attack / (attack + defense));
        return (int) Math.max(1, damage); // Minimum 1 damage
    }

    /**
     * Selects a target hero based on weighted probability (lower HP = higher
     * chance).
     *
     * @param heroes The list of potential hero targets.
     * @return The selected hero.
     */
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

    /**
     * Checks if all heroes in the party have fainted.
     *
     * @return True if all heroes are fainted, false otherwise.
     */
    private boolean areAllHeroesFainted() {
        for (Hero h : party.getHeroes()) {
            if (h.isAlive())
                return false;
        }
        return true;
    }

    /**
     * Distributes rewards (XP and Gold) to the heroes after a victory.
     */
    private void distributeRewards() {
        int totalXp = initialMonsterCount * maxMonsterLevel * 2;
        int totalGold = initialMonsterCount * maxMonsterLevel * 100;

        for (Hero h : party.getHeroes()) {
            if (h.isAlive()) {
                h.setMoney(h.getMoney() + totalGold);
                h.gainExperience(totalXp, output);
                output.printlnGreen(h.getName() + " gained " + totalGold + " gold and " + totalXp + " XP.");
            } else {
                // Revive fainted heroes with 50% HP
                h.setHp(h.getLevel() * 50);
                output.printlnGreen(h.getName() + " has been revived with " + h.getHp() + " HP.");
            }
        }
    }
}
