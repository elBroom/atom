package ru.atom.game.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.atom.game.geometry.Point;

import java.util.ArrayList;
import java.util.List;

public class GameSession implements Tickable {
    private static final int MAX_X = 17;
    private static final int MAX_Y = 13;
    private static final Logger log = LogManager.getLogger(GameSession.class);
    private static int id = 0;
    private List<GameObject> gameObjects = new ArrayList<>();
    private List<Player> players = new ArrayList<>();
    // TODO Save Action PLANT_BOMB, MOVE

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

    public void init() {
        for (int x = 0; x < MAX_X; x++) {
            for (int y = 0; y < MAX_Y; y++) {
                if (x % 2 == 0 && y % 2 == 0 || y == 0 || y == MAX_Y - 1 || x == 0 || x == MAX_X - 1) {
                    // Create Wall
                    addGameObject(new Wall(new Point(x, y)));
                } else if (!((x == 1 || x == MAX_X - 2) && (y < 3 || y > MAX_Y - 4) || (x == 2 || x== MAX_X - 3) &&
                           (y == 1 || y == MAX_Y - 2)))  {
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
    }

    @Override
    // TODO reaction on action
    public void tick(long elapsed) {
        log.info("tick");
        ArrayList<Temporary> dead = new ArrayList<>();
        for (GameObject gameObject : gameObjects) {
            if (gameObject instanceof Tickable) {
                ((Tickable) gameObject).tick(elapsed);
            }
            if (gameObject instanceof Temporary && ((Temporary) gameObject).isDead()) {
                dead.add((Temporary)gameObject);
            }
        }
        gameObjects.removeAll(dead);
    }

    // TODO get object with change state
}
