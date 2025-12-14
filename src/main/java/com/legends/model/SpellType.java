package com.legends.model;

public enum SpellType {
    FIRE, ICE, LIGHTNING;

    public static SpellType fromString(String s) {
        if (s == null) return null;
        switch (s.trim().toLowerCase()) {
            case "fire": return FIRE;
            case "ice": return ICE;
            case "lightning": return LIGHTNING;
            default: throw new IllegalArgumentException("Unknown spell type: " + s);
        }
    }
}
