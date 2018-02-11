package com.sample;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;



public class Utils {
    private final static Path TEMP_DIR = Paths.get(new File("src/temp").getAbsolutePath());
    private final static Path INDEX_DIR = Paths.get(TEMP_DIR.toString(), "index");
    private final static Path RAW_DOC_DIR = Paths.get(TEMP_DIR.toString(), "raw_doc");

    public static void initialize(){
//        System.out.println( RAW_DOC_DIR);
////        System.out.println( DOCS_DIR);
//        ClassLoader classLoader = new Object.getClass().getClassLoader();
//        File file = new File(classLoader.getResource("somefile").getFile());
        System.out.println(resourcesDirectory.getAbsolutePath());
    }
    public static void clear(Path toBeDeleted) {
        try {

        }
    }

    boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }
}
