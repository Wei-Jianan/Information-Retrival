package cn.demo;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.temporal.Temporal;
import java.util.Comparator;

public class Utils {
    private final static File TEMP_DIR = new File("temp");
    public final static File INDEX_DIR = new File(TEMP_DIR, "index");
    public static File JSONS_DIR = null;
    public static File QUERIES_FIR = null;

    private static void clearTempDir(boolean ifClearIndex) {
        if (ifClearIndex) {
            try {
                clear(TEMP_DIR);
            } catch (IOException e) {
                e.printStackTrace();
            }
            TEMP_DIR.mkdir();
            INDEX_DIR.mkdirs();
        }
        System.out.println("the director " + INDEX_DIR.toString() + " have been made.");
    }
    public static void initialize(boolean ifclearIndex, String jsonsDirPath, String queriesDirPath) {
        clearTempDir(ifclearIndex);
        JSONS_DIR = new File(jsonsDirPath);
        QUERIES_FIR = new File(queriesDirPath);
        if (JSONS_DIR.isDirectory()  && QUERIES_FIR.isFile()) {
            System.out.println("jsons directory is :" + JSONS_DIR.getAbsolutePath());
            System.out.println("queries file is:" + QUERIES_FIR.getAbsolutePath());
        } else {
            System.out.println("-j should follow a path to directory\n -j should follow a path to a file\nThe path should be absolute path");
        }
    }
    private static void clear(File toBeDeleted) throws IOException {
        Path pathToBeDEleted = Paths.get(toBeDeleted.getAbsolutePath());

        Files.walk(pathToBeDEleted)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        System.out.println("the director " + TEMP_DIR.toString() + " have been deleted.");
        // common-io to delete a directory
//        FileUtils.forceDelete(new File(destination));
    }

}
