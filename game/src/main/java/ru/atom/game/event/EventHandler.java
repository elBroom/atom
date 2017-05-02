package ru.atom.game.event;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.hibernate.Transaction;
import ru.atom.game.dao.Database;
import ru.atom.game.dao.TokenDao;
import ru.atom.game.matchmaker.MatchMaker;
import ru.atom.game.message.Topic;
import ru.atom.game.model.Token;
import ru.atom.game.model.User;
import ru.atom.game.network.Broker;
import ru.atom.game.network.ConnectionPool;

import java.util.List;

public class EventHandler extends WebSocketAdapter {
    private static final Logger log = LogManager.getLogger(EventHandler.class);

    @Override
    public void onWebSocketConnect(Session sess) {
        super.onWebSocketConnect(sess);
        List<String> tokens = sess.getUpgradeRequest().getParameterMap().get("token");
        if (tokens.isEmpty()) {
            log.info("Params empty");
            sess.close();
        } else {
            Transaction txn = null;
            try (org.hibernate.Session session = Database.session()) {
                txn = session.beginTransaction();
                Token token = TokenDao.getInstance().getByToken(session, tokens.get(0));
                if (token == null) {
                    log.info("Token not found");
                    sess.close();
                } else {
                    User player = token.getUser();
                    ConnectionPool.getInstance().add(sess, player.getName());
                    log.info("add to ConnectionPool");
                }
                txn.commit();
            } catch (RuntimeException e) {
                if (txn != null && txn.isActive()) {
                    log.error("Transaction failed.", e);
                    txn.rollback();
                    sess.close();
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
