package ru.atom.game.util;

import ru.atom.game.controller.Ticker;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by zarina on 13.05.17.
 */
public class ThreadSafeQueueTicker {
    private static BlockingQueue<Ticker> instance = new LinkedBlockingQueue<>();

    public static BlockingQueue<Ticker> getInstance() {
        return instance;
    }
}
