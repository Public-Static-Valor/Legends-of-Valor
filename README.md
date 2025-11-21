# Legends: Monsters and Heroes

This is a Java-based Role Playing Game (RPG) project where heroes battle monsters in a fantasy world.

## Project Structure

The project is organized into the following packages:

- `com.legends.model`: Contains the core data models (Entities, Items, Spells).
  - **Entities**: `Hero` (Paladin, Sorcerer, Warrior), `Monster` (Spirit, Dragon, Exoskeleton).
  - **Items**: `Weapon`, `Armor`, `Potion`, `Spell` (Fire, Ice, Lightning).
- `com.legends.game`: Contains the game logic.
  - `Game`: Manages the game loop, initialization, and world configuration.
  - `Board`: Represents the game board (grid) with different tile types (`Common`, `Market`, `Inaccessible`).
- `com.legends.io`: Input/Output abstraction.
  - `Input`/`Output`: Interfaces for user interaction.
  - `ConsoleInput`/`ConsoleOutput`: Console-based implementations.
- `com.legends.utils`: Utility classes.
  - `DataLoader`: Loads game data from CSV files (Factory pattern).
- `com.legends`: Entry point.
  - `Main`: Runs the application.

## Features

- **Hero Classes**: Paladins (Strength/Dexterity), Sorcerers (Dexterity/Agility), Warriors (Strength/Agility).
- **Monster Types**: Dragons (High Damage), Exoskeletons (High Defense), Spirits (High Dodge).
- **Items**: Weapons, Armor, Potions, and Spells with specific effects.
- **Gameplay Mechanics**:
  - **Leveling System**: Heroes gain XP and level up, increasing stats.
  - **Dual Wielding**: Heroes can equip two one-handed weapons.
  - **Spell Effects**: Fire (reduces defense), Ice (reduces damage), Lightning (reduces dodge chance).
  - **Dynamic World**: Configurable board size with randomized terrain.
- **IO Abstraction**: Decoupled input/output logic for better testability.

## Data Files

The game data is loaded from CSV files located in `src/main/resources`:
- `Paladins.csv`, `Sorcerers.csv`, `Warriors.csv`
- `Spirits.csv`, `Dragons.csv`, `Exoskeletons.csv`
- `Weaponry.csv`, `Armory.csv`, `Potions.csv`
- `FireSpells.csv`, `IceSpells.csv`, `LightningSpells.csv`

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
   javac -d bin -sourcepath src/main/java src/main/java/com/legends/Main.java
   cp -r src/main/resources/* bin/
   ```

2. Run the game:
   ```bash
   java -cp bin com.legends.Main
   ```

## Design Principles
- **Object-Oriented Design**: Uses inheritance, polymorphism, encapsulation, and abstraction.
- **Factory Pattern**: Used in `DataLoader` to create entities and items based on the type
- **Dependency Injection**: `Game` class receives `Input` and `Output` dependencies.
- **Scalability**: New entities and items can be added easily by creating new classes and updating the CSV files.
- **Maintainability**: Code is modular and organized into packages.
