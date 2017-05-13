package ru.atom.game.matchmaker;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.atom.game.game.GameClient;
import ru.atom.game.model.User;
import ru.atom.game.util.ThreadSafeQueueUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by sergey on 3/14/17.
 */
public class MatchMaker implements Runnable {
    private static final Logger log = LogManager.getLogger(MatchMaker.class);
    public static final int PLAYERS_IN_GAME = 4;
    private  static ConcurrentHashMap<User, String> memory = new ConcurrentHashMap<>();

    @Override
    public void run() {
        log.info("Started");
        List<User> candidates = new ArrayList<>(PLAYERS_IN_GAME);
        while (!Thread.currentThread().isInterrupted()) {
            try {
                candidates.add(
                    ThreadSafeQueueUser.getInstance().poll(10_000, TimeUnit.SECONDS)
                );
            } catch (InterruptedException e) {
                log.warn("Timeout reached");
            }

            // Получаем уникальные ссылки для каждого пользователя и расскладываем ссылки в memory
            if (candidates.size() == PLAYERS_IN_GAME) {
                log.info("Build game");
                LinkedList<String> links = new LinkedList<>();
                Pair<String, String> gsInfo = MatchMakerServer.getGameServer();
                try {
                    log.info("Get link from {}", gsInfo.getValue());
                    links = GameClient.createGameSession(gsInfo.getValue(), gsInfo.getKey());
                } catch (IOException e) {}
                if (links.isEmpty()) {
                    log.info("Links Empty");
                } else {
                    log.info("Save links");
                    for (User candidate: candidates) {
                        memory.put(candidate, links.pop());
                    }
                }
                candidates.clear();
            }
        }
    }

    public static String getLink(User user) {
        return memory.get(user);
    }

    public static String popLink(User user) {
        String link = memory.get(user);
        memory.remove(user);
        return link;
    }

}
