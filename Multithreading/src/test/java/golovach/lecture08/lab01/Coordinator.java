package golovach.lecture08.lab01;

import golovach.lecture08.PrintRunnable;

/**
 * Created by konstantin on 18.06.2017.
 */
public class Coordinator implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            // A, B
            Runnable printA = new PrintRunnable("A    .", 100);
            Thread threadA = new Thread(printA);
            threadA.start();
            Runnable printB = new PrintRunnable(".    B", 99);
            Thread threadB = new Thread(printB);
            threadB.start();
            Runnable printC =  new PrintRunnable("  C  ", 100);
            Thread threadC = new Thread(printC);
            try {
                threadA.join();
                threadB.join();
                // C
                System.out.println("-----");
                threadC.start();
                threadC.join();
                System.out.println("-----");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
