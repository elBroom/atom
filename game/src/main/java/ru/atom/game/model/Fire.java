package ru.atom.game.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.atom.game.geometry.Point;

/**
 * Created by zarina on 01.05.17.
 */
public class Fire implements Positionable, Tickable {
    private static final Logger log = LogManager.getLogger(Fire.class);
    private final int id;
    private Point position;
    private long lifeTime;

    public Fire(Point position) {
        this.id = GameSession.createId();
        this.position = position;
        log.info("create object id={}, x={}, y={}", id, position.getX(), position.getY());
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public void tick(long elapsed) {
        lifeTime += elapsed;
    }
}
