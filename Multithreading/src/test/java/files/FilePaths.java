package files;

import com.google.common.collect.Sets;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by konstantin on 23.03.2019.
 */
public class FilePaths {
    private static final String METADATA_FOLDER_NAME = "metadata";
    private static final Set<String> DB_EXTENSIONS = Sets.newHashSet("NDS", "DB", "SQLITE");
    private static final FileFilter NDS_DB_FILE_FILTER = file -> DB_EXTENSIONS.contains(FilenameUtils.getExtension(file.getName().toUpperCase()));


   /* private String getMetadataPathInDepth()
    {
        File rootDb = new File(mapPath).getAbsoluteFile();
        File temp = rootDb.getParentFile();
        FileFilter metadataFilter = file -> file.isDirectory() &&
                (file.getName().equalsIgnoreCase(METADATA_FOLDER_NAME) || file.getName().equalsIgnoreCase("map"));
        while (temp != null)
        {
            File[] mapFolders = temp.listFiles(metadataFilter);
            if (mapFolders != null && mapFolders.length == 2)
            {
                return Arrays.stream(mapFolders)
                        .filter(file -> file.getName().equalsIgnoreCase(METADATA_FOLDER_NAME))
                        .map(File::getAbsolutePath)
                        .findFirst().get();
            }
            temp = temp.getParentFile();
        }
        return rootDb.getAbsolutePath();
    }*/

    private static String getMetadataPathInDepth(String mapPath) {
        File rootDb = new File(mapPath).getAbsoluteFile();
        File temp = rootDb.getParentFile();
        FileFilter metadataFilter = file -> file.isDirectory() &&
                (file.getName().equalsIgnoreCase(METADATA_FOLDER_NAME) || file.getName().equalsIgnoreCase("map"));
        while (temp != null) {
            File[] mapFolders = temp.listFiles(metadataFilter);
            if (mapFolders != null && mapFolders.length == 2) {
                return Arrays.stream(mapFolders)
                        .filter(file -> file.getName().equalsIgnoreCase(METADATA_FOLDER_NAME))
                        .map(File::getAbsolutePath)
                        .findFirst().get();
            }
            temp = temp.getParentFile();
        }
        return rootDb.getAbsolutePath();
    }

    private static Path getRootParentFolder(String mapPath) {
        Path rootDB = Paths.get(mapPath).toAbsolutePath();
        Path rootFolder = rootDB.getParent();

        Path temp = rootDB.getParent();
        while (temp != null) {
     /* try
      {*/
            if (Files.isDirectory(temp) && temp.toString().endsWith("map")) {
                System.out.println(temp.getParent().relativize(rootFolder));
                return temp.getParent().resolve(METADATA_FOLDER_NAME);
            }
            temp = temp.getParent();
     /* }
      catch (NullPointerException e)
      {
        int a =1 ;
        return rootDB;

      }*/
        }
        return rootDB;
    }

    /*private static void buildMetadata(String metadataFolderPath, int updateRegionId)
    {
        File metadataRootFolder = new File(metadataFolderPath);
        File metadataUrFolder = new File(metadataFolderPath + File.separator + "UR" + updateRegionId);
        try
        {
            Files.walk(Paths.get(metadataFolderPath))
                    .map(Path::toFile)
                    .filter(NDS_DB_FILE_FILTER::accept)
                    .filter(f -> f.getParentFile().getName().equals("UR" + updateRegionId))
                    .forEach(System.out::println);

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        if (metadataRootFolder.exists() && metadataUrFolder.exists())
        {
            File[] ndsDbFiles = metadataUrFolder.listFiles(NDS_DB_FILE_FILTER);
            if (ndsDbFiles != null)
            {
                for (File dbFile : ndsDbFiles)
                {
                    MetadataBlockName block = MetadataBlockName.getMetadataBlock(dbFile.getName());
//          metadataBlockMap.put(block, new MetadataBlock(block, updateRegionId, dbFile.getAbsolutePath()));
                }
            }
        }
    }*/

    // optional: Path rootFolderPath
    private static void collectDbFiles(Path metadataFolderPath, String product, int updateRegionId, List<String> dbFiles) {
        String urFolder = "UR" + updateRegionId;
        Path metadataCrossProductUr = Paths.get(metadataFolderPath.toString(), product, urFolder);
        Path metadataSingleProductUr = Paths.get(metadataFolderPath.toString(), urFolder);
        File[] ndsDbFiles = null;
        // cross product
        if (Files.exists(metadataCrossProductUr)) {
            ndsDbFiles = metadataCrossProductUr.toFile().listFiles(NDS_DB_FILE_FILTER);
            // 1 product case
        } else if (Files.exists(metadataSingleProductUr)) {
            ndsDbFiles = metadataSingleProductUr.toFile().listFiles(NDS_DB_FILE_FILTER);
        }
        if (ndsDbFiles != null) {
            for (File dbFile : ndsDbFiles) {
                dbFiles.add(dbFile.getAbsolutePath() + "\n");
//                    MetadataBlockName block = MetadataBlockName.getMetadataBlock(dbFile.getName());
//          metadataBlockMap.put(block, new MetadataBlock(block, updateRegionId, dbFile.getAbsolutePath()));
            }
        }
    }

    public static void main(String[] args) {
        String[] pathsToRootMap = {
                // delivery
                "D:\\workspace\\konstantin-examples\\Multithreading\\src\\main\\java\\full\\map\\root.nds",
                "D:\\workspace\\konstantin-examples\\Multithreading\\src\\main\\java\\full\\map\\LC\\root.nds",
                "D:\\workspace\\konstantin-examples\\Multithreading\\src\\main\\java\\full\\map\\LC\\UR10101\\root.nds",
                //  1 product
                "D:\\workspace\\konstantin-examples\\Multithreading\\src\\main\\java\\not_full\\map\\root.nds",
                "D:\\workspace\\konstantin-examples\\Multithreading\\src\\main\\java\\not_full\\map\\UR10101\\root.nds"
        };
        for (String path : pathsToRootMap) {
            System.out.println("***************************************");
            List<String> dbFiles = new ArrayList<>();
            Path metadataFolderPath = getRootParentFolder(path);
            String metadataFolder = getMetadataPathInDepth(path);
            System.out.println("map path = " + path);
            /*System.out.println("getRootParentFolder = " + metadataFolderPath);
            System.out.println("getMetadataPathInDepth = " + metadataFolder);*/
            collectDbFiles(metadataFolderPath, "LC", 10101, dbFiles);
            System.out.println(dbFiles);
            int a = 1;
        }
    }
}
