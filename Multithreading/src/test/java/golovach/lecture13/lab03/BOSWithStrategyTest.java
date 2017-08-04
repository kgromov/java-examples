package golovach.lecture13.lab03;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Created by kgr on 8/4/2017.
 */
/*
 * 1) fillByByte -> toByteArray;
 * 2) fillByByte -> writeTo;
 * 3) fillByArray -> toByteArray;
 * 4) fillByArray -> writeTo
 */
public class BOSWithStrategyTest {
    private final static int COUNT = 1_000_000;
    private final static AllocateStrategy ALLOCATE_STRATEGY = new AllocateStrategy() {
        @Override
        public int nextAfter(int now) {
            return now + (now >> 1);
        }
    };

    public static void main(String[] args) throws IOException {
        test_fillByByte_WriteTo();

        test_fillByArray_WriteTo();

        test_fillByByte_toByteArray();

        test_fillByArray_toByteArray();
    }

    private static void test_fillByByte_WriteTo() throws IOException {
        BOSWithStrategy buff = fillByByte();
        ByteArrayOutputStream tmpBuff = new ByteArrayOutputStream();
        buff.writeTo(tmpBuff);
        checkCorrectData(tmpBuff.toByteArray());
    }

    private static void test_fillByArray_WriteTo() throws IOException {
        BOSWithStrategy buff = fillByArray();
        ByteArrayOutputStream tmpBuff = new ByteArrayOutputStream();
        buff.writeTo(tmpBuff);
        checkCorrectData(tmpBuff.toByteArray());
    }

    private static void test_fillByByte_toByteArray() throws IOException {
        BOSWithStrategy buff = fillByByte();
        checkCorrectData(buff.toByteArray());
    }

    private static void test_fillByArray_toByteArray() throws IOException {
        BOSWithStrategy buff = fillByArray();
        checkCorrectData(buff.toByteArray());
    }

    private static void checkCorrectData(byte[] buff) {
        Random random = new Random(0);
        for (int i = 0; i < COUNT; i++) {
            byte expected = (byte) random.nextInt();
            byte actual = buff[i];
            if (actual != expected)
                throw new AssertionError(String.format("expected %d != actual %d", expected, actual));
        }
    }

    private static BOSWithStrategy fillByByte() throws IOException {
        BOSWithStrategy result = new BOSWithStrategy(ALLOCATE_STRATEGY);
        Random random = new Random(0);
        for (int i = 0; i < COUNT; i++) {
            result.write((byte) random.nextInt());
        }
        return result;
    }

    private static BOSWithStrategy fillByArray() throws IOException {
        BOSWithStrategy result = new BOSWithStrategy(ALLOCATE_STRATEGY);
        Random dataRandom = new Random(0);
        Random arraySizeRnd = new Random(1);
        int wroteSize = 0;
        while (wroteSize < COUNT) {
            int newBuffSize = arraySizeRnd.nextInt(10);
            if (newBuffSize == 1) {
                result.write((byte) dataRandom.nextInt());
                wroteSize++;
            } else {
                byte[] buff = new byte[newBuffSize];
                for (int i = 0; i < COUNT; i++) {
                    buff[i] = ((byte) dataRandom.nextInt());
                }
                result.write(buff);
            }
        }
        return result;
    }
}
