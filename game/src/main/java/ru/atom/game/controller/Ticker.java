package ru.atom.game.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import ru.atom.game.message.Topic;
import ru.atom.game.model.GameSession;
import ru.atom.game.network.Broker;
import ru.atom.game.network.ConnectionPool;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class Ticker {
    private static final Logger log = LogManager.getLogger(Ticker.class);
    private static final int FPS = 60;
    private static final long FRAME_TIME = 1000 / FPS;
    private long tickNumber = 0;
    private static Object lock = new Object();
    private GameSession gameSession;

    public Ticker(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    public void init() {
        log.info("gameSession init");
        gameSession.init();
        for(Map.Entry<Session, String> entry : ConnectionPool.getInstance().getPlayers()) {
            Broker.getInstance().send(entry.getValue(), Topic.POSSESS, gameSession.getIdPlayer());
        }
    }

    public void loop() {
        while (!Thread.currentThread().isInterrupted()) {
            long started = System.currentTimeMillis();
            act(FRAME_TIME);
            long elapsed = System.currentTimeMillis() - started;
            if (elapsed < FRAME_TIME) {
                log.info("All tick finish at {} ms", elapsed);
                LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(FRAME_TIME - elapsed));
            } else {
                log.warn("tick lag {} ms", elapsed - FRAME_TIME);
            }
            log.info("{}: tick ", tickNumber);
            tickNumber++;
        }
    }

    private void act(long time) {
        synchronized (lock) {
            gameSession.tick(time);
            Broker.getInstance().broadcast(Topic.REPLICA, gameSession.getGameObjects());
        }
    }

    public long getTickNumber() {
        return tickNumber;
    }
}