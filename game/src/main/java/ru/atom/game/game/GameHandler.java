package ru.atom.game.game;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import ru.atom.game.controller.Ticker;
import ru.atom.game.network.Broker;
import ru.atom.game.network.ConnectionPool;
import ru.atom.game.network.GameSessionPool;
import ru.atom.game.util.ThreadSafeQueueTicker;

import java.util.List;

public class GameHandler extends WebSocketAdapter {
    private static final Logger log = LogManager.getLogger(GameHandler.class);

    @Override
    public void onWebSocketConnect(Session sess) {
        super.onWebSocketConnect(sess);
        List<String> params = sess.getUpgradeRequest().getParameterMap().get("id");
        if (params.isEmpty()) {
            log.info("Params empty");
            sess.close();
        } else {
            GameSession gameSession = GameSessionPool.getInstance().getGameSession(params.get(0));
            if (gameSession == null) {
                log.info("GameSession not found");
                sess.close();
            } else {
                // Link work only one time
                GameSessionPool.getInstance().remove(params.get(0));
                ConnectionPool.getInstance().add(sess, gameSession, gameSession.getIdPlayer());

                if (GameSessionPool.getInstance().countGameSession(gameSession) < 1) {
                    log.info("Add ticker");
                    ThreadSafeQueueTicker.getInstance().offer(new Ticker(gameSession));
                }
            }

        }
    }

    @Override
    public void onWebSocketText(String message) {
        super.onWebSocketText(message);
        log.info("Received TEXT message: " + message);
        Broker.getInstance().receive(getSession(), message);
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode, reason);
        log.info("Socket Closed: [" + statusCode + "] " + reason);
        ConnectionPool.getInstance().remove(getSession());
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        super.onWebSocketError(cause);
        cause.printStackTrace(System.err);
    }
}
