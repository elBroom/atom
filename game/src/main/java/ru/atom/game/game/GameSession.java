package ru.atom.game.game;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.atom.game.geometry.Bar;
import ru.atom.game.geometry.Point;
import ru.atom.game.model.GameObject;
import ru.atom.game.model.Player;
import ru.atom.game.model.Tickable;
import ru.atom.game.model.Wall;
import ru.atom.game.model.Wood;
import ru.atom.game.model.Temporary;
import ru.atom.game.model.Movable;
import ru.atom.game.model.Positionable;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class GameSession implements Tickable {
    private static final int MAX_X = 17;
    private static final int MAX_Y = 13;
    private static final Logger log = LogManager.getLogger(GameSession.class);
    private static int id = 0;
    private List<GameObject> gameObjects = new ArrayList<>();
    private List<Player> players = new ArrayList<>();

    private Set<Pair<Action, Integer>> actions = new LinkedHashSet<>();

    public enum Action {
        PLANT_BOMB, MOVE_UP, MOVE_DOWN, MOVE_RIGHT, MOVE_LEFT, MOVE_IDLE,
    }

    public static int createId() {
        return id++;
    }

    public List<GameObject> getGameObjects() {
        return new ArrayList<>(gameObjects);
    }

    public int getIdPlayer() {
        return players.remove(0).getId();
    }

    public void addGameObject(GameObject gameObject) {
        gameObjects.add(gameObject);
    }

    public GameSession() {
        for (int x = 0; x < MAX_X; x++) {
            for (int y = 0; y < MAX_Y; y++) {
                if (x % 2 == 0 && y % 2 == 0 || y == 0 || y == MAX_Y - 1 || x == 0 || x == MAX_X - 1) {
                    // Create Wall
                    addGameObject(new Wall(new Point(x, y)));
                } else if (!((x == 1 || x == MAX_X - 2) && (y < 3 || y > MAX_Y - 4) || (x == 2 || x == MAX_X - 3)
                        && (y == 1 || y == MAX_Y - 2)))  {
                    // Create Wood
                    addGameObject(new Wood(new Point(x, y)));
                }
            }
        }

        // Create Players
        Player player1 = new Player(new Point(1, 1), Player.PlayerType.BOY, 1);
        addGameObject(player1);
        players.add(player1);
        Player player2 = new Player(new Point(1, MAX_Y - 2), Player.PlayerType.BOY, 1);
        addGameObject(player2);
        players.add(player2);
        Player player3 = new Player(new Point(MAX_X - 2, 1), Player.PlayerType.BOY, 1);
        addGameObject(player3);
        players.add(player3);
        Player player4 = new Player(new Point(MAX_X - 2, MAX_Y - 2), Player.PlayerType.BOY, 1);
        addGameObject(player4);
        players.add(player4);
        log.info("Init GameSession");
    }

    @Override
    public void tick(long elapsed) {
        ArrayList<Temporary> dead = new ArrayList<>();
        ArrayList<GameObject> newObjects = new ArrayList<>();
        for (GameObject gameObject : gameObjects) {
            if (gameObject instanceof Tickable) {
                ((Tickable) gameObject).tick(elapsed);
            }
            if (gameObject instanceof Temporary && ((Temporary) gameObject).isDead()) {
                dead.add((Temporary)gameObject);
            }

            // log.info(actions);
            // TODO reaction on action
            for (Pair pair: actions) {
                if (gameObject instanceof Movable && gameObject.getId() == (Integer) pair.getValue()) {
                    Action action = (Action) pair.getKey();
                    move(gameObject, action);
                    switch (action) {
                        case PLANT_BOMB:
                            newObjects.add(((Player) gameObject).plantBomb());
                            break;
                        default:
                            log.warn("Illegal action {}", pair.getKey().toString());
                    }
                }
            }
        }
        gameObjects.addAll(newObjects);
        gameObjects.removeAll(dead);
        actions.clear();
    }

    public void addAction(Action action, Integer playerId) {
        actions.add(new Pair<>(action, playerId));
    }

    private void move(GameObject gameObject, Action action) {
        Point prevPosition = ((Positionable) gameObject).getPosition();
        switch (action) {
            case MOVE_UP:
                ((Movable) gameObject).move(Movable.Direction.UP);
                break;
            case MOVE_DOWN:
                ((Movable) gameObject).move(Movable.Direction.DOWN);
                break;
            case MOVE_RIGHT:
                ((Movable) gameObject).move(Movable.Direction.RIGHT);
                break;
            case MOVE_LEFT:
                ((Movable) gameObject).move(Movable.Direction.LEFT);
                break;
            default:
                return;
        }

        Bar playerBar = ((Positionable) gameObject).getBar();
        Boolean notCollision = gameObjects.stream()
                .filter(_gameObject -> ((GameObject) _gameObject).getBar().isColliding(playerBar))
                .anyMatch(_gameObject -> _gameObject instanceof Wall || _gameObject instanceof Wood);
        if (notCollision) {
            log.info("Collision");
            ((Positionable) gameObject).setPosition(prevPosition);
        }

    }
}
