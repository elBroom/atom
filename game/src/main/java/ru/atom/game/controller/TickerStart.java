package ru.atom.game.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.atom.game.util.ThreadSafeQueueTicker;

import java.util.concurrent.TimeUnit;

/**
 * Created by zarina on 13.05.17.
 */
public class TickerStart implements Runnable {
    private static final Logger log = LogManager.getLogger(TickerStart.class);

    @Override
    public void run() {
        Ticker ticker = null;
        log.info("Start");
        while (!Thread.currentThread().isInterrupted()) {
            try {
                log.info("Ticker get");
                ticker = ThreadSafeQueueTicker.getInstance().poll(10_000, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                log.warn("Timeout reached");
            }
            if (ticker != null) {
                Thread threadTicker = new Thread(ticker);
                threadTicker.start();
            }
        }
    }
}
