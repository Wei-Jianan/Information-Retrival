import java.io.File;

public class Utils {
    private final static File TEMP_DIR = new File("temp");
    public final static File INDEX_DIR = new File(TEMP_DIR, "index");
    public final static File RAW_DOC_DIR = new File(TEMP_DIR, "raw");
    public final static String cranAllName = "cran.all.1400";
    public final static String cranQryName = "cran.qry";
    public final static String cranQrelName = "cranqrel";

    public static final File RAW_DOC = new File(RAW_DOC_DIR, cranAllName);
    public static final File RAW_QRY = new File(RAW_DOC_DIR, cranQryName);
    public static final File RAW_QREL = new File(RAW_DOC_DIR, cranQrelName);
}
