package com.ewolff.demo;

import java.time.Instant;
import java.util.Random;

/**
 * Created by konstantin on 25.11.2018.
 */
public class LongRunner implements Runnable {

    private final long timeout;

    public LongRunner() {
        this.timeout = 2000L * new Random().nextInt(101) / 100;
        System.out.println("Init long runner with timeout = " + timeout);
    }

    @Override
    public void run() {
        Instant instant = Instant.now();
        System.out.println("Start" + instant + " with wait time = " + timeout);
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Finish in :" + Instant.now());
    }
}
