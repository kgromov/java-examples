package concurrency.challenge04;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Even with try-finally unlock could be collision when after successful withdraw
 * destination thread is sleeping and locking lock
 * So deposit will be unsuccessful (lock.tryLock() of dest BankAccount)
 * And potentially LiveLock could not be resolved
 *
 * TODO: try with Atomic variable class (es) - aka non-blocking algorithm
 */
class BankAccount {
    private double balance;
    private String accountNumber;
    private Lock lock = new ReentrantLock();

    BankAccount(String accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public boolean withdraw(double amount) {

        if (lock.tryLock()) {
            try {
                try {
                    // Simulate database access
                    Thread.sleep(100);
                    balance -= amount;
                    System.out.printf("%s: Withdrew %f\n", Thread.currentThread().getName(), amount);
                    return true;
                } catch (InterruptedException e) {
                }
            } finally {
                lock.unlock();
            }
        }
        return false;
    }

    public boolean deposit(double amount) {
        if (lock.tryLock()) {
            try {
                try {
                    // Simulate database access
                    Thread.sleep(100);
                    balance += amount;
                    System.out.printf("%s: Deposited %f\n", Thread.currentThread().getName(), amount);
                    return true;
                } catch (InterruptedException e) {
                }
            } finally {
                lock.unlock();
            }
        }
        return false;
    }

    public boolean transfer(BankAccount destinationAccount, double amount) {
        if (withdraw(amount)) {
            if (destinationAccount.deposit(amount)) {
                return true;
            } else {
                // The deposit failed. Refund the money back into the account.
                System.out.printf("%s: Destination account busy. Refunding money\n",
                        Thread.currentThread().getName());
                deposit(amount);
            }
        }

        return false;
    }

    public void printAccountNumber() {
        System.out.println(String.format("Account number = %s, balance = %s", accountNumber, balance));
    }
}
