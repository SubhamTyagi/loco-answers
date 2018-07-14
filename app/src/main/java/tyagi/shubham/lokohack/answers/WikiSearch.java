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
            Document option1wiki = Jsoup.connect(Jsoup.connect(Data.GOOGLE_URL + URLEncoder.encode(simplifiedQuestion + " " + option + " wiki", "UTF-8") + "&num=2").userAgent(Data.USER_AGENT).get().select(".g>.r>a").get(0).absUrl("href")).userAgent(Data.USER_AGENT).get();
            String t1 = option1wiki.body().text().toLowerCase();
            String text11 = getSimplifiedString(t1, null);
            for (String word : simplifiedQuestionList) {
                int p = count(word, text11);
                if (p != 0) {
                    text11.replaceAll(word, "");
                    recurrence += p;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}