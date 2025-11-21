package com.legends.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Party {
    private List<Hero> heroes;

    public Party() {
        this.heroes = new ArrayList<>();
    }

    public void addHero(Hero hero) {
        this.heroes.add(hero);
    }

    public List<Hero> getHeroes() {
        return Collections.unmodifiableList(heroes);
    }

    public Hero getHero(int index) {
        return heroes.get(index);
    }

    public boolean contains(Hero hero) {
        return heroes.contains(hero);
    }

    public int getSize() {
        return heroes.size();
    }

    public boolean isEmpty() {
        return heroes.isEmpty();
    }

    public Hero getLeader() {
        if (heroes.isEmpty()) {
            return null;
        }
        return heroes.get(0);
    }

    public void setLocation(int x, int y) {
        for (Hero hero : heroes) {
            hero.setX(x);
            hero.setY(y);
        }
    }
}
