package ru.atom.game.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.atom.game.geometry.Point;

/**
 * Created by zarina on 14.03.17.
 */
public class Bonus implements Positionable, Tickable {
    private static final Logger log = LogManager.getLogger(Bonus.class);
    private final int id;
    private Point position;
    private BonusType type;
    private long lifeTime;

    public enum BonusType {
        SNEAKERS, BOMB, BURST
    }

    public Bonus(Point position, BonusType type) {
        this.id = GameSession.createId();
        this.position = position;
        this.type = type;
        log.info("create object id={}, x={}, y={}, type={}", id, position.getX(), position.getY(), type);
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

    public BonusType getType() {
        return type;
    }
}
