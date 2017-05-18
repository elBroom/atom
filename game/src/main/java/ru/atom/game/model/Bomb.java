package ru.atom.game.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.atom.game.game.GameSession;
import ru.atom.game.geometry.Bar;
import ru.atom.game.geometry.Point;

/**
 * Created by zarina on 14.03.17.
 */
public class Bomb implements Positionable, Temporary {
    private static final Logger log = LogManager.getLogger(Bomb.class);
    private final int id;
    private Point position;
    private long lifeTime;
    private String type = "Bomb";
    private Bar bar;

    public Bomb(Point position, long lifeTime) {
        if (lifeTime <= 0) {
            throw new IllegalArgumentException("Can't support negative lifeTime");
        }
        this.id = GameSession.createId();;
        this.position = position;
        this.lifeTime = lifeTime;
        this.bar = new Bar(new Point(position.getX() * sizeTile, position.getY() * sizeTile),
                new Point(position.getX() * sizeTile + sizeTile, position.getY() * sizeTile + sizeTile));
        log.info("create object id={}, x={}, y={}, lifeTime={}", id, position.getX(), position.getY(),
                lifeTime);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public Bar getBar() {
        return bar;
    }

    @Override
    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
    public void tick(long elapsed) {
        lifeTime -= elapsed;
    }

    @Override
    public long getLifetimeMillis() {
        return lifeTime;
    }

    @Override
    public boolean isDead() {
        return lifeTime <= 0;
    }
}
