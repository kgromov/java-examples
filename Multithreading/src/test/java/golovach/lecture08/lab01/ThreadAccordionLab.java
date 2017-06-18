package golovach.lecture08.lab01;

import golovach.lecture08.PrintRunnable;

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
        for (int i = 0; i < 10; i++) {
            // A, B
            Runnable printA = new PrintRunnable("A    .", 100);
            Thread threadA = new Thread(printA);
            threadA.start();
            Runnable printB = new PrintRunnable(".    B", 99);
            Thread threadB = new Thread(printB);
            threadB.start();
            threadA.join();
            threadB.join();
            // C
            System.out.println("-----");
            Runnable printC = new PrintRunnable("  C  ", 100);
            printC.run();
            System.out.println("-----");
        }
    }
}
