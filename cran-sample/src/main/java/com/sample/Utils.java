package com.sample;

import org.apache.commons.io.FileUtils;
import org.codehaus.plexus.archiver.tar.TarGZipUnArchiver;
import org.codehaus.plexus.logging.console.ConsoleLoggerManager;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
//import org.apache.commons.io.FileUtils;


public class Utils {
    private final static File TEMP_DIR = new File("src/temp");
    public final static File INDEX_DIR = TEMP_DIR.toPath().resolve("index").toFile();
    public final static File RAW_DOC_DIR = TEMP_DIR.toPath().resolve("raw").toFile();
    public final static String cranAllName = "cran.all.1400";
    public final static String cranQryName = "cran.qry";
    public final static String cranQrelName = "cranqrel";

    public static final File RAW_DOC = new File(RAW_DOC_DIR, cranAllName);
    public static final File RAW_QRY = new File(RAW_DOC_DIR, cranQryName);
    public static final File RAW_QREL = new File(RAW_DOC_DIR, cranQrelName);
    private final static URL url;

    static {
        try {
            url = new URL("http://ir.dcs.gla.ac.uk/resources/test_collections/cran/cran.tar.gz");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new Error(e);
        }

    }

    //    private final static Path RAW_DOC_DIR = TEMP_DIR.resolve("raw_doc");
    public static void initialize() throws IOException {
//        System.out.println( RAW_DOC_DIR);
//        System.out.println( DOCS_DIR);
//        File file = new File(classLoader.getResource("somefile").getFile());
        if (TEMP_DIR.isDirectory()) {
            clear(TEMP_DIR);
        }
        TEMP_DIR.mkdir();
        INDEX_DIR.mkdirs();
        RAW_DOC_DIR.mkdirs();

        File rawDocFile = downloadFromUrltoDir(url, RAW_DOC_DIR);
        decompress(rawDocFile);
        delate(rawDocFile);

        System.out.println("\n \nthe director " +RAW_DOC_DIR.toString() + "contains:\n" );
        Arrays.stream(RAW_DOC_DIR.list())
                .sorted()
                .forEach(System.out::println);
        System.out.println("\n\n");
    }

    public static void delate(File toDeleteFile) {
        toDeleteFile.delete();

        System.out.println(toDeleteFile.toString() + " are deleted.");
    }

    public static void decompress(File toDecompressFile) {
        TarGZipUnArchiver ua = new TarGZipUnArchiver();
        ConsoleLoggerManager manager = new ConsoleLoggerManager();
        manager.initialize();
        ua.enableLogging(manager.getLoggerForComponent("bla"));
        ua.setSourceFile(toDecompressFile);
        ua.setDestDirectory(toDecompressFile.getParentFile());
        ua.extract();
        System.out.println(toDecompressFile.toString() + " are decompressed.");
    }


    public static boolean clear(File toBeDeleted) {
        File[] contentsInDir = toBeDeleted.listFiles();
        if (contentsInDir != null) {
            for (File item : contentsInDir) {
                clear(item);
            }
        }
        System.out.println(toBeDeleted.toString() + " are cleared.");
        return toBeDeleted.delete();
    }

    public static boolean clear(Path toBeDeleted) {
        File f = new File(toBeDeleted.toString());
        return clear(f);
    }
//
    public static void clearFP(File toBeDeleted) throws IOException {
        Path pathToBeDEleted = Paths.get(toBeDeleted.getAbsolutePath());
        Files
                .walk(pathToBeDEleted)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        // common-io to delete a directory
//        FileUtils.forceDelete(new File(destination));
    }

    public static File downloadFromUrltoDir(URL fromUrl, File toDir) {
        String rawDocName = fromUrl.toString().substring(fromUrl.toString().lastIndexOf("/") + 1);
        File toFile = new File(toDir, rawDocName);
        try {

            FileUtils.copyURLToFile(fromUrl, toFile);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        //another way to download
        System.out.println(toFile.getAbsoluteFile().toString() + " are downloaded to " + toDir.toString());
        return toFile;
    }

    public static void main(String[] args) {
        try {
            initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

