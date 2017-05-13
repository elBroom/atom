package ru.atom.game.network;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import ru.atom.game.game.GameSession;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionPool {
    private static final Logger log = LogManager.getLogger(ConnectionPool.class);
    private static final ConnectionPool instance = new ConnectionPool();
    private static final int PARALLELISM_LEVEL = 4;

    private final ConcurrentHashMap<Session, Pair<GameSession, Integer>> pool;

    public static ConnectionPool getInstance() {
        return instance;
    }

    private ConnectionPool() {
        pool = new ConcurrentHashMap<>();
    }

    public void send(@NotNull Session session, @NotNull String msg) {
        if (session.isOpen()) {
            try {
                session.getRemote().sendString(msg);
            } catch (IOException ignored) {
            }
        }
    }

    public void broadcast(@NotNull GameSession gameSession, @NotNull String msg) {
        pool.entrySet().stream().filter(entry -> entry.getValue().getKey()
                .equals(gameSession)).map(Map.Entry::getKey).forEach(session -> send(session, msg));
    }

    public void shutdown() {
        pool.forEachKey(PARALLELISM_LEVEL, session -> {
            if (session.isOpen()) {
                session.close();
            }
        });
    }

    public GameSession getGameSession(Session session) {
        return pool.get(session).getKey();
    }

    public Integer getPlayerId(Session session) {
        return pool.get(session).getValue();
    }

    public Iterator<Session> getSessions(GameSession gameSession) {
        return pool.entrySet().stream()
                .filter(entry -> entry.getValue().getKey().equals(gameSession))
                .map(Map.Entry::getKey).iterator();
    }

    public void add(Session session, GameSession gameSession, Integer playerId) {
        if (pool.putIfAbsent(session, new Pair<>(gameSession, playerId)) == null) {
            log.info("add new connection");
        }
    }

    public void remove(Session session) {
        pool.remove(session);
    }
}
