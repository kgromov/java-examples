package golovach.lecture13.lab03;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kgr on 8/4/2017.
 */
public class BOSWithStrategy extends OutputStream {
    private final static int DEFAULT_START_SIZE = 16;
    private final static AllocateStrategy DOUBLE_ALLOCATE_STRATEGY = new DoubleAllocateStrategy();
    private final AllocateStrategy strategy;
    private List<byte[]> bufferList = new ArrayList<>();
    private int count = 0;

    public BOSWithStrategy() {
        this(DEFAULT_START_SIZE, DOUBLE_ALLOCATE_STRATEGY);
    }

    public BOSWithStrategy(int startSize) {
        this(startSize, DOUBLE_ALLOCATE_STRATEGY);
    }

    public BOSWithStrategy(AllocateStrategy strategy) {
        this(DEFAULT_START_SIZE, strategy);
    }

    public BOSWithStrategy(int startSize, AllocateStrategy strategy) {
        this.strategy = strategy;
        this.bufferList.add(new byte[startSize]);
    }

    @Override
    public void write(int b) throws IOException {
        byte[] lastBuff = bufferList.get(bufferList.size() - 1);
        if (count == lastBuff.length) {
            int newSize = strategy.nextAfter(count);
            byte[] newLastBuff = new byte[newSize];
            bufferList.add(newLastBuff);
            count = 0;
        }
        lastBuff[count++] = (byte) b;
    }
}
