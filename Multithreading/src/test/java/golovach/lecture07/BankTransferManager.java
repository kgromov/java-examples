package golovach.lecture07;

import java.util.Arrays;

/**
 * Created by konstantin on 05.06.2017.
 */
public class BankTransferManager implements TransactionManager {

    @Override
    public boolean transfer(Account[] accounts, int[] money) {
        if (accounts.length != money.length)
            throw new IllegalArgumentException(String.format("No accordance between accounts and money\t" +
                    "Accounts='%s'\tMoney='%s'", Arrays.toString(accounts), Arrays.toString(money)));
        for (int i = 0; i < accounts.length; i++) {
            try {
                performTransaction(accounts[i], money[i]);
            } catch (TransferException e2) {
                for (int j = i; i >= 0; i--) {
                    try {
                        performTransaction(accounts[j], -money[j]);
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