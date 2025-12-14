# Class Design Document

## 1. Overview
This document describes the class structure and relationships for the "Legends: Monsters and Heroes" game. The project follows an object-oriented design with a clear separation of concerns between the game logic, data models, and input/output handling.

## 2. Package Structure
The source code is organized into the following packages:
- `com.legends`: Contains the main entry point.
- `com.legends.Game`: Contains the core game logic, board management, and battle system.
- `com.legends.model`: Contains the data models for entities (heroes, monsters), items, and the party.
- `com.legends.io`: Contains interfaces and implementations for user input and output.
- `com.legends.utils`: Contains utility classes for data loading.

## 3. Class Descriptions and Relationships

### 3.1. Core Game Logic (`com.legends.Game`)

#### `Game`
- **Description**: The central controller of the application. It manages the game loop, initializes the game state, handles user input for the main menu and game actions, and coordinates interactions between the board, party, and battles.
- **Responsibilities**:
  - Initialize game data (heroes, monsters, items).
  - Manage the main game loop.
  - Handle player movement and interactions on the board.
  - Trigger battles and market visits.
  - Save and load game state.
- **Relationships**:
  - Has a `Board`.
  - Has a `Party`.
  - Has lists of available `Hero`, `Monster`, and `Item` objects.
  - Uses `Input` and `Output` interfaces for interaction.
  - Uses `DataLoader` to populate initial data.

#### `Board`
- **Description**: Represents the game map as a grid of tiles.
- **Responsibilities**:
  - Generate a random map layout.
  - Ensure map connectivity.
  - Manage entity placement and movement.
  - Print the board state.
- **Relationships**:
  - Contains a 2D array of `Tile` objects.
  - Uses `Entity` to track positions.

#### `Tile` (Abstract)
- **Description**: Represents a single cell on the board.
- **Subclasses**:
  - `CommonTile`: A standard tile where battles can occur.
  - `MarketTile`: A tile containing a market.
  - `InaccessibleTile`: A tile that cannot be entered.
- **Responsibilities**:
  - Store its coordinate (x, y).
  - Hold a reference to an `Entity` if occupied.
  - Define accessibility.

#### `Battle`
- **Description**: Manages a turn-based combat encounter.
- **Responsibilities**:
  - Manage rounds of combat.
  - Handle hero and monster turns.
  - Calculate damage, healing, and stat changes.
  - Determine victory or defeat.
- **Relationships**:
  - Uses `Party` (heroes).
  - Uses a list of `Monster`s.
  - Uses `Input` and `Output`.

### 3.2. Data Models (`com.legends.model`)

#### `Entity` (Abstract)
- **Description**: The base class for all living characters.
- **Responsibilities**:
  - Store common attributes: name, level, HP, position (x, y).
  - specific methods like `takeDamage`, `isAlive`.
- **Subclasses**: `Hero`, `Monster`.

#### `Hero` (Abstract)
- **Description**: Represents a player-controlled character.
- **Responsibilities**:
  - Manage stats: Mana, Strength, Agility, Dexterity, Money, Experience.
  - Manage inventory (List of `Item`).
  - Equip weapons and armor.
  - Level up logic.
- **Subclasses**:
  - `Warrior`: Favors Strength and Agility.
  - `Sorcerer`: Favors Dexterity and Agility.
  - `Paladin`: Favors Strength and Dexterity.

#### `Monster` (Abstract)
- **Description**: Represents an enemy character.
- **Responsibilities**:
  - Manage stats: Damage, Defense, Dodge Chance.
- **Subclasses**:
  - `Dragon`: High damage.
  - `Exoskeleton`: High defense.
  - `Spirit`: High dodge chance.

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