# Class Design Document

## 1. Overview
This document describes the class structure and relationships for the "Legends: Monsters and Heroes" game. The project follows an object-oriented design with a clear separation of concerns between the game logic, data models, and input/output handling.

## 2. Package Structure
The source code is organized into the following packages:
- `com.legends`: Contains the main entry point.
- `com.legends.game`: Contains the core game logic and controllers.
- `com.legends.board`: Contains board and tile management.
- `com.legends.model`: Contains the data models for entities, items, and factories.
- `com.legends.battle`: Contains the battle system logic.
- `com.legends.ai`: Contains AI logic for monsters.
- `com.legends.market`: Contains market logic.
- `com.legends.io`: Contains interfaces and implementations for user input and output.
- `com.legends.ui`: Contains UI rendering and styling classes.
- `com.legends.utils`: Contains utility classes for data loading and audio.

## 3. Class Descriptions and Relationships

### 3.1. Core Game Logic (`com.legends.game`)

#### `GameInterface` (Abstract)
- **Description**: The base controller for any game type.
- **Responsibilities**:
  - Manage basic input/output.
  - Define the contract for starting a game.

#### `RPGGame` (Abstract)
- **Description**: Extends `GameInterface` to provide common RPG functionality.
- **Responsibilities**:
  - Handle hero selection.
  - Manage common actions like using potions.
  - Display final statistics.
- **Subclasses**: `GameValor`, `GameMonstersAndHeroes`.

#### `GameValor`
- **Description**: The main controller for "Legends of Valor".
- **Responsibilities**:
  - Manage the lane-based MOBA-style gameplay.
  - Handle hero movement, teleportation, and recalling.
  - Manage monster spawning and movement in lanes.
  - Check victory conditions (reaching Nexus).
- **Relationships**:
  - Uses `ValorBoard`.
  - Uses `ValorMonsterAI`.

#### `GameMonstersAndHeroes`
- **Description**: The controller for the original "Monsters and Heroes" game.
- **Responsibilities**:
  - Manage the open-world exploration gameplay.
  - Trigger random battles.

### 3.2. Board and Tiles (`com.legends.board`)

#### `Board`
- **Description**: Represents the game map.
- **Responsibilities**:
  - Manage a grid of tiles.
  - Handle entity placement.

#### `ValorBoard`
- **Description**: Specialized board for Legends of Valor.
- **Responsibilities**:
  - Initialize the 8x8 grid with lanes and special tiles.
  - Manage lane logic (inaccessible columns).
  - Track heroes and monsters separately (allowing mixed occupancy).

#### `Tile` (Abstract)
- **Subclasses**:
  - `PlainTile`: Standard walkable tile.
  - `BushTile`: Increases Dexterity.
  - `CaveTile`: Increases Agility.
  - `KoulouTile`: Increases Strength.
  - `NexusTile`: Base for heroes/monsters, acts as Market for heroes.
  - `ObstacleTile`: Blocks movement, can be destroyed.
  - `InaccessibleTile`: Permanently blocks movement.

### 3.3. Battle System (`com.legends.battle`)

#### `Battle`
- **Description**: Manages combat encounters.
- **Responsibilities**:
  - Handle turn-based combat.
  - Manage actions (Attack, Spell, Potion, Equipment).
  - Distribute rewards (XP, Gold) and handle drops.

### 3.4. AI (`com.legends.ai`)

#### `MonsterAI` (Interface)
- **Description**: Defines strategy for monster behavior.
- **Implementations**:
  - `ValorMonsterAI`: Strategy for Valor (move down lanes, attack if in range).
  - `RpgMonsterAI`: Strategy for standard RPG battles.

### 3.5. Data Models (`com.legends.model`)

#### `Entity` (Abstract)
- **Description**: Base class for characters.
- **Attributes**: `name`, `level`, `hp`, `x`, `y`, `homeNexus_row`, `targetNexus_row`.

#### `Hero` (Abstract)
- **Attributes**: `mana`, `strength`, `agility`, `dexterity`, `money`, `experience`, `lane`, `originalLane`.
- **Responsibilities**:
  - Manage stats and caps (HP, Mana).
  - Manage inventory and equipment.

#### `Monster` (Abstract)
- **Attributes**: `damage`, `defense`, `dodgeChance`, `lane`.
- **Responsibilities**:
  - Use `MonsterAI` to take turns.

#### `Factories`
- `DragonFactory`, `ExoskeletonFactory`, `SpiritFactory`: Create specific monster types.

#### `Party`
- **Description**: Represents the group of heroes controlled by the player.
- **Responsibilities**:
  - Maintain a list of `Hero` objects.
  - Track the party's location on the board.
  - Manage the active party members.

#### `Item` (Abstract)
- **Description**: Base class for all purchasable and usable items.
- **Responsibilities**:
  - Store name, cost, and required level.
- **Subclasses**:
  - `Weapon`: Has damage and required hands.
  - `Armor`: Has damage reduction.
  - `Potion`: Has stat increase amount and affected attribute.
  - `Spell` (Abstract): Has damage and mana cost.
    - `FireSpell`: Reduces enemy defense.
    - `IceSpell`: Reduces enemy damage.
    - `LightningSpell`: Reduces enemy dodge chance.

### 3.3. Input/Output (`com.legends.io`)

#### `Input` (Interface)
- **Description**: Defines methods for reading user input.
- **Implementation**: `ConsoleInput`.

#### `Output` (Interface)
- **Description**: Defines methods for displaying information to the user.
- **Implementation**: `ConsoleOutput`.

### 3.4. Utilities (`com.legends.utils`)

#### `DataLoader`
- **Description**: Helper class to parse CSV files.
- **Responsibilities**:
  - Read configuration files for heroes, monsters, and items.
  - Create instances of model classes from file data.