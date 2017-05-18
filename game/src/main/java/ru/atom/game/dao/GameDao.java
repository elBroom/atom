package ru.atom.game.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import ru.atom.game.model.Game;

/**
 * Created by zarina on 01.05.17.
 */
public class GameDao {
    private static final Logger log = LogManager.getLogger(GameDao.class);

    private static GameDao instance = new GameDao();

    public static GameDao getInstance() {
        return instance;
    }

    private GameDao(){}

    public void insert(Session session, Game game) {
        session.saveOrUpdate(game);
    }

    public void update(Session session, Game game) {
        session.saveOrUpdate(game);
    }

    public Game getBySublink(Session session, String sublink) {
        return (Game) session
                .createQuery("from Game where sublink = :sublink")
                .setParameter("sublink", sublink)
                .uniqueResult();
    }

    public void deleteAll(Session session) {
        session.createQuery("delete Game").executeUpdate();
    }
}
