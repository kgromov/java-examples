package golovach.lecture08.lab01;

/**
 * Created by konstantin on 18.06.2017.
 */
/*
 * 1) PrintC as standalone thread;
 * 2) Move logic from main to Coordinator.class
 * 3) Start a few Coordinator threads
 */
public class ThreadAccordionLab {
    public static void main(String[] args) throws InterruptedException {
        Coordinator coordinator = new Coordinator();
        Thread thread = new Thread(coordinator);
        thread.start();
        thread.join();
    }
}
