package com.legends.game;

import com.legends.model.*;
import com.legends.gameFiles.Battle;
import com.legends.gameFiles.Board;
import com.legends.gameFiles.CommonTile;
import com.legends.gameFiles.DynamicMarket;
import com.legends.gameFiles.Market;
import com.legends.gameFiles.MarketTile;
import com.legends.gameFiles.Tile;
import com.legends.io.Input;
import com.legends.io.Output;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The main game controller.
 * Manages the game loop, board, party, and interactions.
 */
public class GameMonstersAndHeroes extends GameInterface implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Hero> heroes;
    private Party party;
    private List<Monster> monsters;
    private List<Item> items;
    private Board board;
    private boolean isRunning;
    private transient Input input;
    private transient Output output;
    private boolean gameRunning;
    private String difficulty = "Normal";

    /**
     * Constructs a new Game instance.
     *
     * @param input  The input interface.
     * @param output The output interface.
     */
    public GameMonstersAndHeroes(Input input, Output output) {
        super(input, output);
        this.party = new Party();
        this.heroes = super.heroes;
        this.monsters = super.monsters;
        this.items = super.items;
        this.isRunning = true;
        this.input = super.input;
        this.output = super.output;
    }

    /**
     * Initializes the game by loading data from CSV files.
     */
    public void init() {
        super.init();
    }

    /**
     * Starts the main menu loop.
     */
    public void start() {
        output.println("Welcome to Legends: Monsters and Heroes!");

        while (isRunning) {
            try {
                output.println("\n--- Main Menu ---");
                output.println("1. Start New Game");
                output.println("2. Load Game");
                output.println("3. Delete Saved Game");
                output.println("4. How to Play");
                output.println("5. Quit");
                output.print("Choose an option: ");

                String choice = input.readLine();

                switch (choice) {
                    case "1":
                        startGame();
                        break;
                    case "2":
                        loadGame();
                        break;
                    case "3":
                        deleteSaveGame();
                        break;
                    case "4":
                        showInstructions();
                        break;
                    case "5":
                        isRunning = false;
                        output.println("Goodbye!");
                        break;
                    default:
                        output.println("Invalid option.");
                }
            } catch (QuitGameException e) {
                output.println("\nReturning to Main Menu...");
            }
        }
    }

    /**
     * Starts a new game session.
     * Handles difficulty selection, board setup, party initialization, and the game
     * loop.
     */
    protected void startGame() {
        selectDifficulty();
        resetGame();
        setupBoard();
        initializeParty();
        placeHeroesOnBoard();
        gameLoop();
    }

    /**
     * Prompts the user to select the game difficulty.
     */
    private void selectDifficulty() {
        output.println("\nSelect Difficulty:");
        output.println("1. Normal (Standard gameplay, Game Over on defeat)");
        output.println("2. Hard (Stronger monsters, Continue on defeat with rewards)");
        output.print("Choose difficulty: ");
        String choice = input.readLine();
        if (choice.equals("2")) {
            difficulty = "Hard";
            output.println("Difficulty set to Hard.");
        } else {
            difficulty = "Normal";
            output.println("Difficulty set to Normal.");
        }
    }

    /**
     * Resets the game state for a new session.
     */
    protected void resetGame() {
        heroes.clear();
        monsters.clear();
        items.clear();
        party = new Party();
        board = null;
        init();
    }

    /**
     * Displays the game instructions.
     */
    protected void showInstructions() {
        output.println("\n=== How to Play ===");
        output.println("- You control a party of 1–3 heroes.");
        output.println("- Each hero has HP, Mana, Strength, Dexterity, Agility, Gold, and Experience.");
        output.println("");
        output.println("On the map:");
        output.println("  H = Your heroes");
        output.println("  M = Market (buy/sell weapons, armor, potions, spells)");
        output.println("  - = Common land (you may encounter monsters)");
        output.println("  X = Inaccessible tile (you cannot step there)");
        output.println("");
        output.println("Controls during exploration:");
        output.println("  W = move up");
        output.println("  A = move left");
        output.println("  S = move down");
        output.println("  D = move right");
        output.println("  M = enter market if you are on a Market tile");
        output.println("  I = show party info (stats, equipment, gold)");
        output.println("  Q = quit the current game and return to the main menu");
        output.println("");
        output.println("During battles (in Battle class):");
        output.println("  1) Attack with your weapon");
        output.println("  2) Cast a spell (if you have spells and enough mana)");
        output.println("  3) Use a potion (heal or buff stats)");
        output.println("  4) Equip a weapon from your inventory");
        output.println("  5) Equip armor from your inventory");
        output.println("  0) Skip the hero's turn");
        output.println("");
        output.println("Goal:");
        output.println("- Explore the map, defeat monsters, earn gold and experience,");
        output.println("  buy better gear from markets, and level up your heroes.");
        output.println("====================");
    }

    /**
     * The main game loop.
     * Handles player input for movement and menu access.
     */
    protected void gameLoop() {
        gameRunning = true;
        while (gameRunning) {
            if (board != null)
                board.printBoard(output);

            output.print("Enter move (W/A/S/D), I for Info, H for Hero Menu, M for Market, K to Save, or Q to quit: ");
            String dir = input.readLine().toUpperCase();

            if (dir.equals("Q")) {
                gameRunning = false;
            } else if (dir.equals("W") || dir.equals("A") || dir.equals("S") || dir.equals("D")) {
                processMove(dir);
            } else if (dir.equals("I")) {
                showInfoMenu();
            } else if (dir.equals("H")) {
                showHeroMenu();
            } else if (dir.equals("K")) {
                saveGame();
            } else if (dir.equals("M")) {
                Hero leader = party.getLeader();
                Tile tile = board.getTileAt(leader.getX(), leader.getY());
                if (tile instanceof MarketTile) {
                    visitMarket();
                } else {
                    output.println("You are not on a market tile.");
                }
            } else {
                output.println("Invalid input.");
            }
        }
    }

    /**
     * Processes a movement command.
     *
     * @param dir The direction to move (W/A/S/D).
     */
    private void processMove(String dir) {
        Hero partyLeader = party.getLeader();
        int newX = partyLeader.getX();
        int newY = partyLeader.getY();

        switch (dir) {
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

        if (board.moveEntity(partyLeader.getX(), partyLeader.getY(), newX, newY, output)) {
            party.setLocation(newX, newY);

            Tile tile = board.getTileAt(newX, newY);
            if (tile instanceof CommonTile) {
                checkEncounter();
            } else if (tile instanceof MarketTile) {
                output.println("Party entered market space.");
            }
        }
    }

    /**
     * Handles the market interaction.
     * Allows buying and selling items.
     */
    private void visitMarket() {
        Hero leader = party.getLeader();
        Tile tile = board.getTileAt(leader.getX(), leader.getY());
        if (!(tile instanceof MarketTile)) return;
        Market market = ((MarketTile) tile).getMarket();

        output.println("You have entered a Market!");
        boolean inMarket = true;
        while (inMarket) {
            output.println("\n--- Market Menu ---");
            output.println("1. Buy Item");
            output.println("2. Sell Item");
            output.println("3. Exit Market");
            output.print("Choose an option: ");
            String choice = input.readLine();

            if (choice.equals("1")) {
                buyItemMenu(market);
            } else if (choice.equals("2")) {
                sellItemMenu(market);
            } else if (choice.equals("3")) {
                inMarket = false;
                output.println("Exiting Market.");
            } else {
                output.println("Invalid option.");
            }
        }
    }

    /**
     * Displays the menu for buying items in the market.
     */
    private void buyItemMenu(Market market) {
        // Select Hero
        output.println("\nSelect a Hero to buy for:");
        for (int i = 0; i < party.getSize(); i++) {
            Hero h = party.getHero(i);
            output.println((i + 1) + ". " + h.getName() + " (Gold: " + h.getMoney() + ")");
        }
        output.println((party.getSize() + 1) + ". Cancel");

        int heroIdx = -1;
        try {
            String in = input.readLine();
            heroIdx = Integer.parseInt(in) - 1;
        } catch (NumberFormatException e) {
            output.println("Invalid input.");
            return;
        }

        if (heroIdx == party.getSize())
            return;
        if (heroIdx < 0 || heroIdx >= party.getSize()) {
            output.println("Invalid hero selection.");
            return;
        }

        Hero hero = party.getHero(heroIdx);

        // Select Item Category
        output.println("\nSelect Item Category:");
        output.println("1. Weapons");
        output.println("2. Armor");
        output.println("3. Potions");
        output.println("4. Spells");
        output.println("5. Cancel");
        output.print("Choose a category: ");

        String catChoice = input.readLine();
        List<Item> availableItems = new ArrayList<>();
        List<Item> marketInventory = market.getInventory();

        switch (catChoice) {
            case "1":
                for (Item i : marketInventory)
                    if (i instanceof Weapon)
                        availableItems.add(i);
                break;
            case "2":
                for (Item i : marketInventory)
                    if (i instanceof Armor)
                        availableItems.add(i);
                break;
            case "3":
                for (Item i : marketInventory)
                    if (i instanceof Potion)
                        availableItems.add(i);
                break;
            case "4":
                for (Item i : marketInventory)
                    if (i instanceof Spell)
                        availableItems.add(i);
                break;
            case "5":
                return;
            default:
                output.println("Invalid category.");
                return;
        }

        // Show Items
        output.println("\nAvailable Items:");
        for (int i = 0; i < availableItems.size(); i++) {
            Item item = availableItems.get(i);
            output.println((i + 1) + ". " + item.getName() + " (Cost: " + item.getCost() + ", Lvl Req: "
                    + item.getRequiredLevel() + ")");
        }
        output.println((availableItems.size() + 1) + ". Cancel");
        output.print("Choose an item: ");

        int itemIdx = -1;
        try {
            String in = input.readLine();
            itemIdx = Integer.parseInt(in) - 1;
        } catch (NumberFormatException e) {
            output.println("Invalid input.");
            return;
        }

        if (itemIdx == availableItems.size())
            return;
        if (itemIdx < 0 || itemIdx >= availableItems.size()) {
            output.println("Invalid item selection.");
            return;
        }

        Item itemToBuy = availableItems.get(itemIdx);

        if (hero.getMoney() < itemToBuy.getCost()) {
            output.println("Not enough money!");
        } else if (hero.getLevel() < itemToBuy.getRequiredLevel()) {
            output.println("Hero level too low!");
        } else {
            hero.setMoney(hero.getMoney() - itemToBuy.getCost());
            hero.addItem(itemToBuy);
            market.removeItem(itemToBuy);

    /**
     * Displays the menu for selling items in the market.
     */
    private void sellItemMenu(Market market) {
        // Select Hero
        output.println("\nSelect a Hero to sell from:");
        for (int i = 0; i < party.getSize(); i++) {
            Hero h = party.getHero(i);
            output.println((i + 1) + ". " + h.getName() + " (Gold: " + h.getMoney() + ")");
        }
        output.println((party.getSize() + 1) + ". Cancel");

        int heroIdx = -1;
        try {
            String in = input.readLine();
            heroIdx = Integer.parseInt(in) - 1;
        } catch (NumberFormatException e) {
            output.println("Invalid input.");
            return;
        }

        if (heroIdx == party.getSize())
            return;
        if (heroIdx < 0 || heroIdx >= party.getSize()) {
            output.println("Invalid hero selection.");
            return;
        }

        Hero hero = party.getHero(heroIdx);
        List<Item> inventory = hero.getInventory();

        if (inventory.isEmpty()) {
            output.println(hero.getName() + " has no items to sell.");
            return;
        }

        // Show Items
        output.println("\nSelect Item to Sell:");
        for (int i = 0; i < inventory.size(); i++) {
            Item item = inventory.get(i);
            int sellPrice = item.getCost() / 2;
            output.println((i + 1) + ". " + item.getName() + " (Sell Price: " + sellPrice + ")");
        }
        output.println((inventory.size() + 1) + ". Cancel");
        output.print("Choose an item: ");

        int itemIdx = -1;
        try {
            String in = input.readLine();
            itemIdx = Integer.parseInt(in) - 1;
        } catch (NumberFormatException e) {
            output.println("Invalid input.");
            return;
        }

        if (itemIdx == inventory.size())
            return;
        if (itemIdx < 0 || itemIdx >= inventory.size()) {
            output.println("Invalid item selection.");
            return;
        }

        Item itemToSell = inventory.get(itemIdx);
        int sellPrice = itemToSell.getCost() / 2;

        hero.setMoney(hero.getMoney() + sellPrice);
        hero.removeItem(itemToSell);
        market.addItem(itemToSell);
        output.println(hero.getName() + " sold " + itemToSell.getName() + " for " + sellPrice + " gold.");
    }

    /**
     * Displays the information menu.
     * Allows viewing monsters and items.
     */
    protected void showInfoMenu() {
        output.println("\n--- Info Menu ---");
        output.println("1. Monster Book");
        output.println("2. Show Items");
        output.println("3. Back");
        output.print("Choose an option: ");

        String choice = input.readLine();

        switch (choice) {
            case "1":
                showMonsters();
                break;
            case "2":
                showItems();
                break;
            case "3":
                break;
            default:
                output.println("Invalid option.");
        }
    }

    /**
     * Sets up the game board based on user input for size.
     */
    private void setupBoard() {
        int size = 0;
        final int MIN_SIZE = 4;
        final int MAX_SIZE = 20; // Max size for terminal display limitations

        output.println("\n--- World Configuration ---");

        while (size < MIN_SIZE || size > MAX_SIZE) {
            output.print("Enter world size (" + MIN_SIZE + "-" + MAX_SIZE + "): ");
            try {
                String in = input.readLine();
                size = Integer.parseInt(in);
                if (size < MIN_SIZE || size > MAX_SIZE) {
                    output.println("Invalid size. Must be between " + MIN_SIZE + " and " + MAX_SIZE + ".");
                }
            } catch (NumberFormatException e) {
                output.println("Invalid input. Please enter a number.");
            }
        }

        this.board = new Board(size, size);
        
        // Initialize markets
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                Tile t = board.getTileAt(x, y);
                if (t instanceof MarketTile) {
                    ((MarketTile) t).setMarket(new DynamicMarket(items));
                }
            }
        }
    }

    /**
     * Initializes the party by allowing the user to select heroes.
     */
    private void initializeParty() {
        int partySize = 0;
        while (partySize < 1 || partySize > 3) {
            output.print("Enter number of heroes (1-3): ");
            try {
                String in = input.readLine();
                partySize = Integer.parseInt(in);
                if (partySize < 1 || partySize > 3) {
                    output.println("Invalid number. Must be between 1 and 3.");
                }
            } catch (NumberFormatException e) {
                output.println("Invalid input. Please enter a number.");
            }
        }

        List<Hero> availableHeroes = new ArrayList<>(heroes);

        output.println("\n--- Choose your Heroes ---");
        for (int i = 0; i < partySize; i++) {
            output.println("Select Hero " + (i + 1) + ":");
            for (int j = 0; j < availableHeroes.size(); j++) {
                Hero h = availableHeroes.get(j);
                String hpBar = com.legends.io.ConsoleOutput.createProgressBar(h.getHp(), h.getLevel() * 100, com.legends.io.ConsoleOutput.ANSI_RED);
                String manaBar = com.legends.io.ConsoleOutput.createProgressBar(h.getMana(), h.getMaxMana(), com.legends.io.ConsoleOutput.ANSI_BLUE);
                
                output.println((j + 1) + ". " + h.getName() +
                        " (Class: " + h.getHeroClass() +
                        ", Lvl " + h.getLevel() +
                        ", HP: " + hpBar +
                        ", MP: " + manaBar +
                        ", Str: " + h.getStrength() +
                        ", Agi: " + h.getAgility() +
                        ", Dex: " + h.getDexterity() +
                        ", Money: " + h.getMoney() +
                        ", Exp: " + h.getExperience() + ")");
            }

            int choice = -1;
            while (choice < 1 || choice > availableHeroes.size()) {
                output.print("Enter choice: ");
                try {
                    String in = input.readLine();
                    choice = Integer.parseInt(in);
                    if (choice < 1 || choice > availableHeroes.size()) {
                        output.println("Invalid choice.");
                    } else {
                        Hero selected = availableHeroes.get(choice - 1);

                        // Bonus gold for Hard difficulty to help with early game
                        if (difficulty.equals("Hard")) {
                            selected.setMoney(selected.getMoney() + 1000);
                        }

                        party.addHero(selected);
                        availableHeroes.remove(choice - 1);
                        output.println(selected.getName() + " added to party.");
                    }
                } catch (NumberFormatException e) {
                    output.println("Invalid input.");
                }
            }
        }
    }

    /**
     * Places the party on the first available accessible tile on the board.
     */
    private void placeHeroesOnBoard() {
        if (!party.isEmpty()) {
            boolean placed = false;
            for (int y = 0; y < board.getHeight(); y++) {
                for (int x = 0; x < board.getWidth(); x++) {
                    if (board.getTileAt(x, y).isAccessible() && !board.getTileAt(x, y).isOccupied()) {
                        board.placeEntity(party.getLeader(), x, y);
                        party.setLocation(x, y);
                        placed = true;
                        break;
                    }
                }
                if (placed)
                    break;
            }

            if (!placed) {
                output.println("Warning: Could not place party due to lack of space!");
            } else {
                output.println("Party placed on board.");
            }
        }
    }

    /**
     * Checks for a random monster encounter.
     * If an encounter occurs, initiates a battle.
     */
    private void checkEncounter() {
        java.util.Random rand = new java.util.Random();
        // 50% chance of encounter
        if (rand.nextInt(100) < 50) {
            output.println("You have encountered monsters!");

            // Create monsters for battle
            List<Monster> battleMonsters = new ArrayList<>();
            int maxLevel = 1;
            for (Hero h : party.getHeroes()) {
                if (h.getLevel() > maxLevel)
                    maxLevel = h.getLevel();
            }

            // Filter monsters by level
            List<Monster> eligibleMonsters = new ArrayList<>();
            for (Monster m : monsters) {
                if (m.getLevel() == maxLevel) {
                    eligibleMonsters.add(m);
                }
            }

            // Fallback: if no monsters of exact level exist, use monsters from lower levels
            if (eligibleMonsters.isEmpty()) {
                for (Monster m : monsters) {
                    if (m.getLevel() < maxLevel) {
                        eligibleMonsters.add(m);
                    }
                }
            }

            // Generate same number of monsters as heroes
            for (int i = 0; i < party.getSize(); i++) {
                if (!eligibleMonsters.isEmpty()) {
                    Monster template = eligibleMonsters.get(rand.nextInt(eligibleMonsters.size()));

                    Monster newMonster = null;
                    // Create new instance based on template
                    // We use the template's level and stats directly
                    if (template instanceof Spirit) {
                        newMonster = new Spirit(template.getName(), template.getLevel(), template.getDamage(),
                                template.getDefense(), template.getDodgeChance());
                    } else if (template instanceof Dragon) {
                        newMonster = new Dragon(template.getName(), template.getLevel(), template.getDamage(),
                                template.getDefense(), template.getDodgeChance());
                    } else if (template instanceof Exoskeleton) {
                        newMonster = new Exoskeleton(template.getName(), template.getLevel(), template.getDamage(),
                                template.getDefense(), template.getDodgeChance());
                    }

                    if (newMonster != null) {
                        // Ensure HP is set correctly for the level
                        newMonster.setHp(newMonster.getLevel() * 100);
                        battleMonsters.add(newMonster);
                    }
                }
            }

            if (!battleMonsters.isEmpty()) {
                Battle battle = new Battle(party, battleMonsters, input, output, difficulty);
                String battleResult = battle.start();

                if (battleResult.equals("Defeat")) {
                    if (difficulty.equals("Hard")) {
                        output.println("Hard Mode: Heroes revived and rewarded for their bravery!");
                        reviveHeroes();
                        giveGoldForLoss();
                    } else {
                        output.println("Your party has been defeated! Game Over.");
                        gameRunning = false;
                    }
                }

                if (battleResult.equals("Victory")) {
                    output.println("You won the battle!");
                }
            }
        }
    }

    /**
     * Revives all heroes in the party with 50% HP.
     * Used in Hard mode after defeat.
     */
    private void reviveHeroes() {
        for (int i = 0; i < party.getSize(); i++) {
            Hero h = party.getHero(i);
            h.setHp(h.getLevel() * 50);
        }
    }

    /**
     * Gives gold to heroes after a loss in Hard mode.
     */
    private void giveGoldForLoss() {
        for (int i = 0; i < party.getSize(); i++) {
            Hero h = party.getHero(i);
            h.setMoney(h.getMoney() + 100 * h.getLevel());
        }
    }

    /**
     * Displays the list of all monsters in the game.
     */
    private void showMonsters() {
        output.println("\n--- Monsters ---");
        for (Monster m : monsters) {
            output.println(m);
        }
    }

    /**
     * Displays the list of all items in the game.
     */
    private void showItems() {
        output.println("\n--- Items ---");
        for (Item i : items) {
            output.println(i.getName() + " (Cost: " + i.getCost() + ")");
        }
    }

    /**
     * Displays the hero management menu.
     */
    private void showHeroMenu() {
        output.println("\n--- Hero Menu ---");
        for (int i = 0; i < party.getSize(); i++) {
            output.println((i + 1) + ". " + party.getHero(i).getName());
        }
        output.println((party.getSize() + 1) + ". Back");
        output.print("Select a hero: ");

        int heroIdx = -1;
        try {
            String in = input.readLine();
            heroIdx = Integer.parseInt(in) - 1;
        } catch (NumberFormatException e) {
            output.println("Invalid input.");
            return;
        }

        if (heroIdx == party.getSize())
            return;
        if (heroIdx < 0 || heroIdx >= party.getSize()) {
            output.println("Invalid selection.");
            return;
        }

        manageHero(party.getHero(heroIdx));
    }

    /**
     * Manages a specific hero.
     * Allows checking stats and managing inventory.
     *
     * @param hero The hero to manage.
     */
    private void manageHero(Hero hero) {
        boolean managing = true;
        while (managing) {
            output.println("\n--- Manage " + hero.getName() + " ---");
            output.println("1. Check Stats");
            output.println("2. Manage Inventory");
            output.println("3. Back");
            output.print("Choose an option: ");

            String choice = input.readLine();
            switch (choice) {
                case "1":
                    showHeroStats(hero);
                    break;
                case "2":
                    manageInventory(hero);
                    break;
                case "3":
                    managing = false;
                    break;
                default:
                    output.println("Invalid option.");
            }
        }
    }

    /**
     * Allows the user to unequip items from the hero.
     *
     * @param hero The hero to unequip items from.
     */
    private void unequipItem(Hero hero) {
        output.println("\n--- Unequip Item ---");
        output.println("1. Main Hand: " + (hero.getMainHandWeapon() != null ? hero.getMainHandWeapon().getName() : "None"));
        output.println("2. Off Hand: " + (hero.getOffHandWeapon() != null ? hero.getOffHandWeapon().getName() : "None"));
        output.println("3. Armor: " + (hero.getEquippedArmor() != null ? hero.getEquippedArmor().getName() : "None"));
        output.println("4. Back");
        output.print("Choose slot to unequip: ");

        String choice = input.readLine();
        switch (choice) {
            case "1":
                if (hero.getMainHandWeapon() != null) {
                    output.println("Unequipped " + hero.getMainHandWeapon().getName());
                    hero.unequipMainHand();
                } else {
                    output.println("Nothing equipped in Main Hand.");
                }
                break;
            case "2":
                if (hero.getOffHandWeapon() != null) {
                    output.println("Unequipped " + hero.getOffHandWeapon().getName());
                    hero.unequipOffHand();
                } else {
                    output.println("Nothing equipped in Off Hand.");
                }
                break;
            case "3":
                if (hero.getEquippedArmor() != null) {
                    output.println("Unequipped " + hero.getEquippedArmor().getName());
                    hero.unequipArmor();
                } else {
                    output.println("Nothing equipped in Armor slot.");
                }
                break;
            case "4":
                break;
            default:
                output.println("Invalid option.");
        }
    }

    /**
     * Displays the stats of a hero.
     *
     * @param h The hero to display stats for.
     */
    private void showHeroStats(Hero h) {
        output.println("\n--- Stats for " + h.getName() + " ---");
        output.println("Class: " + h.getHeroClass());
        output.println("Level: " + h.getLevel());
        
        String hpBar = com.legends.io.ConsoleOutput.createProgressBar(h.getHp(), h.getLevel() * 100, com.legends.io.ConsoleOutput.ANSI_RED);
        String manaBar = com.legends.io.ConsoleOutput.createProgressBar(h.getMana(), h.getMaxMana(), com.legends.io.ConsoleOutput.ANSI_BLUE);
        
        output.println("HP: " + hpBar);
        output.println("Mana: " + manaBar);
        output.println("Strength: " + h.getStrength());
        output.println("Agility: " + h.getAgility());
        output.println("Dexterity: " + h.getDexterity());
        output.println("Money: " + h.getMoney());
        output.println("Experience: " + h.getExperience());

        Weapon main = h.getMainHandWeapon();
        Weapon off = h.getOffHandWeapon();
        Armor armor = h.getEquippedArmor();

        output.println("Main Hand: " + (main != null ? main.getName() : "None"));
        output.println("Off Hand: " + (off != null ? off.getName() : "None"));
        output.println("Armor: " + (armor != null ? armor.getName() : "None"));
    }

    /**
     * Manages the inventory of a hero.
     * Allows equipping, using, giving, or unequipping items.
     *
     * @param hero The hero whose inventory is being managed.
     */
    private void manageInventory(Hero hero) {
        boolean managing = true;
        while (managing) {
            output.println("\n--- Inventory Menu ---");
            output.println("1. Equip/Use/Give Item");
            output.println("2. Unequip Item");
            output.println("3. Back");
            output.print("Choose an option: ");

            String choice = input.readLine();
            switch (choice) {
                case "1":
                    handleInventoryActions(hero);
                    break;
                case "2":
                    unequipItem(hero);
                    break;
                case "3":
                    managing = false;
                    break;
                default:
                    output.println("Invalid option.");
            }
        }
    }

    /**
     * Handles actions on inventory items (Equip, Use, Give).
     *
     * @param hero The hero.
     */
    private void handleInventoryActions(Hero hero) {
        List<Item> inv = hero.getInventory();
        if (inv.isEmpty()) {
            output.println("Inventory is empty.");
            return;
        }

        output.println("\n--- Inventory ---");
        for (int i = 0; i < inv.size(); i++) {
            Item item = inv.get(i);
            output.println((i + 1) + ". " + item.getName() + " (" + item.getClass().getSimpleName() + ")");
        }
        output.println((inv.size() + 1) + ". Back");
        output.print("Select item to Equip/Use: ");

        int itemIdx = -1;
        try {
            String in = input.readLine();
            itemIdx = Integer.parseInt(in) - 1;
        } catch (NumberFormatException e) {
            output.println("Invalid input.");
            return;
        }

        if (itemIdx == inv.size())
            return;
        if (itemIdx < 0 || itemIdx >= inv.size()) {
            output.println("Invalid selection.");
            return;
        }

        Item item = inv.get(itemIdx);

        output.println("1. Equip/Use");
        output.println("2. Give to another Hero");
        output.print("Choose action: ");
        String action = input.readLine();

        if (action.equals("2")) {
            giveItem(hero, item);
            return;
        }

        if (item instanceof Weapon) {
            Weapon weapon = (Weapon) item;
            if (weapon.getRequiredHands() == 1) {
                output.println("1. Equip Main Hand");
                output.println("2. Equip Off Hand");
                output.println("3. Equip Main Hand (2-Handed Grip)");
                output.print("Choose slot: ");
                String slot = input.readLine();
                if (slot.equals("1")) {
                    hero.equipMainHand(weapon, false);
                    output.println("Equipped " + item.getName() + " in Main Hand.");
                } else if (slot.equals("2")) {
                    if (hero.equipOffHand(weapon, output)) {
                        output.println("Equipped " + item.getName() + " in Off Hand.");
                    }
                } else if (slot.equals("3")) {
                    hero.equipMainHand(weapon, true);
                    output.println("Equipped " + item.getName() + " in Main Hand (2-Handed Grip).");
                }
            } else {
                hero.equipMainHand(weapon);
                output.println("Equipped " + item.getName() + " in Main Hand (2-Handed).");
            }
        } else if (item instanceof Armor) {
            hero.equipArmor((Armor) item);
            output.println("Equipped " + item.getName() + ".");
        } else if (item instanceof Potion) {
            hero.usePotion((Potion) item);
            output.println("Used " + item.getName() + ".");
        } else {
            output.println("Cannot use this item directly.");
        }
    }

    /**
     * Gives an item from one hero to another.
     *
     * @param sourceHero The hero giving the item.
     * @param item       The item to give.
     */
    private void giveItem(Hero sourceHero, Item item) {
        if (party.getSize() <= 1) {
            output.println("No other heroes in party.");
            return;
        }

        output.println("\nSelect Hero to give " + item.getName() + " to:");
        List<Hero> targets = new ArrayList<>();
        for (int i = 0; i < party.getSize(); i++) {
            Hero h = party.getHero(i);
            if (h != sourceHero) {
                targets.add(h);
                output.println((targets.size()) + ". " + h.getName() + " (Lvl " + h.getLevel() + ")");
            }
        }
        output.println((targets.size() + 1) + ". Cancel");

        int targetIdx = -1;
        try {
            String in = input.readLine();
            targetIdx = Integer.parseInt(in) - 1;
        } catch (NumberFormatException e) {
            output.println("Invalid input.");
            return;
        }

        if (targetIdx == targets.size())
            return;
        if (targetIdx < 0 || targetIdx >= targets.size()) {
            output.println("Invalid selection.");
            return;
        }

        Hero targetHero = targets.get(targetIdx);
        if (targetHero.getLevel() < item.getRequiredLevel()) {
            output.println(targetHero.getName() + " is not high enough level to use this item (Required: "
                    + item.getRequiredLevel() + ").");
            return;
        }

        sourceHero.removeItem(item);
        targetHero.addItem(item);
        output.println("Gave " + item.getName() + " to " + targetHero.getName() + ".");
    }

    /**
     * Saves the current game state to a file.
     */
    public void saveGame() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("MonstersAndHeroesSave.ser"))) {
            oos.writeObject(this);
            output.println("Game saved successfully!");
        } catch (IOException e) {
            output.printError("Error saving game: " + e.getMessage());
        }
    }

    /**
     * Loads the game state from a file.
     */
    public void loadGame() {
        File saveFile = new File("MonstersAndHeroesSave.ser");
        if (!saveFile.exists()) {
            output.printlnRed("Error: No saved game found.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile))) {
            GameMonstersAndHeroes loadedGame = (GameMonstersAndHeroes) ois.readObject();
            this.heroes = loadedGame.heroes;
            this.party = loadedGame.party;
            this.monsters = loadedGame.monsters;
            this.items = loadedGame.items;
            this.board = loadedGame.board;
            this.difficulty = loadedGame.difficulty;
            output.printlnGreen("Game loaded successfully!");
            gameLoop();
        } catch (IOException | ClassNotFoundException e) {
            output.printError("Error loading game: " + e.getMessage());
        }
    }

    /**
     * Deletes the saved game file.
     */
    public void deleteSaveGame() {
        File saveFile = new File("savegame.ser");
        if (saveFile.exists()) {
            if (saveFile.delete()) {
                output.printlnGreen("Saved game deleted successfully.");
            } else {
                output.printlnRed("Error: Failed to delete saved game.");
            }
        } else {
            output.printlnRed("Error: No saved game to delete.");
        }
    }
}
