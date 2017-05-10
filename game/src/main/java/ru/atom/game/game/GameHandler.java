package ru.atom.game.game;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import ru.atom.game.dao.Database;
import ru.atom.game.dao.TokenDao;
import ru.atom.game.model.Token;
import ru.atom.game.model.User;
import ru.atom.game.network.Broker;
import ru.atom.game.network.ConnectionPool;

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
            try (org.hibernate.Session session = Database.session()) {
                String s_match = params.get(0).substring(0, 36);
                String s_token = params.get(0).substring(36);
                System.out.println(s_match);
                Token token = TokenDao.getInstance().getByToken(session, s_token);
                if (token == null) {
                    log.info("Token not found");
                    sess.close();
                } else {
                    User player = token.getUser();
                    ConnectionPool.getInstance().add(sess, player.getName());
                    log.info("add to ConnectionPool");
                }
            } catch (RuntimeException e) {
                    log.error("Transaction failed.", e);
                    sess.close();
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
