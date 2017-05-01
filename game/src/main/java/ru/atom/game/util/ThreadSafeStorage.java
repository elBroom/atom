package ru.atom.game.util;

import ru.atom.game.matchmaker.GameSession;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sergey on 3/15/17.
 */
public class ThreadSafeStorage {
    private static ConcurrentHashMap<Long, GameSession> map = new ConcurrentHashMap<>();

    public static void put(GameSession session) {
        map.put(session.getId(), session);
    }

    public static Collection<GameSession> getAll() {
        return map.values();
    }
}
