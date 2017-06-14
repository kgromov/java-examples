package golovach.lecture07;

import java.util.Arrays;

/**
 * Created by konstantin on 05.06.2017.
 */
public class BankTransferManager implements TransactionManager {
    static boolean isTransferFailed;

    @Override
    public boolean transfer(Account[] accounts, int[] money) {
        if (accounts.length != money.length)
            throw new IllegalArgumentException(String.format("No accordance between accounts and money\t" +
                    "Accounts='%s'\tMoney='%s'", Arrays.toString(accounts), Arrays.toString(money)));
        for (int i = 0; i < accounts.length; i++) {
            try {
                performTransaction(accounts[i], money[i]);
            } catch (TransferException e2) {
                System.out.println(String.format("Transfer exception happens on %d account", i));
                isTransferFailed = true;
                for (int j = i - 1; j >= 0; j--) {
                    try {
                        System.out.println(String.format("Try to make return for %d account", j));
                        performTransaction(accounts[j], -money[j]);
                        System.out.println(String.format("Successfully perform return for %d account", j));
                    } catch (TransferException ignored) {
                    }
                }
                return false;
            }
        }
        return true;
    }

    public void performTransaction(Account account, int amount) throws TransferException {
        while (true) {
            try {
                account.change(amount);
                break;
            } catch (TransactionException ignored) {
            }
        }
    }
}