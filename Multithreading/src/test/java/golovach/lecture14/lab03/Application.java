package golovach.lecture14.lab03;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by konstantin on 23.07.2017.
 */
public class Application {
    private static final int COUNT = 1000;

    private static void initStorageData(String fileName) throws IOException {
        try (RecordStorage storage = new RecordStorage(fileName)) {
            for (int k = 0; k < COUNT; k++) {
                Record newRecord = generateRecord(k);
                storage.write(newRecord, k);
            }
            storage.flush();
        }
    }

    private static void testStorageData(String fileName) throws IOException {
        try (RecordStorage storage = new RecordStorage(fileName)) {
            for (int k = COUNT - 1; k >= 0; k--) {
                Record expected = generateRecord(k);
                Record actual = storage.read(k);
                if (actual.getId() != expected.getId()) {
                    throw new AssertionError("k = " + k + ", actual.getId()  = " + actual.getId() + ", expected.getId() = " + expected.getId());
                }
                if (!Arrays.equals(actual.getData(), expected.getData())) {
                    throw new AssertionError("k = " + k + ", \n  actual.getData() = " + Arrays.toString(actual.getData()) + ", \nexpected.getData() = " + Arrays.toString(expected.getData()));
                }
            }
        }
    }

    private static Record generateRecord(int k) {
        Random rnd = new Random(k);
        // random int
        int id = rnd.nextInt();
        // create array of random length
        byte[] data = new byte[rnd.nextInt(Record.MAX_DATA_LENGTH)];
//        byte[] data = new byte[Record.MAX_DATA_LENGTH];
        // fill array by random bytes
        rnd.nextBytes(data);
        return new Record(id, data);
    }

    public static void main(String[] args) throws IOException {
        String fileName = "c:/tmp/data.bin";
        initStorageData(fileName);
        testStorageData(fileName);
    }
}
