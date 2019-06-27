package aws;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Reader {

    public static final String UPDATE_REGION_COLUMN = "UpdateRegionName";
    public static final String OUTPUT_FILE_NAME = "ACCUMULATED_ERRORS_";

    public Reader() {
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        /*if (args.length == 0) {
            System.out.println("Arguments: absolute_path_to_folder_with_hd_log_zips [name_or_start_of_name_from_validCheckId_column] [additional_search_string_for_contains_in_search]");
            System.out.println("Example of cmd run: java -jar HDLogsReader01.jar \"C:\\\\DBs\\\\HDLogs\" \"LANE_GROUPS_VALIDITY_RANGE_VALIDATION\" \"contain Overlap\"");
        } else {
            logsFolder = args[0];
            if (args.length > 1) {
                errorToFind = args[1];
                if (args.length > 2) {
                    errorToFind2 = args[2];
                }
            }*/

        String logsFolder = "C:\\HERE-CARDS\\my_dev_presubmits\\14474\\iteration_1\\logs";
//        String errorToFind = "ERROR -> TEST_QUERY_RESULTS";
        String errorToFind = "ERROR ->";
        String errorToFind2 = "SHARED_DATA ";

        File[] files = (new File(logsFolder)).listFiles();
        File csvOutputFile = new File(logsFolder + "\\ ACCUMULATED_ERRORS_rap.gateway_presence.csv");
        long count = 0L;
        String header = "UpdateRegionName;";
        PrintWriter pw = new PrintWriter(csvOutputFile);
        File[] var7 = files;
        int var8 = files.length;

        label351:
        for (int var9 = 0; var9 < var8; ++var9) {
            File file = var7[var9];
            if (file.isFile() && file.getName().contains("console_log")) {
                System.out.println("Processing " + file.getName() + " file.");
                String uRName = file.getName();
                uRName = uRName.replaceAll("LC_", "");
                uRName = uRName.substring(0, uRName.toUpperCase().indexOf("_BUILD_NUMBER_"));
                FileInputStream fileInputStream = new FileInputStream(file);
                Throwable var13 = null;

                try {
                    ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
                    Throwable var15 = null;

                    try {
                        while (true) {
                            ZipEntry zipEntry;
                            do {
                                do {
                                    if ((zipEntry = zipInputStream.getNextEntry()) == null) {
                                        continue label351;
                                    }
                                } while (zipEntry.isDirectory());
                            } while (!zipEntry.getName().contains("error.log"));

                            Scanner in = new Scanner(zipInputStream);

                            while (in.hasNext()) {
                                String line = in.nextLine();
                                if (count == 0L) {
                                    header = header + line;
                                    pw.println(header);
                                }

                                if (line.contains(errorToFind) && line.contains(errorToFind2)) {
                                    pw.println(uRName + ";" + line);
                                }

                                ++count;
                                if (count % 10000L == 0L && count > 1L) {
                                    System.out.println("Processed " + count + " rows.");
                                }
                            }
                        }
                    } catch (Throwable var40) {
                        var15 = var40;
                        throw var40;
                    } finally {
                        if (zipInputStream != null) {
                            if (var15 != null) {
                                try {
                                    zipInputStream.close();
                                } catch (Throwable var39) {
                                    var15.addSuppressed(var39);
                                }
                            } else {
                                zipInputStream.close();
                            }
                        }

                    }
                } catch (Throwable var42) {
                    var13 = var42;
                    throw var42;
                } finally {
                    if (fileInputStream != null) {
                        if (var13 != null) {
                            try {
                                fileInputStream.close();
                            } catch (Throwable var38) {
                                var13.addSuppressed(var38);
                            }
                        } else {
                            fileInputStream.close();
                        }
                    }

                }
            }
        }

    }
//    }
}
