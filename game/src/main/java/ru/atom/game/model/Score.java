package ru.atom.game.model;


import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.CascadeType;
import javax.persistence.Column;

/**
 * Created by zarina on 01.05.17.
 */
@Entity
@Table(name = "score", schema = "game")
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @JoinColumn(name = "user_id", nullable = false)
    @OneToOne(cascade = CascadeType.PERSIST)
    private User user;

    @JoinColumn(name = "game_id", nullable = false)
    @OneToOne(cascade = CascadeType.PERSIST)
    private Game game;

    @Column(name = "value", nullable = false)
    private Integer value;

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Game getGame() {
        return game;
    }

    public Integer getValue() {
        return value;
    }

    public Score setUser(User user) {
        this.user = user;
        return this;
    }

    public Score setGame(Game game) {
        this.game = game;
        return this;
    }

    public Score setValue(Integer value) {
        this.value = value;
        return this;
    }

    @Override
    public String toString() {
        return getValue().toString();
    }
}
