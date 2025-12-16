# CS611 Final Project: Legends of Valor

**Course:** CS611 - Object-Oriented Design  
**Semester:** Fall 2025  
**Submission:** Final Project  

### Teammates
*   [Teammate 1 Name]
*   [Teammate 2 Name]
*   [Teammate 3 Name]

---

# Legends of Valor

Legends of Valor is a MOBA-style (Multiplayer Online Battle Arena) strategy game where a team of Heroes battles against a team of Monsters. The game takes place on a grid-based board divided into lanes, with the ultimate goal of reaching the opponent's Nexus.

## Project Structure

The project is organized into the following packages:

- `com.legends.game`: Contains the core game logic and loops.
  - `GameValor`: The main implementation of the Legends of Valor game.
  - `GameMonstersAndHeroes`: The implementation of the previous RPG game.
  - `RPGGame`: Abstract base class for RPG games.
- `com.legends.board`: Manages the game board and tiles.
  - `ValorBoard`: Specialized board for Legends of Valor with lanes and specific tile types.
  - `tiles`: Contains tile definitions (`BushTile`, `CaveTile`, `KoulouTile`, `NexusTile`, etc.).
- `com.legends.model`: Contains the core data models.
  - **Entities**: `Hero` (Paladin, Sorcerer, Warrior), `Monster` (Spirit, Dragon, Exoskeleton).
  - **Items**: `Weapon`, `Armor`, `Potion`, `Spell`.
- `com.legends.ai`: Artificial Intelligence for monsters.
  - `ValorMonsterAI`: Strategy for monsters in Legends of Valor (move forward, attack).
- `com.legends.io`: Input/Output abstraction (`ConsoleInput`, `ConsoleOutput`).
- `com.legends.utils`: Utility classes (DataLoader, SoundManager).
- `com.legends`: Entry point (`Main`).

## Features

- **MOBA Gameplay**:
  - **Lanes**: The board is divided into 3 lanes. Heroes and Monsters are restricted to their lanes unless they teleport.
  - **The Nexus**: Each side has a Nexus. Heroes start at the bottom (Row 7), Monsters at the top (Row 0).
  - **Objective**: The first team to move a unit into the opposing team's Nexus wins.
- **Terrain Bonuses**:
  - **Bush**: Increases Dexterity by 10%.
  - **Cave**: Increases Agility by 10%.
  - **Koulou**: Increases Strength by 10%.
  - **Plain**: Standard terrain.
  - **Obstacle**: Impassable terrain.
- **Hero Actions**:
  - **Move**: Move to an adjacent accessible tile.
  - **Attack**: Attack an enemy in range.
  - **Teleport**: Move to a different lane (cannot teleport to the same lane or behind enemies).
  - **Recall**: Return to the home Nexus to heal and shop.
  - **Trade**: Buy and sell items at the Nexus.
- **Monster AI**: Monsters automatically advance towards the Hero Nexus and attack heroes in range.
- **Respawn Mechanics**: Heroes respawn at their Nexus after being defeated.
- **Leveling**: Heroes gain experience from battles and level up, increasing their stats.

## Data Files

The game data is loaded from CSV files located in `src/main/resources`:
- `Paladins.csv`, `Sorcerers.csv`, `Warriors.csv`
- `Spirits.csv`, `Dragons.csv`, `Exoskeletons.csv`
- `Weaponry.csv`, `Armory.csv`, `Potions.csv`
- `FireSpells.csv`, `IceSpells.csv`, `LightningSpells.csv`

## Prerequisites

- Java Development Kit (JDK) 8 or higher.
- A terminal or command prompt.

## How to Run

### Using the Helper Script (Recommended)
The project includes a script to compile and run the game automatically:

```bash
./run.sh
```

### Manual Compilation
1. Compile the project:
   ```bash
   mkdir -p bin
   find src -name "*.java" | xargs javac -d bin
   cp -r src/main/resources/* bin/
   ```

2. Run the game:
   ```bash
   java -cp bin com.legends.Main
   ```

## Controls

- **W/A/S/D**: Move Up/Left/Down/Right.
- **T**: Teleport to another lane.
- **B**: Back/Recall to Nexus.
- **I**: Open Info Menu (View stats).
- **H**: Open Hero Menu (Equip items, use potions).
- **M**: Enter Market (Only available on Nexus tiles).
- **Q**: Quit Game.
- **Battle Controls**: Follow on-screen prompts to Attack, Cast Spells, Use Potions, or Change Equipment.

## Game Flow

1. **Start Game**: Launch the application and select "Legends of Valor".
2. **Hero Selection**: Choose 3 heroes to form your party.
3. **Lane Assignment**: Heroes are assigned to lanes (Top, Mid, Bot).
4. **Rounds**:
   - **Hero Turn**: Each hero takes an action (Move, Attack, Teleport, etc.).
   - **Monster Turn**: Monsters move or attack.
   - **Round End**: Heroes regain some HP/Mana. New monsters may spawn every 8 rounds.
5. **Victory/Defeat**: Reach the enemy Nexus to win. If a monster reaches your Nexus, you lose.

## Design Patterns

The project utilizes several key design patterns to ensure modularity, extensibility, and maintainability:

- **Factory Pattern**: Used in `ItemFactory` and `MonsterFactory` to create objects without specifying their concrete classes, decoupling game logic from object instantiation.
- **Singleton Pattern**: Implemented in `SoundManager` to ensure a single instance manages audio resources globally.
- **Strategy Pattern**: Used in `MonsterAI` to allow interchangeable behaviors for monsters (e.g., `ValorMonsterAI` vs. `RpgMonsterAI`) depending on the game mode.
- **Template Method Pattern**: `RPGGame` defines the skeleton of the game structure, while subclasses like `GameValor` implement specific game loops and rules.
- **Abstract Factory Pattern**: `ItemFactory` groups the creation of related item families (Weapons, Armor, Potions, Spells).
- **Facade Pattern**: `GameInterface` acts as a facade, orchestrating interactions between the board, entities, and I/O, simplifying the main entry point.
