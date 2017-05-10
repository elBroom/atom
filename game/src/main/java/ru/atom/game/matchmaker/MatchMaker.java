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

            if (candidates.size() == PLAYERS_IN_GAME) {
                Transaction txn = null;
                try (Session session = Database.session()) {
                    txn = session.beginTransaction();
                    Game game = new Game().setSublink(idMatch).setAllUsers(candidates);
                    for (User candidate: candidates) {
                        memory.put(candidate, idMatch);
                    }
                    GameDao.getInstance().insert(session, game);
                    txn.commit();
                } catch (RuntimeException e) {
                    log.error("Transaction failed.", e);
                    if (txn != null && txn.isActive()) {
                        txn.rollback();
                    }
                }
                idMatch = stringGenerate();
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
