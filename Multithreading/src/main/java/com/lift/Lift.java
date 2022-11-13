package com.lift;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by konstantin on 13.11.2022.
 */
public class Lift {
    private int florsNumber;
    private AtomicInteger currentFlor = new AtomicInteger();
    private SortedSet<Integer> waitingFlors = new TreeSet<>();
    private SortedSet<Integer> movingToFlors = new TreeSet<>();

    public Lift(int florsNumber) {
        this.florsNumber = florsNumber;
    }
}
