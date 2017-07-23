package golovach.lecture14.lab03;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by konstantin on 23.07.2017.
 */
public class RecordStorage implements AutoCloseable {
    private static final int INT_SIZE_IN_BYTES = 4;
    private final RandomAccessFile file;

    public RecordStorage(String fileName) throws FileNotFoundException {
        this.file = new RandomAccessFile(fileName, "rw");
    }

    public void write(Record record, int index) throws IOException {
        // seek to record position in file
        file.seek(index * (INT_SIZE_IN_BYTES + Record.MAX_DATA_LENGTH));
        // write fields
        file.writeInt(record.getId());
        file.write(record.getData());
    }

    public Record read(int index) throws IOException {
        // seek to record position in file
        file.seek(index * (INT_SIZE_IN_BYTES + Record.MAX_DATA_LENGTH));
        // read fields
        int id = file.readInt();
        byte[] data = new byte[Record.MAX_DATA_LENGTH];
        file.readFully(data);
        // return
        return new Record(id, data);
    }

    public void flush() throws IOException {
        file.getChannel().force(true);
        file.getFD().sync();
    }

    @Override
    public void close() throws IOException {
        file.close();
    }
}
