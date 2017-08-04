package golovach.lecture13.lab03;

/**
 * Created by kgr on 8/4/2017.
 */
public class DoubleAllocateStrategy implements AllocateStrategy {
    @Override
    public int nextAfter(int now) {
        return now * 2;
    }
}
