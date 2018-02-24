package dao;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by konstantin on 23.12.2017.
 */
public class CsvDataProvider {

    public static List<List<String>> getData(String csvFile, char delimiter) {
        try {
            Reader reader = new FileReader(new File(csvFile));
            BufferedReader buffer = new BufferedReader(reader);
            List<List<String>> rows = new ArrayList<>();
            String line;
            while ((line = buffer.readLine()) != null) {
                rows.add(Arrays.asList(line.split(String.valueOf(delimiter))));
            }
            return rows;
        } catch (IOException e) {
            throw new IllegalArgumentException("No file found by path or file can not be parsed" + csvFile);
        }
    }
}
