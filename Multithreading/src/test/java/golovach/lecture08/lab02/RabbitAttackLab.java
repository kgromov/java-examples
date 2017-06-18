package golovach.lecture08.lab02;

import golovach.lecture08.PrintRunnable;

/**
 * Created by konstantin on 18.06.2017.
 */
public class RabbitAttackLab {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 1; i < 100000; i++) {
            String spaces = spaces(i);
            Runnable printer = new PrintRunnable(spaces + i, 100);
            Thread thread = new Thread(printer);
            thread.start();
            Thread.sleep(100);
        }
    }

    private static String spaces(int count) {
        String result = "";
        for (int i = 0; i < count; i++)
            result += " ";
        return result;
    }
}
