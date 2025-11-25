package com.legends.model;

/**
 * Abstract base class for all living entities in the game (Heroes and Monsters).
 * Manages common attributes like name, level, HP, and position.
 */
public abstract class Entity {
    protected String name;
    protected int level;
    protected int hp;
    protected int x;
    protected int y;

    /**
     * Constructs a new Entity.
     *
     * @param name  The name of the entity.
     * @param level The level of the entity.
     */
    public Entity(String name, int level) {
        this.name = name;
        this.level = level;
        this.hp = level * 100; // Base HP calculation, can be overridden
    }

    /**
     * Gets the name of the entity.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the level of the entity.
     *
     * @return The level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets the current HP of the entity.
     *
     * @return The HP.
     */
    public int getHp() {
        return hp;
    }

    /**
     * Sets the HP of the entity.
     *
     * @param hp The new HP value.
     */
    public void setHp(int hp) {
        this.hp = hp;
    }

    /**
     * Checks if the entity is alive.
     *
     * @return True if HP > 0, false otherwise.
     */
    public boolean isAlive() {
        return hp > 0;
    }

    /**
     * Reduces the entity's HP by the specified damage amount.
     *
     * @param damage The amount of damage to take.
     */
    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp < 0) {
            this.hp = 0;
        }
    }

    /**
     * Gets the x-coordinate of the entity.
     *
     * @return The x-coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x-coordinate of the entity.
     *
     * @param x The new x-coordinate.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets the y-coordinate of the entity.
     *
     * @return The y-coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y-coordinate of the entity.
     *
     * @param y The new y-coordinate.
     */
    public void setY(int y) {
        this.y = y;
    }
}
