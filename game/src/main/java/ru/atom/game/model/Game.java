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

    @JoinColumn(name = "player1", nullable = false)
    @OneToOne(cascade = CascadeType.PERSIST)
    private User player1;

    @JoinColumn(name = "player2", nullable = false)
    @OneToOne(cascade = CascadeType.PERSIST)
    private User player2;

    @JoinColumn(name = "player3", nullable = false)
    @OneToOne(cascade = CascadeType.PERSIST)
    private User player3;

    @JoinColumn(name = "player4", nullable = false)
    @OneToOne(cascade = CascadeType.PERSIST)
    private User player4;

    @Column(name = "create_at")
    @CreationTimestamp
    private Date createAt;

    @Column(name = "sublink", nullable = false)
    private String sublink;

    public Integer getId() {
        return id;
    }

    public User getPlayer1() {
        return player1;
    }

    public User getPlayer2() {
        return player2;
    }

    public User getPlayer3() {
        return player3;
    }

    public User getPlayer4() {
        return player4;
    }

    public List<User> allUsers() {
        List<User> lst = new ArrayList<>();
        lst.add(player1);
        lst.add(player2);
        lst.add(player3);
        lst.add(player4);
        return lst;
    }

    public Game setAllUsers(List<User> users) {
        player1 = users.get(0);
        player2 = users.get(1);
        player3 = users.get(2);
        player4 = users.get(3);
        return this;
    }

    public Game setSublink(String sublink) {
        this.sublink = sublink;
        return this;
    }

    @Override
    public String toString() {
        return getId().toString();
    }
}

