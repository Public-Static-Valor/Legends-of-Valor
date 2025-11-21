package com.legends.game;

import com.legends.model.*;
import com.legends.utils.DataLoader;
import com.legends.io.Input;
import com.legends.io.Output;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private List<Hero> heroes;
    private Party party;
    private List<Monster> monsters;
    private List<Item> items;
    private Board board;
    private boolean isRunning;
    private Input input;
    private Output output;
    private boolean gameRunning;

    public Game(Input input, Output output) {
        this.heroes = new ArrayList<>();
        this.party = new Party();
        this.monsters = new ArrayList<>();
        this.items = new ArrayList<>();
        this.isRunning = true;
        this.input = input;
        this.output = output;
    }

    public void init() {
        try {
            // Load Heroes
            heroes.addAll(DataLoader.loadHeroes("Paladins.csv", "Paladin"));
            heroes.addAll(DataLoader.loadHeroes("Sorcerers.csv", "Sorcerer"));
            heroes.addAll(DataLoader.loadHeroes("Warriors.csv", "Warrior"));

            // Load Monsters
            monsters.addAll(DataLoader.loadMonsters("Spirits.csv", "Spirit"));
            monsters.addAll(DataLoader.loadMonsters("Dragons.csv", "Dragon"));
            monsters.addAll(DataLoader.loadMonsters("Exoskeletons.csv", "Exoskeleton"));

            // Load Items
            items.addAll(DataLoader.loadWeapons("Weaponry.csv"));
            items.addAll(DataLoader.loadArmor("Armory.csv"));
            items.addAll(DataLoader.loadPotions("Potions.csv"));
            items.addAll(DataLoader.loadSpells("FireSpells.csv", "Fire"));
            items.addAll(DataLoader.loadSpells("IceSpells.csv", "Ice"));
            items.addAll(DataLoader.loadSpells("LightningSpells.csv", "Lightning"));

            output.println("Game Data Loaded Successfully!");
            output.println("Loaded " + heroes.size() + " Heroes.");
            output.println("Loaded " + monsters.size() + " Monsters.");
            output.println("Loaded " + items.size() + " Items.");

        } catch (IOException e) {
            output.printError("Error loading game data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void start() {
        output.println("Welcome to Legends: Monsters and Heroes!");
        
        while (isRunning) {
            output.println("\n--- Main Menu ---");
            output.println("1. Start Game");
            output.println("2. How to Play");
            output.println("3. Quit");
            output.print("Choose an option: ");

            String choice = input.readLine();

            switch (choice) {
                case "1":
                    startGame();
                    break;
                case "2":
                    showInstructions();
                    break;
                case "3":
                    isRunning = false;
                    output.println("Goodbye!");
                    break;
                default:
                    output.println("Invalid option.");
            }
        }
    }

    private void startGame() {
        resetGame();
        setupBoard();
        initializeParty();
        placeHeroesOnBoard();
        gameLoop();
    }

    private void resetGame() {
        heroes.clear();
        monsters.clear();
        items.clear();
        party = new Party();
        board = null;
        init();
    }

    private void showInstructions() {
        output.println("\n--- How to Play ---");
        output.println("1. Create a world by specifying the size.");
        output.println("2. Choose your party of heroes (1-3).");
        output.println("3. Move around the board using W/A/S/D.");
        output.println("4. Avoid inaccessible tiles (marked by 'I').");
        output.println("5. Encounter monsters on common tiles (Marked by empty spaces).");
        output.println("6. Visit markets to buy items (marked by 'M').");
        output.println("7. Defeat monsters to level up and gain gold.");
        output.println("8. Use the Info menu to view details about your heroes, monsters, and items.");
        output.println("9. Use the Hero menu to manage your heroes.");
        output.println("10. Use the Market menu to buy and sell items when on a market tile.");
        output.println("Enjoy your adventure!");
    }

    private void gameLoop() {
        gameRunning = true;
        while (gameRunning) {
            if (board != null) board.printBoard(output);
            
            output.print("Enter move (W/A/S/D), I for Info, H for Hero Menu, M for Market, or Q to quit: ");
            String dir = input.readLine().toUpperCase();

            if (dir.equals("Q")) {
                gameRunning = false;
            } else if (dir.equals("W") || dir.equals("A") || dir.equals("S") || dir.equals("D")) {
                processMove(dir);
            } else if (dir.equals("I")) {
                showInfoMenu();
            } else if (dir.equals("H")) {
                showHeroMenu();
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

    private void processMove(String dir) {
        Hero partyLeader = party.getLeader();
        int newX = partyLeader.getX();
        int newY = partyLeader.getY();

        switch (dir) {
            case "W": newY--; break;
            case "S": newY++; break;
            case "A": newX--; break;
            case "D": newX++; break;
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

    private void visitMarket() {
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
                buyItemMenu();
            } else if (choice.equals("2")) {
                sellItemMenu();
            } else if (choice.equals("3")) {
                inMarket = false;
                output.println("Exiting Market.");
            } else {
                output.println("Invalid option.");
            }
        }
    }

    private void buyItemMenu() {
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
        
        if (heroIdx == party.getSize()) return;
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
        
        switch (catChoice) {
            case "1":
                for (Item i : items) if (i instanceof Weapon) availableItems.add(i);
                break;
            case "2":
                for (Item i : items) if (i instanceof Armor) availableItems.add(i);
                break;
            case "3":
                for (Item i : items) if (i instanceof Potion) availableItems.add(i);
                break;
            case "4":
                for (Item i : items) if (i instanceof Spell) availableItems.add(i);
                break;
            case "5": return;
            default: 
                output.println("Invalid category.");
                return;
        }
        
        // Show Items
        output.println("\nAvailable Items:");
        for (int i = 0; i < availableItems.size(); i++) {
            Item item = availableItems.get(i);
            output.println((i + 1) + ". " + item.getName() + " (Cost: " + item.getCost() + ", Lvl Req: " + item.getRequiredLevel() + ")");
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
        
        if (itemIdx == availableItems.size()) return;
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
            output.println(hero.getName() + " bought " + itemToBuy.getName() + "!");
        }
    }

    private void sellItemMenu() {
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
        
        if (heroIdx == party.getSize()) return;
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
        
        if (itemIdx == inventory.size()) return;
        if (itemIdx < 0 || itemIdx >= inventory.size()) {
            output.println("Invalid item selection.");
            return;
        }
        
        Item itemToSell = inventory.get(itemIdx);
        int sellPrice = itemToSell.getCost() / 2;
        
        hero.setMoney(hero.getMoney() + sellPrice);
        hero.removeItem(itemToSell);
        output.println(hero.getName() + " sold " + itemToSell.getName() + " for " + sellPrice + " gold.");
    }

    private void showInfoMenu() {
        output.println("\n--- Info Menu ---");
        output.println("1. Show Heroes");
        output.println("2. Show Monsters");
        output.println("3. Show Items");
        output.println("4. Back");
        output.print("Choose an option: ");

        String choice = input.readLine();

        switch (choice) {
            case "1":
                showHeroes();
                break;
            case "2":
                showMonsters();
                break;
            case "3":
                showItems();
                break;
            case "4":
                break;
            default:
                output.println("Invalid option.");
        }
    }

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
        output.println("World created with size " + size + "x" + size + ".");
    }

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

        output.println("\n--- Choose your Heroes ---");
        for (int i = 0; i < partySize; i++) {
            output.println("Select Hero " + (i + 1) + ":");
            for (int j = 0; j < heroes.size(); j++) {
                Hero h = heroes.get(j);
                output.println((j + 1) + ". " + h.getName() + 
                    " (Class: " + h.getHeroClass() + 
                    ", Lvl " + h.getLevel() + 
                    ", HP: " + h.getHp() + 
                    ", MP: " + h.getMana() + 
                    ", Str: " + h.getStrength() + 
                    ", Agi: " + h.getAgility() + 
                    ", Dex: " + h.getDexterity() + 
                    ", Money: " + h.getMoney() + 
                    ", Exp: " + h.getExperience() + ")");
            }
            
            int choice = -1;
            while (choice < 1 || choice > heroes.size()) {
                output.print("Enter choice: ");
                try {
                    String in = input.readLine();
                    choice = Integer.parseInt(in);
                    if (choice < 1 || choice > heroes.size()) {
                        output.println("Invalid choice.");
                    } else {
                        Hero selected = heroes.get(choice - 1);
                        party.addHero(selected);
                        heroes.remove(choice - 1);
                        output.println(selected.getName() + " added to party.");
                    }
                } catch (NumberFormatException e) {
                    output.println("Invalid input.");
                }
            }
        }
    }

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
                if (placed) break;
            }
            
            if (!placed) {
                output.println("Warning: Could not place party due to lack of space!");
            } else {
                output.println("Party placed on board.");
            }
        }
    }

    private void checkEncounter() {
        java.util.Random rand = new java.util.Random();
        // 50% chance of encounter
        if (rand.nextInt(100) < 50) {
            output.println("You have encountered monsters!");
            
            // Create monsters for battle
            List<Monster> battleMonsters = new ArrayList<>();
            int maxLevel = 1;
            for (Hero h : party.getHeroes()) {
                if (h.getLevel() > maxLevel) maxLevel = h.getLevel();
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
                        newMonster = new Spirit(template.getName(), template.getLevel(), template.getDamage(), template.getDefense(), template.getDodgeChance());
                    } else if (template instanceof Dragon) {
                        newMonster = new Dragon(template.getName(), template.getLevel(), template.getDamage(), template.getDefense(), template.getDodgeChance());
                    } else if (template instanceof Exoskeleton) {
                        newMonster = new Exoskeleton(template.getName(), template.getLevel(), template.getDamage(), template.getDefense(), template.getDodgeChance());
                    }
                    
                    if (newMonster != null) {
                        // Ensure HP is set correctly for the level
                        newMonster.setHp(newMonster.getLevel() * 100);
                        battleMonsters.add(newMonster);
                    }
                }
            }
            
            if (!battleMonsters.isEmpty()) {
                Battle battle = new Battle(party, battleMonsters, input, output);
                String battleResult = battle.start();

                if (battleResult.equals("Defeat")) {
                    output.println("Your party has been defeated! Game Over.");
                    gameRunning = false;
                }

                if (battleResult.equals("Victory")) {
                    output.println("You won the battle!");
                }
            }
        }
    }

    private void showHeroes() {
        output.println("\n--- Heroes ---");
        for (Hero h : heroes) {
            output.println(h);
        }
    }

    private void showMonsters() {
        output.println("\n--- Monsters ---");
        for (Monster m : monsters) {
            output.println(m);
        }
    }

    private void showItems() {
        output.println("\n--- Items ---");
        for (Item i : items) {
            output.println(i.getName() + " (Cost: " + i.getCost() + ")");
        }
    }

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

        if (heroIdx == party.getSize()) return;
        if (heroIdx < 0 || heroIdx >= party.getSize()) {
            output.println("Invalid selection.");
            return;
        }

        manageHero(party.getHero(heroIdx));
    }

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

    private void showHeroStats(Hero h) {
        output.println("\n--- Stats for " + h.getName() + " ---");
        output.println("Class: " + h.getHeroClass());
        output.println("Level: " + h.getLevel());
        output.println("HP: " + h.getHp());
        output.println("Mana: " + h.getMana());
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

    private void manageInventory(Hero hero) {
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

        if (itemIdx == inv.size()) return;
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
            output.println("1. Equip Main Hand");
            output.println("2. Equip Off Hand");
            output.print("Choose slot: ");
            String slot = input.readLine();
            if (slot.equals("1")) {
                hero.equipMainHand((Weapon) item);
                output.println("Equipped " + item.getName() + " in Main Hand.");
            } else if (slot.equals("2")) {
                if (hero.equipOffHand((Weapon) item, output)) {
                    output.println("Equipped " + item.getName() + " in Off Hand.");
                }
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
        
        if (targetIdx == targets.size()) return;
        if (targetIdx < 0 || targetIdx >= targets.size()) {
            output.println("Invalid selection.");
            return;
        }
        
        Hero targetHero = targets.get(targetIdx);
        if (targetHero.getLevel() < item.getRequiredLevel()) {
            output.println(targetHero.getName() + " is not high enough level to use this item (Required: " + item.getRequiredLevel() + ").");
            return;
        }
        
        sourceHero.removeItem(item);
        targetHero.addItem(item);
        output.println("Gave " + item.getName() + " to " + targetHero.getName() + ".");
    }
}
