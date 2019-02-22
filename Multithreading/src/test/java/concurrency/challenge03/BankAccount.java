package concurrency.challenge03;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {
    private double balance;
    private String accountNumber;
    private Lock lock = new ReentrantLock(false);

    public BankAccount(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }

    public void deposit(double amount) {
        boolean state = false;
        try {
            lock.tryLock(1, TimeUnit.MILLISECONDS);
            try {
                balance += amount;
                state = true;
            } finally {
                lock.unlock();
            }
        } catch (InterruptedException e) {

        }
        if (!state) {
            System.out.println("Unsuccessful attempt to lock");
        }
    }

    public void withdraw(double amount) {
        boolean state = false;
        try {
            lock.tryLock(1, TimeUnit.MILLISECONDS);
            try {
                balance -= amount;
                state = true;
            } finally {
                lock.unlock();
            }
        } catch (InterruptedException e) {

        }
        if (!state) {
            System.out.println("Unsuccessful attempt to lock");
        }
    }

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
