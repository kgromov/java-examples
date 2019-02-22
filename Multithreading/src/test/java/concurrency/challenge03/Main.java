package concurrency.challenge03;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        final BankAccount account = new BankAccount("12345-678", 1000.00);

        // Create and start the threads here...
        Thread trThread1 = new Thread(() -> {
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
        trThread2.join();

        System.out.println("After transactions: "+account);
    }
}
