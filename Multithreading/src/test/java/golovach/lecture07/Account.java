package golovach.lecture07;

/**
 * Created by konstantin on 05.06.2017.
 */
public interface Account {
    void change(int delta) throws TransactionException, TransferException;
}
