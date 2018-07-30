package ai.loko.hk.ui.answers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URLEncoder;
import java.util.ArrayList;

import ai.loko.hk.ui.data.Data;

import static ai.loko.hk.ui.utils.Utils.count;
import static ai.loko.hk.ui.utils.Utils.getSimplifiedString;

class WikiSearch extends Thread{
    //String TAG="wiki";
    int recurrence;
    private String option;
    private ArrayList<String> simplifiedQuestionList;
    private String simplifiedQuestion;

    WikiSearch(String option, ArrayList<String> simplifiedQuestionList, String simplifiedQuestion) {
        this.option = option;
        this.simplifiedQuestion = simplifiedQuestion;
        this.simplifiedQuestionList = simplifiedQuestionList;
        recurrence = 0;
    }

    @Override
    public void run() {
        try {
           //DO WIKI SEARCH
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}