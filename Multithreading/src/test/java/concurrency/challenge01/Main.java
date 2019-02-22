package concurrency.challenge01;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        final BankAccount account = new BankAccount("12345-678", 1000.00);

        // Create and start the threads here...
        /*Thread trThread1 = new Thread(() -> {
            account.deposit(300.00);
            account.withdraw(50.00);
        });

        Thread trThread2 = new Thread(() -> {
            account.deposit(203.75);
            account.withdraw(100.00);
        });
        trThread1.start();
        trThread2.start();

        trThread1.join();
        trThread2.join();*/

        long start = System.nanoTime();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i =0; i < 10; i++)
        {
//            executorService.submit(() -> account.deposit(100));
            executorService.submit(() ->
            {
                account.deposit(100);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();

        System.out.println(String.format("Time elapsed = %d ms", TimeUnit.MILLISECONDS.convert(System.nanoTime()- start, TimeUnit.NANOSECONDS)));
        System.out.println("After transactions: "+account);
    }
}
