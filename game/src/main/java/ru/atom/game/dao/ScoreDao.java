package ru.atom.game.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import ru.atom.game.model.Game;
import ru.atom.game.model.Score;
import ru.atom.game.model.User;

/**
 * Created by zarina on 01.05.17.
 */
public class ScoreDao {
    private static final Logger log = LogManager.getLogger(ScoreDao.class);

    private static ScoreDao instance = new ScoreDao();

    public static ScoreDao getInstance() {
        return instance;
    }

    private ScoreDao(){}

    public void insert(Session session, Score score) {
        session.saveOrUpdate(score);
    }

    public void update(Session session, Score score) {
        session.saveOrUpdate(score);
    }

    public Score getByGameAndUser(Session session, Integer game, String user) {
        return (Score) session
                .createQuery("from Score s where s.user.name = :user and s.game.id = :game")
                .setParameter("user", user)
                .setParameter("game", game)
                .uniqueResult();
    }

    public void deleteAll(Session session) {
        session.createQuery("delete Score").executeUpdate();
    }
}
