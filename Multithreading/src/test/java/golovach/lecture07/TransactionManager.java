package golovach.lecture07;

/**
 * Created by konstantin on 05.06.2017.
 */
public interface TransactionManager {
    boolean transfer(Account[] accounts, int[] money);
}
