package ru.atom.game.network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.atom.game.game.GameSession;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zarina on 13.05.17.
 */
public class GameSessionPool {
    private static final Logger log = LogManager.getLogger(GameSessionPool.class);
    private static final GameSessionPool instance = new GameSessionPool();

    private final ConcurrentHashMap<String, GameSession> pool;

    public static GameSessionPool getInstance() {
        return instance;
    }

    private GameSessionPool() {
        pool = new ConcurrentHashMap<>();
    }

    public void add(String link, GameSession gameSession) {
        if (pool.putIfAbsent(link, gameSession) == null) {
            log.info("add link to gamePool ");
        }
    }

    public GameSession getGameSession(String link) { return  pool.get(link); }


    public int countGameSession(GameSession gameSession) {
        int i = 0;
        for (GameSession gs: pool.values()) {
            if (gs.equals(gameSession)) {
                i++;
            }
        }
        return i;
    }

    public void remove(String link) {
        pool.remove(link);
    }
}
