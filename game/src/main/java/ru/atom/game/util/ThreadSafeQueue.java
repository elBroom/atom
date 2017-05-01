package ru.atom.game.util;


import ru.atom.game.model.User;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by sergey on 3/14/17.
 */
public class ThreadSafeQueue {
    private static BlockingQueue<User> instance = new LinkedBlockingQueue<>();

    public static BlockingQueue<User> getInstance() {
        return instance;
    }
}
