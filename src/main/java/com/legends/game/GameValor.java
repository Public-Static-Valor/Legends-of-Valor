package com.legends.game;

import com.legends.ai.ValorMonsterAI;
import com.legends.board.ValorBoard;
import com.legends.board.tiles.BushTile;
import com.legends.board.tiles.CaveTile;
import com.legends.board.tiles.KoulouTile;
import com.legends.board.tiles.NexusTile;
import com.legends.board.tiles.ObstacleTile;
import com.legends.board.tiles.Tile;
import com.legends.model.*;
import com.legends.ui.AsciiArt;
import com.legends.ui.StyledOutput;
import com.legends.utils.audio.SoundManager;
import com.legends.io.Input;
import com.legends.io.Output;
import com.legends.market.Market;
import com.legends.market.StaticMarket;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The main controller for Legends of Valor game.
 * Manages the lane-based MOBA-style gameplay.
 */
public class GameValor extends RPGGame {
    private static final long serialVersionUID = 3L;
    private static final int MONSTER_SPAWN_INTERVAL = 8; // Spawn monsters every 8 rounds

    private List<Hero> selectedHeroes;
    private List<Monster> activeMonsters;
    private ValorBoard board;
    private int roundNumber;
    // Shadowed fields removed to use parent's input/output
    private int monsterSpawnCounter = 0;

    /**
     * Constructs a new GameValor instance.
     *
     * @param input  The input interface.
     * @param output The output interface.
     */
    public GameValor(Input input, Output output) {
        super(input, output);
        this.selectedHeroes = new ArrayList<>();
        this.activeMonsters = new ArrayList<>();
        this.roundNumber = 0;
    }

    @Override
    protected String getSaveFileName() {
        return "ValorSave.ser";
    }

    @Override
    protected String getWelcomeMessage() {
        return AsciiArt.getLegendsOfValorTitle();
    }

    /**
     * Starts a new game session.
     */
    protected void startGame() {
        resetGame();
        selectHeroes();
        assignHeroesToLanes();
        spawnInitialMonsters();
        gameLoop();
    }

    /**
     * Resets the game state for a new session.
     */
    protected void resetGame() {
        selectedHeroes.clear();
        activeMonsters.clear();
        init();
        board = new ValorBoard();
        // Initialize markets
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Tile t = board.getTileAt(x, y);
                if (t instanceof NexusTile && ((NexusTile) t).isHeroNexus()) {
                    ((NexusTile) t).setMarket(new StaticMarket(items));
                }
            }
        }
        roundNumber = 0;
    }

    /**
     * Displays the game instructions.
     */
    protected void showInstructions() {
        output.println("\n=== How to Play Legends of Valor ===");
        output.println("Goal: Get any hero to the monsters' Nexus (top row) to win!");
        output.println("Lose if any monster reaches the heroes' Nexus (bottom row).");
        output.println("");
        output.println("Game Setup:");
        output.println("- 8x8 board with 3 lanes (left, middle, right)");
        output.println("- Each lane has 2 columns separated by walls");
        output.println("- Heroes start at the bottom Nexus");
        output.println("- Monsters spawn at the top Nexus");
        output.println("");
        output.println("Actions per turn:");
        output.println("  W = move up");
        output.println("  A = move left");
        output.println("  S = move down");
        output.println("  D = move right");
        output.println("  T = teleport to another lane");
        output.println("  R = recall to your Nexus");
        output.println("  1 = attack a nearby monster");
        output.println("  2 = cast a spell");
        output.println("  3 = use a potion");
        output.println("  4 = change weapon/armor");
        output.println("  5 = destroy obstacle (if adjacent)");
        output.println("  M = enter market (only at heroes' Nexus)");
        output.println("  I = show info");
        output.println("  K = save game");
        output.println("  Q = quit (prompts to save if unsaved)");
        output.println("");
        output.println("Special Tiles:");
        output.println("  N (Yellow) = Nexus (heroes' Nexus is also a market)");
        output.println("  I (Blue) = Inaccessible wall");
        output.println("  B (Green) = Bush (increases Dexterity +10%)");
        output.println("  C (Cyan) = Cave (increases Agility +10%)");
        output.println("  K (Magenta) = Koulou (increases Strength +10%)");
        output.println("  O (Red) = Obstacle (can be destroyed)");
        output.println("  P = Plain tile");
        output.println("");
        output.println("Combat:");
        output.println("- Attack range: current space and all neighboring spaces (side + diagonal)");
        output.println("- Heroes cannot move past monsters without killing them");
        output.println("- Monsters move down one space per turn if not attacking");
        output.println("- New monsters spawn every " + MONSTER_SPAWN_INTERVAL + " rounds");
        output.println("- Monsters have unique IDs (e.g., Spirit 1) for easier identification");
        output.println("- Single targets are automatically selected for attacks and spells");
        output.println("");
        output.println("Tips:");
        output.println("- Use terrain bonuses strategically");
        output.println("- Block monsters with heroes to prevent them from advancing");
        output.println("- Teleport between lanes to help teammates");
        output.println("- Shop at heroes' Nexus between rounds");
        output.println("====================");
    }

    /**
     * Main game loop handling rounds of play.
     */
    protected void gameLoop() {
        gameRunning = true;

        while (gameRunning) {
            roundNumber++;
            styledOutput.printRoundStart(roundNumber);

            // Display board
            board.printBoard(output);

            // Check victory conditions
            if (checkVictory()) {
                break;
            }

            // Spawn new monsters every N rounds
            if (roundNumber % MONSTER_SPAWN_INTERVAL == 0) {
                spawnMonsters();
                output.println("New monsters have appeared!");
                board.printBoard(output);
            }

            // Heroes' turn
            heroesTurn();

            if (!gameRunning)
                break;

            // Check victory again after heroes move
            if (checkVictory()) {
                break;
            }

            // Monsters' turn
            monstersTurn();

            // Check victory after monsters move
            if (checkVictory()) {
                break;
            }

            // End of round effects
            endOfRoundEffects();
        }
    }

    /**
     * Handles the heroes' turn.
     */
    private void heroesTurn() {
        output.println("\n--- HEROES' TURN ---");

        for (Hero hero : selectedHeroes) {
            if (!gameRunning)
                break;
            if (!hero.isAlive()) {
                continue;
            }

            boolean actionTaken = false;
            while (!actionTaken && gameRunning) {
                output.println("\n" + hero.getName() + "'s turn (Lane " + hero.getLane() +
                        ", Position: " + hero.getX() + "," + hero.getY() + ")");

                String hpBar = com.legends.io.ConsoleOutput.createProgressBar(hero.getHp(), hero.getLevel() * 100,
                        com.legends.io.ConsoleOutput.ANSI_RED);
                String manaBar = com.legends.io.ConsoleOutput.createProgressBar(hero.getMana(), hero.getMaxMana(),
                        com.legends.io.ConsoleOutput.ANSI_BLUE);
                output.println("HP: " + hpBar + " | MP: " + manaBar + " | Gold: " + hero.getMoney());

                output.println(
                        "Actions: W/A/S/D=Move | T=Teleport | R=Recall | 1=Attack | 2=Spell | 3=Potion | 4=Equipment | 5=Destroy Obstacle | M=Market | I=Info | K=Save | Q=Quit");
                output.print("Choose action: ");

                String action = "";
                try {
                    action = input.readLine().toUpperCase();
                } catch (QuitGameException e) {
                    action = "Q";
                }

                switch (action) {
                    case "W":
                    case "A":
                    case "S":
                    case "D":
                        actionTaken = handleHeroMove(hero, action);
                        break;
                    case "T":
                        actionTaken = handleTeleport(hero);
                        break;
                    case "R":
                        actionTaken = handleRecall(hero);
                        break;
                    case "1":
                        actionTaken = handleHeroAttack(hero);
                        break;
                    case "2":
                        actionTaken = handleHeroCastSpell(hero);
                        break;
                    case "3":
                        actionTaken = handleHeroUsePotion(hero);
                        break;
                    case "4":
                        actionTaken = handleHeroChangeEquipment(hero);
                        break;
                    case "5":
                        actionTaken = handleDestroyObstacle(hero);
                        break;
                    case "M":
                        handleMarket(hero);
                        // Market doesn't consume a turn
                        break;
                    case "I":
                        showInfoMenu();
                        // Info doesn't consume a turn
                        break;
                    case "K":
                        saveGame();
                        // Save doesn't consume a turn
                        break;
                    case "Q":
                        if (confirmQuit()) {
                            gameRunning = false;
                            actionTaken = true;
                        }
                        break;
                    default:
                        output.println("Invalid action.");
                }
            }

            // Print board after each hero's turn for better visualization
            if (gameRunning) {
                board.printBoard(output);
            }

            // Check for victory immediately after each hero's turn
            if (checkVictory()) {
                break;
            }
        }
    }

    /**
     * Handles hero movement.
     */
    private boolean handleHeroMove(Hero hero, String direction) {
        int newX = hero.getX();
        int newY = hero.getY();

        switch (direction) {
            case "W":
                newY--;
                break;
            case "S":
                newY++;
                break;
            case "A":
                newX--;
                break;
            case "D":
                newX++;
                break;
        }

        // Remove terrain bonus from old tile
        removeTerrainBonus(hero);

        if (board.moveHero(hero, newX, newY, output)) {
            // Apply terrain bonus from new tile
            applyTerrainBonus(hero);
            styledOutput.printMove(hero.getName(), newX, newY);
            return true;
        }

        // Re-apply terrain bonus if move failed
        applyTerrainBonus(hero);
        return false;
    }

    /**
     * Handles hero teleportation to another lane.
     */
    private boolean handleTeleport(Hero hero) {
        output.println("\nSelect hero to teleport to:");
        List<Hero> otherHeroes = new ArrayList<>();
        for (Hero h : selectedHeroes) {
            if (h != hero && h.isAlive() && h.getLane() != hero.getLane()) {
                otherHeroes.add(h);
                output.println((otherHeroes.size()) + ". " + h.getName() +
                        " (Lane " + h.getLane() + ", Position: " + h.getX() + "," + h.getY() + ")");
            }
        }

        if (otherHeroes.isEmpty()) {
            output.println("No heroes in other lanes to teleport to!");
            return false;
        }

        output.print("Choose hero (or 0 to cancel): ");
        try {
            int choice = Integer.parseInt(input.readLine());
            if (choice == 0)
                return false;
            if (choice < 1 || choice > otherHeroes.size()) {
                output.println("Invalid choice.");
                return false;
            }

            Hero target = otherHeroes.get(choice - 1);

            // Find valid teleport positions (adjacent to target, same lane, no heroes, not
            // in front)
            int[][] offsets = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };
            List<int[]> validPositions = new ArrayList<>();

            for (int[] offset : offsets) {
                int newX = target.getX() + offset[0];
                int newY = target.getY() + offset[1];

                if (board.getTileAt(newX, newY) != null &&
                        board.getTileAt(newX, newY).isAccessible() &&
                        !board.hasHeroAt(newX, newY) &&
                        board.getLane(newX) == target.getLane()) {

                    // Only allow teleport to same row, left, right, or behind target
                    // Not in front of target (newY < target.getY())
                    if (newY >= target.getY()) {
                        // Check not behind monster
                        boolean validPos = true;
                        for (Monster m : board.getMonsters()) {
                            if (m.isAlive() && m.getLane() == target.getLane() && m.getY() > newY) {
                                validPos = false;
                                break;
                            }
                        }
                        if (validPos) {
                            validPositions.add(new int[] { newX, newY });
                        }
                    }
                }
            }

            if (validPositions.isEmpty()) {
                output.println("No valid positions to teleport to!");
                return false;
            }

            // Display valid teleport positions
            output.println("\nAvailable teleport positions:");
            for (int i = 0; i < validPositions.size(); i++) {
                int[] pos = validPositions.get(i);
                String direction = "";
                if (pos[1] == target.getY()) {
                    direction = pos[0] < target.getX() ? "Left of " : "Right of ";
                } else {
                    direction = "Behind ";
                }
                output.println((i + 1) + ". " + direction + target.getName() +
                        " (" + pos[0] + "," + pos[1] + ")");
            }
            output.print("Choose position (or 0 to cancel): ");

            int posChoice = Integer.parseInt(input.readLine());
            if (posChoice == 0)
                return false;
            if (posChoice < 1 || posChoice > validPositions.size()) {
                output.println("Invalid choice.");
                return false;
            }

            int[] pos = validPositions.get(posChoice - 1);
            removeTerrainBonus(hero);

            hero.setLane(target.getLane());
            board.moveHero(hero, pos[0], pos[1], output);

            applyTerrainBonus(hero);
            SoundManager.getInstance().playTeleportSound();
            output.printlnGreen(hero.getName() + " teleported to lane " + target.getLane() + "!");
            return true;

        } catch (NumberFormatException e) {
            output.println("Invalid input.");
            return false;
        }
    }

    /**
     * Handles hero recall to their Nexus.
     */
    private boolean handleRecall(Hero hero) {
        removeTerrainBonus(hero);

        int nexusRow = board.getHeight() - 1;
        int nexusCol = board.getLeftColumnOfLane(hero.getOriginalLane()); // Use original lane

        hero.setLane(hero.getOriginalLane()); // Reset to original lane
        board.moveHero(hero, nexusCol, nexusRow, output);

        SoundManager.getInstance().playRecallSound();

        output.printlnGreen(hero.getName() + " recalled to their Nexus!");
        return true;
    }

    /**
     * Handles hero attack action.
     */
    private boolean handleHeroAttack(Hero hero) {
        List<Monster> monstersInRange = getMonstersInRange(hero);

        if (monstersInRange.isEmpty()) {
            output.println("No monsters in range!");
            return false;
        }

        Monster target = null;
        if (monstersInRange.size() == 1) {
            target = monstersInRange.get(0);
            output.println("Attacking " + target.getName() + " (HP: " + target.getHp() + ")");
        } else {
            output.println("\nSelect monster to attack:");
            for (int i = 0; i < monstersInRange.size(); i++) {
                Monster m = monstersInRange.get(i);
                output.println((i + 1) + ". " + m.getName() + 
                    " (Level: " + m.getLevel() + 
                    ", HP: " + m.getHp() + 
                    ", Dmg: " + m.getDamage() + 
                    ", Def: " + m.getDefense() + 
                    ", Pos: " + m.getX() + "," + m.getY() + ")");
            }
            output.print("Choose target (or 0 to cancel): ");

            try {
                int choice = Integer.parseInt(input.readLine());
                if (choice == 0)
                    return false;
                if (choice < 1 || choice > monstersInRange.size()) {
                    output.println("Invalid choice.");
                    return false;
                }
                target = monstersInRange.get(choice - 1);
            } catch (NumberFormatException e) {
                output.println("Invalid input.");
                return false;
            }
        }

            // The attack logic is now handled by the Entity class
            hero.attack(target, styledOutput);

            if (!target.isAlive()) {
                // Death sound and message are handled in attack()
                handleMonsterDeath(target, hero);
            }
            return true;
    }

    /**
     * Handles hero casting a spell.
     */
    private boolean handleHeroCastSpell(Hero hero) {
        List<Spell> spells = new ArrayList<>();
        for (Item item : hero.getInventory()) {
            if (item instanceof Spell) {
                spells.add((Spell) item);
            }
        }

        if (spells.isEmpty()) {
            output.println("No spells available!");
            return false;
        }

        output.println("\nSelect spell:");
        for (int i = 0; i < spells.size(); i++) {
            Spell s = spells.get(i);
            output.println((i + 1) + ". " + s.getName() + " (MP: " + s.getManaCost() +
                    ", Dmg: " + s.getDamage() + ")");
        }
        output.print("Choose spell (or 0 to cancel): ");

        try {
            int spellChoice = Integer.parseInt(input.readLine());
            if (spellChoice == 0)
                return false;
            if (spellChoice < 1 || spellChoice > spells.size()) {
                output.println("Invalid choice.");
                return false;
            }

            Spell spell = spells.get(spellChoice - 1);
            if (hero.getMana() < spell.getManaCost()) {
                output.println("Not enough mana!");
                return false;
            }

            List<Monster> monstersInRange = getMonstersInRange(hero);
            if (monstersInRange.isEmpty()) {
                output.println("No monsters in range!");
                return false;
            }

            Monster target = null;
            if (monstersInRange.size() == 1) {
                target = monstersInRange.get(0);
                output.println("Casting on " + target.getName() + " (HP: " + target.getHp() + ")");
            } else {
                output.println("\nSelect target:");
                for (int i = 0; i < monstersInRange.size(); i++) {
                    Monster m = monstersInRange.get(i);
                    output.println((i + 1) + ". " + m.getName() + 
                        " (Level: " + m.getLevel() + 
                        ", HP: " + m.getHp() + 
                        ", Dmg: " + m.getDamage() + 
                        ", Def: " + m.getDefense() + 
                        ", Pos: " + m.getX() + "," + m.getY() + ")");
                }
                output.print("Choose target (or 0 to cancel): ");

                int targetChoice = Integer.parseInt(input.readLine());
                if (targetChoice == 0)
                    return false;
                if (targetChoice < 1 || targetChoice > monstersInRange.size()) {
                    output.println("Invalid choice.");
                    return false;
                }
                target = monstersInRange.get(targetChoice - 1);
            }

            hero.setMana(hero.getMana() - spell.getManaCost());

            double spellDamage = spell.getDamage() + (hero.getDexterity() / 10000.0 * spell.getDamage());
            int damage = (int) spellDamage;

            target.takeDamage(damage);
            SoundManager.getInstance().playDamageSound();

            styledOutput.printSpellCast(hero.getName(), spell.getName(), target.getName(), damage);
            spell.applyEffect(target, output);

            if (!target.isAlive()) {
                SoundManager.getInstance().playMonsterDeathSound();
                handleMonsterDeath(target, hero);
            }
            return true;

        } catch (NumberFormatException e) {
            output.println("Invalid input.");
            return false;
        }
    }

    /**
     * Handles hero using a potion.
     */
    private boolean handleHeroUsePotion(Hero hero) {
        List<Potion> potions = new ArrayList<>();
        for (Item item : hero.getInventory()) {
            if (item instanceof Potion) {
                potions.add((Potion) item);
            }
        }

        if (potions.isEmpty()) {
            output.println("No potions available!");
            return false;
        }

        output.println("\nSelect potion:");
        for (int i = 0; i < potions.size(); i++) {
            Potion p = potions.get(i);
            output.println((i + 1) + ". " + p.getName() + " (+" + p.getAttributeIncrease() +
                    " " + p.getAttributeAffected() + ")");
        }
        output.print("Choose potion (or 0 to cancel): ");

        try {
            int choice = Integer.parseInt(input.readLine());
            if (choice == 0)
                return false;
            if (choice < 1 || choice > potions.size()) {
                output.println("Invalid choice.");
                return false;
            }

            Potion potion = potions.get(choice - 1);
            SoundManager.getInstance().playPotionSound();
            hero.usePotion(potion);
            styledOutput.printPotionUse(hero.getName(), potion.getName());
            return true;

        } catch (NumberFormatException e) {
            output.println("Invalid input.");
            return false;
        }
    }

    /**
     * Handles hero changing equipment.
     */
    private boolean handleHeroChangeEquipment(Hero hero) {
        output.println("\n--- Equipment Menu ---");
        output.println("1. Equip Item");
        output.println("2. Unequip Item");
        output.println("3. Cancel");
        output.print("Choose option: ");

        String menuChoice = input.readLine();

        if (menuChoice.equals("2")) {
            output.println("\n--- Unequip Item ---");
            output.println("1. Main Hand: "
                    + (hero.getMainHandWeapon() != null ? hero.getMainHandWeapon().getName() : "None"));
            output.println(
                    "2. Off Hand: " + (hero.getOffHandWeapon() != null ? hero.getOffHandWeapon().getName() : "None"));
            output.println(
                    "3. Armor: " + (hero.getEquippedArmor() != null ? hero.getEquippedArmor().getName() : "None"));
            output.println("4. Back");
            output.print("Choose slot to unequip: ");

            String choice = input.readLine();
            switch (choice) {
                case "1":
                    if (hero.getMainHandWeapon() != null) {
                        output.printlnGreen("Unequipped " + hero.getMainHandWeapon().getName());
                        hero.unequipMainHand();
                        return true; // Action taken
                    } else {
                        output.println("Nothing equipped in Main Hand.");
                    }
                    break;
                case "2":
                    if (hero.getOffHandWeapon() != null) {
                        output.printlnGreen("Unequipped " + hero.getOffHandWeapon().getName());
                        hero.unequipOffHand();
                        return true; // Action taken
                    } else {
                        output.println("Nothing equipped in Off Hand.");
                    }
                    break;
                case "3":
                    if (hero.getEquippedArmor() != null) {
                        output.printlnGreen("Unequipped " + hero.getEquippedArmor().getName());
                        hero.unequipArmor();
                        return true; // Action taken
                    } else {
                        output.println("Nothing equipped in Armor slot.");
                    }
                    break;
                case "4":
                    break;
                default:
                    output.println("Invalid option.");
            }
            return false;
        } else if (!menuChoice.equals("1")) {
            return false;
        }

        List<Item> equipment = new ArrayList<>();
        for (Item item : hero.getInventory()) {
            if (item instanceof Weapon || item instanceof Armor) {
                equipment.add(item);
            }
        }

        if (equipment.isEmpty()) {
            output.println("No equipment available!");
            return false;
        }

        output.println("\nCurrent Equipment:");
        output.println(
                "Main Hand: " + (hero.getMainHandWeapon() != null ? hero.getMainHandWeapon().getName() : "None"));
        output.println("Off Hand: " + (hero.getOffHandWeapon() != null ? hero.getOffHandWeapon().getName() : "None"));
        output.println("Armor: " + (hero.getEquippedArmor() != null ? hero.getEquippedArmor().getName() : "None"));

        output.println("\nSelect equipment to equip:");
        for (int i = 0; i < equipment.size(); i++) {
            Item item = equipment.get(i);
            output.print((i + 1) + ". " + item.getName());
            if (item instanceof Weapon) {
                output.println(" (Weapon, Dmg: " + ((Weapon) item).getDamage() + ")");
            } else {
                output.println(" (Armor, Def: " + ((Armor) item).getDamageReduction() + ")");
            }
        }
        output.print("Choose equipment (or 0 to cancel): ");

        try {
            int choice = Integer.parseInt(input.readLine());
            if (choice == 0)
                return false;
            if (choice < 1 || choice > equipment.size()) {
                output.println("Invalid choice.");
                return false;
            }

            Item item = equipment.get(choice - 1);
            if (item instanceof Weapon) {
                hero.equipMainHand((Weapon) item);
                SoundManager.getInstance().playEquipSound();
                output.printlnGreen("Equipped " + item.getName());
            } else if (item instanceof Armor) {
                hero.equipArmor((Armor) item);
                SoundManager.getInstance().playEquipSound();
                output.printlnGreen("Equipped " + item.getName());
            }
            return true;

        } catch (NumberFormatException e) {
            output.println("Invalid input.");
            return false;
        }
    }

    /**
     * Handles destroying an obstacle.
     */
    private boolean handleDestroyObstacle(Hero hero) {
        int[][] neighbors = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };
        List<int[]> obstacles = new ArrayList<>();

        for (int[] offset : neighbors) {
            int checkX = hero.getX() + offset[0];
            int checkY = hero.getY() + offset[1];
            Tile tile = board.getTileAt(checkX, checkY);
            if (tile instanceof ObstacleTile) {
                obstacles.add(new int[] { checkX, checkY });
            }
        }

        if (obstacles.isEmpty()) {
            output.println("No adjacent obstacles to destroy!");
            return false;
        }

        int[] pos = obstacles.get(0);
        board.destroyObstacle(pos[0], pos[1], output);
        SoundManager.getInstance().playDamageSound();
        return true;
    }

    /**
     * Handles market interaction.
     */
    private void handleMarket(Hero hero) {
        Tile tile = board.getTileAt(hero.getX(), hero.getY());
        if (!(tile instanceof NexusTile) || !((NexusTile) tile).isMarket()) {
            output.println("You must be at the heroes' Nexus to access the market!");
            return;
        }
        Market market = ((NexusTile) tile).getMarket();

        styledOutput.printMarketBanner();
        SoundManager.getInstance().playMarketSound();

        boolean inMarket = true;
        while (inMarket) {
            output.println("\n--- Market (Gold: " + hero.getMoney() + ") ---");
            output.println("1. Buy Item");
            output.println("2. Sell Item");
            output.println("3. Exit Market");
            output.print("Choose option: ");

            String choice = input.readLine();
            switch (choice) {
                case "1":
                    buyItem(hero, market);
                    break;
                case "2":
                    sellItem(hero, market);
                    break;
                case "3":
                    inMarket = false;
                    break;
                default:
                    output.println("Invalid option.");
            }
        }
    }

    /**
     * Handles buying items from the market.
     */
    private void buyItem(Hero hero, Market market) {
        output.println("\nSelect category:");
        output.println("1. Weapons");
        output.println("2. Armor");
        output.println("3. Potions");
        output.println("4. Spells");
        output.print("Choose category: ");

        String category = input.readLine();
        List<Item> availableItems = new ArrayList<>();
        List<Item> marketInventory = market.getInventory();

        switch (category) {
            case "1":
                for (Item item : marketInventory) {
                    if (item instanceof Weapon)
                        availableItems.add(item);
                }
                break;
            case "2":
                for (Item item : marketInventory) {
                    if (item instanceof Armor)
                        availableItems.add(item);
                }
                break;
            case "3":
                for (Item item : marketInventory) {
                    if (item instanceof Potion)
                        availableItems.add(item);
                }
                break;
            case "4":
                for (Item item : marketInventory) {
                    if (item instanceof Spell)
                        availableItems.add(item);
                }
                break;
            default:
                output.println("Invalid category.");
                return;
        }

        if (availableItems.isEmpty()) {
            output.println("No items available in this category.");
            return;
        }

        output.println("\nAvailable items:");
        for (int i = 0; i < availableItems.size(); i++) {
            Item item = availableItems.get(i);
            output.println((i + 1) + ". " + item.getName() + " (Cost: " + item.getCost() +
                    ", Level: " + item.getRequiredLevel() + ")");
        }
        output.print("Choose item (or 0 to cancel): ");

        try {
            int choice = Integer.parseInt(input.readLine());
            if (choice == 0)
                return;
            if (choice < 1 || choice > availableItems.size()) {
                output.println("Invalid choice.");
                return;
            }

            Item item = availableItems.get(choice - 1);
            if (hero.getMoney() < item.getCost()) {
                output.println("Not enough gold!");
            } else if (hero.getLevel() < item.getRequiredLevel()) {
                output.println("Level too low!");
            } else {
                hero.setMoney(hero.getMoney() - item.getCost());
                hero.addItem(item);
                market.removeItem(item);
                SoundManager.getInstance().playBuySound();
                styledOutput.printBuy(hero.getName(), item.getName(), item.getCost());
            }
        } catch (NumberFormatException e) {
            output.println("Invalid input.");
        }
    }

    /**
     * Handles selling items to the market.
     */
    private void sellItem(Hero hero, Market market) {
        if (hero.getInventory().isEmpty()) {
            output.println("No items to sell!");
            return;
        }

        output.println("\nYour inventory:");
        for (int i = 0; i < hero.getInventory().size(); i++) {
            Item item = hero.getInventory().get(i);
            int sellPrice = item.getCost() / 2;
            output.println((i + 1) + ". " + item.getName() + " (Sell for: " + sellPrice + " gold)");
        }
        output.print("Choose item to sell (or 0 to cancel): ");

        try {
            int choice = Integer.parseInt(input.readLine());
            if (choice == 0)
                return;
            if (choice < 1 || choice > hero.getInventory().size()) {
                output.println("Invalid choice.");
                return;
            }

            Item item = hero.getInventory().get(choice - 1);
            int sellPrice = item.getCost() / 2;
            hero.setMoney(hero.getMoney() + sellPrice);
            hero.removeItem(item);
            market.addItem(item);
            SoundManager.getInstance().playSellSound();
            styledOutput.printSell(hero.getName(), item.getName(), sellPrice);
        } catch (NumberFormatException e) {
            output.println("Invalid input.");
        }
    }

    /**
     * Handles the monsters' turn.
     */
    private void monstersTurn() {
        output.println("\n--- MONSTERS' TURN ---");

        for (Monster monster : new ArrayList<>(board.getMonsters())) {
            monster.takeTurn(board, styledOutput);
        }
    }

    /**
     * Handles end of round effects (HP/MP regeneration, hero respawn).
     */
    private void endOfRoundEffects() {
        for (Hero hero : selectedHeroes) {
            if (hero.isAlive()) {
                int hpRegen = (int) (hero.getLevel() * 100 * 0.1);
                int manaRegen = (int) (hero.getMana() * 0.1);
                hero.setHp(hero.getHp() + hpRegen);
                hero.setMana(hero.getMana() + manaRegen);
            } else {
                // Respawn hero at their Nexus
                int nexusRow = board.getHeight() - 1;
                int nexusCol = board.getLeftColumnOfLane(hero.getLane());
                hero.setHp(hero.getLevel() * 100);
                hero.setMana(hero.getMana());
                board.placeHero(hero, nexusCol, nexusRow);
                SoundManager.getInstance().playRecallSound();
                output.printlnGreen(hero.getName() + " respawned at their Nexus!");
            }
        }
    }

    /**
     * Selects 3 heroes for the game.
     */
    private void selectHeroes() {
        output.println("\n--- Select Your Heroes ---");
        List<Hero> availableHeroes = new ArrayList<>(heroes);

        for (int i = 0; i < 3; i++) {
            output.println("\nSelect Hero " + (i + 1) + ":");
            for (int j = 0; j < availableHeroes.size(); j++) {
                Hero h = availableHeroes.get(j);
                output.println((j + 1) + ". " + h.getName() + " (" + h.getHeroClass() +
                        ") Lvl " + h.getLevel());
            }

            int choice = -1;
            while (choice < 1 || choice > availableHeroes.size()) {
                output.print("Choose hero: ");
                try {
                    choice = Integer.parseInt(input.readLine());
                    if (choice < 1 || choice > availableHeroes.size()) {
                        output.println("Invalid choice.");
                    }
                } catch (NumberFormatException e) {
                    output.println("Invalid input.");
                }
            }

            Hero selected = availableHeroes.get(choice - 1);
            selectedHeroes.add(selected);
            availableHeroes.remove(choice - 1);
            output.printlnGreen(selected.getName() + " selected!");
        }
    }

    /**
     * Assigns heroes to lanes and spawns them.
     */
    private void assignHeroesToLanes() {
        for (int i = 0; i < selectedHeroes.size(); i++) {
            Hero hero = selectedHeroes.get(i);
            hero.setLane(i);
            hero.setOriginalLane(i); // ADD THIS LINE

            int nexusRow = board.getHeight() - 1;
            int nexusCol = board.getLeftColumnOfLane(i);
            board.placeHero(hero, nexusCol, nexusRow);

            output.println(hero.getName() + " assigned to Lane " + i);
        }
    }

    /**
     * Spawns initial set of monsters.
     */
    private void spawnInitialMonsters() {
        spawnMonsters();
    }

    /**
     * Spawns 3 new monsters (one per lane).
     */
    private void spawnMonsters() {
        int maxLevel = 1;
        for (Hero hero : selectedHeroes) {
            if (hero.getLevel() > maxLevel) {
                maxLevel = hero.getLevel();
            }
        }

        Random rand = new Random();
        List<Monster> eligibleMonsters = new ArrayList<>();
        for (Monster m : super.monsters) {
            if (m.getLevel() <= maxLevel) {
                eligibleMonsters.add(m);
            }
        }

        for (int lane = 0; lane < 3; lane++) {
            if (!eligibleMonsters.isEmpty()) {
                Monster template = eligibleMonsters.get(rand.nextInt(eligibleMonsters.size()));

                Monster newMonster = null;
                if (template instanceof Spirit) {
                    newMonster = spiritFactory.createMonster(template.getName(), template.getLevel(),
                            template.getDamage(), template.getDefense(),
                            template.getDodgeChance(), new ValorMonsterAI());
                } else if (template instanceof Dragon) {
                    newMonster = dragonFactory.createMonster(template.getName(), template.getLevel(),
                            template.getDamage(), template.getDefense(),
                            template.getDodgeChance(), new ValorMonsterAI());
                } else if (template instanceof Exoskeleton) {
                    newMonster = exoskeletonFactory.createMonster(template.getName(), template.getLevel(),
                            template.getDamage(), template.getDefense(),
                            template.getDodgeChance(), new ValorMonsterAI());
                }

                if (newMonster != null) {
                    newMonster.setName(newMonster.getName() + " " + (++monsterSpawnCounter));
                    newMonster.setLane(lane);

                    int spawnCol = board.getRightColumnOfLane(lane);
                    int spawnRow = 0;
                    board.placeMonster(newMonster, spawnCol, spawnRow);

                    output.printlnRed("Monster " + newMonster.getName() + " spawned in Lane " + lane + "!");
                }
            }
        }
    }

    /**
     * Checks victory conditions.
     */
    private boolean checkVictory() {
        // Check if any hero reached monsters' Nexus (row 0)
        for (Hero hero : selectedHeroes) {
            if (hero.getY() == 0) {
                styledOutput.printVictory();
                SoundManager.getInstance().playVictorySound();
                displayFinalStats();
                gameRunning = false;
                return true;
            }
        }

        // Check if any monster reached heroes' Nexus (row 7)
        for (Monster monster : board.getMonsters()) {
            if (monster.isAlive() && monster.getY() == board.getHeight() - 1) {
                styledOutput.printDefeat();
                SoundManager.getInstance().playDefeatSound();
                displayFinalStats();
                gameRunning = false;
                return true;
            }
        }

        return false;
    }

    /**
     * Displays final game statistics.
     */
    private void displayFinalStats() {
        output.println("\n--- Final Statistics ---");
        output.println("Rounds survived: " + roundNumber);
        for (Hero hero : selectedHeroes) {
            output.println(hero.getName() + ": Level " + hero.getLevel() +
                    ", Gold: " + hero.getMoney() + ", XP: " + hero.getExperience() +
                    ", Total Gold Earned: " + hero.getTotalGoldEarned() +
                    ", Total XP Earned: " + hero.getTotalXpEarned());
        }
    }

    /**
     * Gets monsters within attack range of a hero.
     */
    private List<Monster> getMonstersInRange(Hero hero) {
        List<Monster> inRange = new ArrayList<>();
        // Include diagonal attacks
        int[][] offsets = {
                { 0, 0 }, // same space
                { 0, 1 }, // down
                { 0, -1 }, // up
                { 1, 0 }, // right
                { -1, 0 }, // left
                { 1, 1 }, // diagonal down-right
                { 1, -1 }, // diagonal up-right
                { -1, 1 }, // diagonal down-left
                { -1, -1 } // diagonal up-left
        };

        for (int[] offset : offsets) {
            int checkX = hero.getX() + offset[0];
            int checkY = hero.getY() + offset[1];
            Monster m = board.getMonsterAt(checkX, checkY);
            if (m != null && m.isAlive()) {
                inRange.add(m);
            }
        }

        return inRange;
    }



    /**
     * Handles monster death, giving rewards to all heroes.
     */
    private void handleMonsterDeath(Monster monster, Hero killer) {
        output.printlnGreen(monster.getName() + " has been defeated!");

        int goldReward = monster.getLevel() * 500;
        int xpReward = monster.getLevel() * 2;

        for (Hero hero : selectedHeroes) {
            hero.setMoney(hero.getMoney() + goldReward);
            hero.gainExperience(xpReward, styledOutput);
        }

        output.println("All heroes gained " + goldReward + " gold and " + xpReward + " XP!");
        board.removeMonster(monster);
    }

    /**
     * Applies terrain bonus to a hero based on their current tile.
     */
    private void applyTerrainBonus(Hero hero) {
        Tile tile = board.getTileAt(hero.getX(), hero.getY());
        if (tile instanceof BushTile) {
            ((BushTile) tile).applyBonus(hero);
        } else if (tile instanceof CaveTile) {
            ((CaveTile) tile).applyBonus(hero);
        } else if (tile instanceof KoulouTile) {
            ((KoulouTile) tile).applyBonus(hero);
        }
    }

    /**
     * Removes terrain bonus from a hero based on their current tile.
     */
    private void removeTerrainBonus(Hero hero) {
        Tile tile = board.getTileAt(hero.getX(), hero.getY());
        if (tile instanceof BushTile) {
            ((BushTile) tile).removeBonus(hero);
        } else if (tile instanceof CaveTile) {
            ((CaveTile) tile).removeBonus(hero);
        } else if (tile instanceof KoulouTile) {
            ((KoulouTile) tile).removeBonus(hero);
        }
    }

    /**
     * Shows information menu.
     */
    protected void showInfoMenu() {
        output.println("\n--- Game Info ---");
        output.println("Round: " + roundNumber);
        output.println("\nHeroes:");
        for (Hero hero : selectedHeroes) {
            String hpBar = com.legends.io.ConsoleOutput.createProgressBar(hero.getHp(), hero.getLevel() * 100,
                    com.legends.io.ConsoleOutput.ANSI_RED);
            String manaBar = com.legends.io.ConsoleOutput.createProgressBar(hero.getMana(), hero.getMaxMana(),
                    com.legends.io.ConsoleOutput.ANSI_BLUE);
            output.println(hero.getName() + " (Lane " + hero.getLane() + "): Lvl " + hero.getLevel() +
                    " HP:" + hpBar + " MP:" + manaBar + " Gold:" + hero.getMoney());
        }
        output.println("\nMonsters:");
        for (Monster monster : board.getMonsters()) {
            if (monster.isAlive()) {
                output.println(monster.getName() + " (Lane " + monster.getLane() + "): " + monster.toString());
            }
        }
    }

    /**
     * Loads a saved game.
     */
    public void loadGame() {
        File saveFile = new File(getSaveFileName());
        if (!saveFile.exists()) {
            output.printlnRed("No saved game found.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile))) {
            GameValor loadedGame = (GameValor) ois.readObject();
            this.selectedHeroes = loadedGame.selectedHeroes;
            this.board = loadedGame.board;
            this.roundNumber = loadedGame.roundNumber;

            // Restore data pools from loaded game if available
            this.heroes = loadedGame.heroes;
            this.monsters = loadedGame.monsters;
            this.items = loadedGame.items;

            // Ensure data pools are initialized
            if (this.heroes == null || this.monsters == null || this.items == null) {
                if (this.heroes == null) this.heroes = new ArrayList<>();
                if (this.monsters == null) this.monsters = new ArrayList<>();
                if (this.items == null) this.items = new ArrayList<>();
                init();
            }

            output.printlnGreen("Game loaded successfully!");
            gameLoop();
        } catch (IOException | ClassNotFoundException e) {
            output.printError("Error loading game: " + e.getMessage());
        }
    }
}
