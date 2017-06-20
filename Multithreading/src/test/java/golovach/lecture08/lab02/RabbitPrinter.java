package golovach.lecture08.lab02;

/**
 * Created by kgr on 6/20/2017.
 */
public class RabbitPrinter implements Runnable {
    @Override
    public void run() {
        System.out.println("New Rabbit was born" + Thread.currentThread().getName());
        try {
            Thread.sleep(500);
            Thread daughter = new Thread(new RabbitPrinter());
            Thread son = new Thread(new RabbitPrinter());
            System.out.println("Rabbit produce 2 children");
            daughter.start();
            son.start();
            daughter.join();
            son.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Rabbit died" + Thread.currentThread().getName());
    }

    private static String spaces(int count) {
        String result = "";
        for (int i = 0; i < count; i++)
            result += " ";
        return result;
    }
}
