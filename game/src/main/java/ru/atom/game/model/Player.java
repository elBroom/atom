package ru.atom.game.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.atom.game.game.GameSession;
import ru.atom.game.geometry.Bar;
import ru.atom.game.geometry.Point;

/**
 * Created by zarina on 14.03.17.
 */
public class Player implements Movable {
    private static final Logger log = LogManager.getLogger(Player.class);
    private int id;
    private Point position;
    private PlayerType playerType;
    private int speed;
    private long lifeTime;
    private String type = "Pawn";

    public enum PlayerType {
        BOY, GIRL
    }

    public Player(Point position, PlayerType type, int speed) {
        if (speed <= 0) {
            throw new IllegalArgumentException("Can't support negative speed");
        }
        this.id = GameSession.createId();
        this.position = new Point(position.getX() * sizeTile, position.getY() * sizeTile);
        this.playerType = type;
        this.speed = speed;
        log.info("create object id={}, x={}, y={}, type={}, spped={}", id, position.getX(), position.getY(),
                type, speed);
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
        int delta = 4;
        return new Bar(new Point(getPosition().getX() + delta, getPosition().getY() + delta),
                new Point(getPosition().getX() + sizeTile - delta, getPosition().getY() + sizeTile - delta));
    }

    @Override
    public void setPosition(Point position) {
        this.position = position;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public void tick(long elapsed) {
        lifeTime += elapsed;
    }

    @Override
    public Point move(Direction direction) {
        switch (direction) {
            case UP:
                position = new Point(position.getX(), position.getY() + speed);
                break;
            case DOWN:
                position = new Point(position.getX(), position.getY() - speed);
                break;
            case RIGHT:
                position = new Point(position.getX() + speed, position.getY());
                break;
            case LEFT:
                position = new Point(position.getX() - speed, position.getY());
                break;
            default:
                break;
        }
        return position;
    }

    public Bomb plantBomb() {
        return new Bomb(getPosition(), 3000);
    }
}
