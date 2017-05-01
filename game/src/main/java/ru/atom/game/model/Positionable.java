package ru.atom.game.model;

import ru.atom.game.geometry.Point;

/**
 * GameObject that has coordinates
 * ^ Y
 * |
 * |
 * |
 * |          X
 * .---------->
 */
public interface Positionable extends GameObject {
    /**
     * @return Current position
     */
    Point getPosition();
}
