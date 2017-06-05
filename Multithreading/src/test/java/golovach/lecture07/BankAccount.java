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
//        if (random.nextInt(10) == 9) throw new TransferException("");
        if (random.nextBoolean()) throw new TransferException("");
        this.history = delta;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "history=" + history +
                '}';
    }
}
