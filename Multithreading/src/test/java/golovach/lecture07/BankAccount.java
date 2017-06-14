package golovach.lecture07;

import java.util.Random;

/**
 * Created by konstantin on 05.06.2017.
 */
public class BankAccount implements Account {
    private int history;
    private Random random;
    private boolean isTransferFailed;

    public BankAccount(Random random, boolean isTransferFailed) {
        this.random = random;
        this.isTransferFailed = isTransferFailed;
    }

    @Override
    public void change(int delta) throws TransactionException, TransferException {
        if (random.nextBoolean()) throw new TransactionException();
        if (!BankTransferManager.isTransferFailed && random.nextBoolean()) {
            this.isTransferFailed = true;
            throw new TransferException("Transfer crash");
        }
        this.history += delta;
    }

    public boolean isTransferFailed() {
        return isTransferFailed;
    }

    public int getHistory() {
        return history;
    }

    public void setTransferFailed(boolean transferFailed) {
        isTransferFailed = transferFailed;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "history=" + history +
                '}';
    }
}
