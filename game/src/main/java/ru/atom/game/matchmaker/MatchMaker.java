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
import java.util.Optional;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by sergey on 3/14/17.
 */
public class MatchMaker implements Runnable {
    private static final Logger log = LogManager.getLogger(MatchMaker.class);
    public static final int PLAYERS_IN_GAME = 4;
    private  static ConcurrentHashMap<User, Pair<String, String>> memory = new ConcurrentHashMap<>();

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
                } catch (IOException e) {
                    log.info("IOException");
                }
                if (links.isEmpty()) {
                    log.info("Links Empty");
                } else {
                    log.info("Save links");
                    for (User candidate: candidates) {
                        memory.put(candidate, new Pair<>(gsInfo.getValue(), links.pop()));
                    }
                }
                candidates.clear();
            }
        }
    }

    public static Pair<String, String> popLink(User user) {
        return memory.remove(user);
    }

    public static Optional<Pair<String, String>> tryPopLink(User user, int attemps) {
        Pair<String, String> link = null;
        try {
            while (null == (link = popLink(user)) && attemps-- > 0) {
                Thread.sleep(100);
            }
        } catch (InterruptedException ignored) {
        }

        return Optional.ofNullable(link);
    }

}
