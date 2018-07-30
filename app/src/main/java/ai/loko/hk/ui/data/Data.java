package ai.loko.hk.ui.data;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Some data..
 */
public class Data {
    public static final String  GOOGLE_ASSITANCE_URL=it is not public for you;
    public static final String GOOGLE_URL = "https://www.google.com/search?q=";


    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64)";
    public static String delete = "delete word";
    private static final String[] remove = {"name",
            "does", "other list in full code"
    };

    public static final ArrayList<String> removeWords = new ArrayList<>(Arrays.asList(remove));

    private static String[] negativeWords = {"negative words "};
    public static final ArrayList<String> removeNegativeWords =new ArrayList<>(Arrays.asList(negativeWords));

}
