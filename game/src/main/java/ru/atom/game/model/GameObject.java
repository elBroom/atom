package ru.atom.game.model;

import ru.atom.game.geometry.Bar;

/**
 * Any entity of game mechanics
 */
public interface GameObject {
    /**
     * Unique id
     */
    int getId();

    String getType();

    Bar getBar();
}
