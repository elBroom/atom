package ru.atom.game.matchmaker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.atom.game.dao.Database;
import ru.atom.game.dao.GameDao;
import ru.atom.game.model.Game;
import ru.atom.game.model.User;
import ru.atom.game.util.ThreadSafeQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by sergey on 3/14/17.
 */
public class MatchMaker implements Runnable {
    private static final Logger log = LogManager.getLogger(MatchMaker.class);
    public static final int PLAYERS_IN_GAME = 4;
    private static String idMatch;
    private  static ConcurrentHashMap<User, String> memory = new ConcurrentHashMap<User, String>();

    @Override
    public void run() {
        log.info("Started");
        List<User> candidates = new ArrayList<>(PLAYERS_IN_GAME);
        idMatch = stringGenerate();
        while (!Thread.currentThread().isInterrupted()) {
            try {
                candidates.add(
                        ThreadSafeQueue.getInstance().poll(10_000, TimeUnit.SECONDS)
                );
            } catch (InterruptedException e) {
                log.warn("Timeout reached");
            }

            // TODO getLink from GameServer
            // Отправляем 4х пользователей
            // Получаем уникальные ссылки для каждого пользователя и расскладываем ссылки в memory
            if (candidates.size() == PLAYERS_IN_GAME) {

                candidates.clear();
            }
        }
    }

    public static String getIdMatch() {
        return idMatch;
    }

    public static String getLink(User user) {
        return memory.get(user);
    }

    public static String popLink(User user) {
        String link = memory.get(user);
        memory.remove(user);
        return link;
    }



    private String stringGenerate() {
        return UUID.randomUUID().toString();
    }
}
