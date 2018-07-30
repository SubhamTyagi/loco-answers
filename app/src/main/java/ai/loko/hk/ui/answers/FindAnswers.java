package ai.loko.hk.ui.answers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URLEncoder;
import java.util.ArrayList;

import ai.loko.hk.ui.data.Data;
import ai.loko.hk.ui.data.Which;

import static ai.loko.hk.ui.data.Data.delete;
import static ai.loko.hk.ui.utils.Utils.count;
import static ai.loko.hk.ui.utils.Utils.getSimplifiedQuestion;
import static ai.loko.hk.ui.utils.Utils.getSimplifiedString;
import static ai.loko.hk.ui.utils.Utils.stringToArrayList;


public class FindAnswers extends Which {

    // private final Context context;
    private final String A;
    final private String B;
    final private String C;
    // private static String TAG = "FindAnswers";
    //Question current;
    //private boolean checkForNegative;
    //private boolean wikiDone;
    //static boolean isOcr;
    private boolean error;
    private StringBuilder A1, B2, C3;
    private String question;
    private int a = 0, b = 0, c = 0;
    private String optionRed;

    private int aSize, bSize, cSize;


    public FindAnswers(String question, String optionA, String optionB, String optionC) {
        A = optionA.toLowerCase();
        B = optionB.toLowerCase();
        C = optionC.toLowerCase();

        //SOME CODE GOES HERE
    }

    private void init() {
        A1 = new StringBuilder();
        B2 = new StringBuilder();
        C3 = new StringBuilder();
    }

    public String getOptionRed() {
        return optionRed;
    }

    public boolean isError() {
        return error;
    }

    public String getAcount() {
        return A1.toString();

    }

    public String getBcount() {
        return B2.toString();
    }

    public String getCcount() {
        return C3.toString();

    }

    public String search() {
        if (A.contains("-")) {
            if (B.contains("-") && C.contains("-"))
                return pairGoogleSearch();
            else return googleSearch();
        }
        return googleSearch();
    }

    private String pairGoogleSearch() {
        
        return optionRed;

    }

    private String googleSearch() {
        //int max = 0;

        return optionRed;

    }

    private String wikiBot(boolean isNeg) {
}

