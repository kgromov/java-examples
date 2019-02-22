package concurrency.challenge02;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
    final BankAccount account = new BankAccount("12345-678", 1000.00);

        // Create and start the threads here...
//        Thread trThread1 = new Thread() {
//            public void run() {
//                account.deposit(300.00);
//                account.withdraw(50.00);
//            }
//        };
//
//        Thread trThread2 = new Thread() {
//            public void run() {
//                account.deposit(203.75);
//                account.withdraw(100.00);
//            }
//        };
 */
public class BankAccount {
    private double balance;
    private String accountNumber;
    private Lock lock = new ReentrantLock(false);

    public BankAccount(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }

    public void deposit(double amount) {
        lock.lock();
        try
        {
            balance += amount;
        }
        finally {
            lock.unlock();
        }
    }

    public void withdraw(double amount) {
        lock.lock();
        try
        {
            balance -= amount;
        }
        finally {
            lock.unlock();
        }
    }

   /* public synchronized void deposit(double amount) {
        balance += amount;
    }

    public synchronized void withdraw(double amount) {
        balance -= amount;
    }*/

    public double getBalance() {
        return balance;
    }

    public void printAccountNumber() {
        System.out.println("Account number = " + accountNumber);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BankAccount{");
        sb.append("balance=").append(balance);
        sb.append(", accountNumber='").append(accountNumber).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
