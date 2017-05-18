package ru.atom.game.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zarina on 01.05.17.
 */
@Entity
@Table(name = "game", schema = "game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @JoinColumn(name = "user_id", nullable = false)
    @OneToOne(cascade = CascadeType.PERSIST)
    private User user;

    @Column(name = "sublink", nullable = false)
    private String sublink;

    @Column(name = "score")
    private Integer score;

    @Column(name = "create_at")
    @CreationTimestamp
    private Date createAt;

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Game setUser(User user) {
        this.user = user;
        return this;
    }

    public Game setSublink(String sublink) {
        this.sublink = sublink;
        return this;
    }

    public Game setScore(Integer score) {
        this.score = score;
        return this;
    }

    @Override
    public String toString() {
        return getId().toString();
    }
}

