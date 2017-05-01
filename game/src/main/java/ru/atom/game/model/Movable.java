package ru.atom.game.model;

import ru.atom.game.geometry.Point;

/**
 * GameObject that can move during game
 */
public interface Movable extends Positionable, Tickable {
    /**
     * Tries to move entity towards specified direction
     * @return final position after movement
     */
    Point move(Direction direction);
    
    enum Direction {
        UP, DOWN, RIGHT, LEFT, IDLE
    }
}
