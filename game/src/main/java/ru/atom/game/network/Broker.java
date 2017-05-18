package ru.atom.game.network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import ru.atom.game.message.Message;
import ru.atom.game.message.Topic;
import ru.atom.game.game.GameSession;
import ru.atom.game.model.Movable;
import ru.atom.game.util.JsonHelper;

import java.util.HashMap;

public class Broker {
    private static final Logger log = LogManager.getLogger(Broker.class);

    private static final Broker instance = new Broker();
    private final ConnectionPool connectionPool;

    public static Broker getInstance() {
        return instance;
    }

    private Broker() {
        this.connectionPool = ConnectionPool.getInstance();
    }

    public void receive(@NotNull Session session, @NotNull String msg) {
        log.info("RECEIVED: " + msg);
        Message message = JsonHelper.fromJson(msg, Message.class);
        GameSession gameSession = connectionPool.getGameSession(session);
        Integer playerId = connectionPool.getPlayerId(session);
        if (message.getTopic() == Topic.PLANT_BOMB) {
            gameSession.addAction(GameSession.Action.PLANT_BOMB, playerId);
            log.info("receive [" + Topic.PLANT_BOMB + "] message: " + message.getData());
        } else if (message.getTopic() == Topic.MOVE) {
            HashMap<String, String> data = JsonHelper.fromJson(message.getData(), HashMap.class);
            if (data.get("direction").equals(Movable.Direction.UP.name())) {
                gameSession.addAction(GameSession.Action.MOVE_UP, playerId);
            } else if (data.get("direction").equals(Movable.Direction.DOWN.name())) {
                gameSession.addAction(GameSession.Action.MOVE_DOWN, playerId);
            } else if (data.get("direction").equals(Movable.Direction.RIGHT.name())) {
                gameSession.addAction(GameSession.Action.MOVE_RIGHT, playerId);
            } else if (data.get("direction").equals(Movable.Direction.LEFT.name())) {
                gameSession.addAction(GameSession.Action.MOVE_LEFT, playerId);
            } else if (data.get("direction").equals(Movable.Direction.IDLE.name())) {
                gameSession.addAction(GameSession.Action.MOVE_IDLE, playerId);
            }
            log.info("receive [" + Topic.MOVE + "] message: " + message.getData());
        } else {
            log.info("receive another topic");
        }
    }

    public void send(@NotNull Session session, @NotNull Topic topic, @NotNull Object object) {
        String message = JsonHelper.toJson(new Message(topic, JsonHelper.toJson(object)));
        connectionPool.send(session, message);
    }

    public void broadcast(@NotNull GameSession gameSession, @NotNull Topic topic, @NotNull Object object) {
        String message = JsonHelper.toJson(new Message(topic, JsonHelper.toJson(object)));
        connectionPool.broadcast(gameSession, message);
    }

}
