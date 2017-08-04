package golovach.lecture13.lab01;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by kgr on 8/4/2017.
 */
/*
 * 1) Save to 2 out steam from 1 input stream
 * 2) Decorate with BufferedInputStream (1 read, 2 writes)
 * 3) Write to 2 files
 * 4) Delete both files if any error occurs
 */
public class DownloadFromInternetLab {
    public static void main(String[] args) throws IOException {
        URL url = new URL("http://google.com");
        InputStream inputStream = url.openStream();
        int b;
        while ((b = inputStream.read()) != -1)
            System.out.print((char) b);
        inputStream.close();

    }
}
