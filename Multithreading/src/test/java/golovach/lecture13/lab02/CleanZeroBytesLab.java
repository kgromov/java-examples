package golovach.lecture13.lab02;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by kgr on 8/4/2017.
 */
/*
 * 1) Save only byte!=0
 * 2) ByteArrayStream
 */
public class CleanZeroBytesLab {
    public static void copyCleanZerosByByte(InputStream in, OutputStream out) throws IOException {
        byte[] buff = new byte[64 * 1024];
        int k;
        while ((k = in.read(buff)) != -1) {
            out.write(k);
        }

        // catch
        // closeQuietly(in)
        // closeQuietlyAndFlush(out)
    }

    private static void closeQuietly(InputStream in) {
        try {
            in.close();
        } catch (IOException e) {

        }
    }
}
