package ru.atom.game.util;


import ru.atom.game.model.Connection;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by sergey on 3/14/17.
 */
public class ThreadSafeQueue {
    private static BlockingQueue<Connection> instance = new LinkedBlockingQueue<>();

    public static BlockingQueue<Connection> getInstance() {
        return instance;
    }
}
