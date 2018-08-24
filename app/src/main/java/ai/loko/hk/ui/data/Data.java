package ai.loko.hk.ui.data;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Some data..
 */
public class Data {
    public static final String GOOGLE_URL = "https://www.google.com/search?q=";

    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64)";
    public static String delete = "the of a an ? & and , in -";
    private static final String[] remove = {"name",
            "does", "do", "is", "am", "are", "have", "has", "been", "did", "was", "not", "least", "never", "don't", "haven't", "didn't", "wasn't", "except", "wouldn't", "itsn't",
            "were", "had", "will", "would", "shall", "can", "should", "could", "may", "might", "need", "come", "comes", "means",
            "what", "who", "which", "whom", "why", "how", "when", "where",
            "of", "on", "these", "that", "this", "those", "there", "their", "at", "between", "from", "since", "for",
            "they", "and", "the", "a", "an", "with", "as", "by", "in", "to", "into", "also", "but",
            "i", "my", "me", "we", "our", "you", "your", "he", "his", "him", "himself", "them", "themselves", "it", "its", "myself", "she", "her", "yourselves",
            "&", ".", "?", ",","matched","paired","pair"
    };

    public static final ArrayList<String> removeWords = new ArrayList<>(Arrays.asList(remove));

    private static String[] negativeWords = {"not", "least", "never", "incorrect", "incorrectly", "none","cannot","can't","didn't"};
    public static final ArrayList<String> removeNegativeWords =new ArrayList<>(Arrays.asList(negativeWords));

}
