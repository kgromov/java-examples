package golovach.lecture07;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by konstantin on 05.06.2017.
 */
public class TestTransfers {
    public static void main(String[] args) {
        for (int k = 0; k < 1000; k++) {
            System.out.println("\n" + k + " transfer attempt");
            final Random random = new Random();
            final AtomicBoolean isTransferFails = new AtomicBoolean(false);
            TransactionManager manager = new BankTransferManager();
            BankAccount[] accounts = {
                    new BankAccount(random, isTransferFails.get()),
                    new BankAccount(random, isTransferFails.get()),
                    new BankAccount(random, isTransferFails.get())
            };
            int[] moneys = {-1, -1, 2};
            boolean ok = manager.transfer(accounts, moneys);
            BankTransferManager.isTransferFailed = false;
            System.out.println("Transfer result is " + (ok ? "SUCCESSFUL" : "FAILED"));
            for (int i = 0; i < accounts.length; i++) {
                if (ok & accounts[i].getHistory() != moneys[i])
                    throw new AssertionError("Invalid transfer\t" + Arrays.toString(accounts));
                else if (!ok & accounts[i].getHistory() != 0)
                    throw new AssertionError("Invalid transfer\t" + Arrays.toString(accounts));

            }
            Arrays.stream(accounts).forEach(account -> System.out.println(account.toString()));
            System.out.println();
        }

    }
}
