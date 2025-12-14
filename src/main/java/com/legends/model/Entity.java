package com.legends.model;

import java.io.Serializable;

/**
 * Abstract base class for all living entities in the game (Heroes and Monsters).
 * Manages common attributes like name, level, HP, and position.
 */
public abstract class Entity implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String name;
    protected int level;
    protected int hp;
    protected int x;
    protected int y;
    protected int homeNexus_row;
    protected int targetNexus_row;
    protected final int range = 1;

    /**
     * Constructs a new entity for legends of valor.
     *
     * @param name Name of entity.
     * @param level level of entity.
     * @param homeNexus_row row index of the entity's home nexus.
     * @param targetNexus_row row index of the entity's target nexus.
     */
    public Entity(String name, int level, int homeNexus_row, int targetNexus_row) {
        this.name = name;
        this.level = level;
        this.hp = level * 100; // Base HP calculation, can be overridden
        this.homeNexus_row = homeNexus_row;
        this.targetNexus_row = targetNexus_row;
    }

    /**
     * Constructs a new Entity.
     *
     * @param name  The name of the entity.
     * @param level The level of the entity.
     */
    public Entity(String name, int level) {
        this(name, level, -1, -1);
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

    /**
     * Gives the row index of the home nexus.
     * @return row index of home nexus of entity.
     */
    public int getHomeNexus_row() { return homeNexus_row; }

    /**
     * Gives the row index of the target nexus.
     * @return row index of the target nexus.
     */
    public int getTargetNexus_row() { return targetNexus_row; }

    /**
     * Moves the entity to the given coordinates.
     * @param x x coordinate to move to.
     * @param y y coordinate to move to.
     * @return boolean on weather it moved to the given coordinates or not
     */
    public boolean moveTo(int x, int y) {
        if (x>=0 && y>=0) {
            this.x = x;
            this.y = y;
            return true;
        }
        return false;
    }

    /**
     * To see if the passed target is within range or not.
     * @param target the entity to check for being in the range.
     * @return True if within range, else false.
     */
    public boolean inRange(Entity target) {
        return this.x - this.range <= target.getX() && this.y - this.range <= target.getY() && this.x + this.range >= target.getX() && this.y + this.range >= target.getY();
    }
}
