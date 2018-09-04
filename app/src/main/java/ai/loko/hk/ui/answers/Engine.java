package ai.loko.hk.ui.answers;

import android.support.annotation.NonNull;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import ai.loko.hk.ui.data.Data;
import ai.loko.hk.ui.model.Question;

//import static ai.loko.hk.ui.data.Data.USER_AGENT;
import static ai.loko.hk.ui.data.Data.delete;
import static ai.loko.hk.ui.data.Data.getRandomUserAgent;
import static ai.loko.hk.ui.utils.Utils.count;
import static ai.loko.hk.ui.utils.Utils.getSimplifiedQuestion;
import static ai.loko.hk.ui.utils.Utils.getSimplifiedString;
import static ai.loko.hk.ui.utils.Utils.stringToArrayList;


public class Engine extends Base {
    private final String TAG = "Engine";

    public Engine(Question questionObj) {
        super(questionObj);
    }

    public String search() {
        if (optionA.contains("-") && optionB.contains("-") && optionC.contains("-")) {
            return googlePairSearch();
        }
        return googleSearch();
    }

    private String googlePairSearch() {
        boolean isNeg;
        a = b = c = 0;
        int p, q, r;
        reset();
        try {
            isNeg = stringToArrayList(question).removeAll(Data.removeNegativeWords);
            if (isNeg) {
                ArrayList<String> simplifiedQuestion = getSimplifiedQuestion(question, 1);//1 means negative words remove
                StringBuilder stringBuilder = new StringBuilder();
                for (String s : simplifiedQuestion) {
                    stringBuilder.append(s).append(" ");
                }
                this.question = stringBuilder.toString();
            }

            String simplifiedQuestion = getSimplifiedString(question, null);

            String sub1 = optionA.substring(0, optionA.indexOf("-") - 1);
            String sub2 = optionB.substring(0, optionB.indexOf("-") - 1);
            String sub3 = optionC.substring(0, optionC.indexOf("-") - 1);


            String text1 = getResponseFromGoogle(simplifiedQuestion, sub1);

            A1.append(sub1).append("-");
            B2.append(sub2).append("-");
            C3.append(sub3).append("-");

            sub1 = optionA.substring(optionA.indexOf("-") + 1);
            String optionA[] = sub1.split(" ");
            aSize = optionA.length;

            for (String words : optionA) {
                if (!delete.contains(words)) {
                    p = count(words, text1);
                    a += p;
                    A1.append(words).append("(").append(p).append(") ");
                } else {
                    A1.append(words).append(" ");
                }
            }


            String text2 = getResponseFromGoogle(simplifiedQuestion, sub2);

            sub2 = optionB.substring(optionB.indexOf("-") + 1);
            String optionB[] = sub2.split(" ");
            bSize = optionB.length;

            for (String words : optionB) {
                if (!delete.contains(words)) {
                    q = count(words, text2);
                    b += q;
                    B2.append(words).append("(").append(q).append(") ");
                } else {
                    B2.append(words).append(" ");
                }
            }

            sub3 = optionC.substring(optionC.indexOf("-") + 1);
            String optionC[] = sub3.split(" ");
            cSize = optionC.length;

            String text3 = getResponseFromGoogle(simplifiedQuestion, sub3);

            for (String words : optionC) {
                if (!delete.contains(words)) {
                    r = count(words, text3);
                    c += r;
                    C3.append(words).append("(").append(r).append(") ");
                } else {
                    C3.append(words).append(" ");
                }
            }

            p = a;
            q = b;
            r = c;

            if (aSize != 1)
                a = count(sub1.trim(), text1);
            if (bSize != 1)
                b = count(sub2.trim(), text2);
            if (cSize != 1)
                c = count(sub3.trim(), text3);

            if (a == b && b == c) {
                a = p;
                b = q;
                c = r;
            }

            A1.append("=(").append(a).append(")");
            B2.append("=(").append(b).append(")");
            C3.append("=(").append(c).append(")");

            return setAnswer(isNeg);

        } catch (Exception ioe) {
            ioe.printStackTrace();
            //Crashlytics.log(ioe.getMessage());

            error = true;
            optionRed = "b";
            return "error";
        }
    }

    private String googleSearch() {
        //int max = 0;
        boolean isNeg = false;
        a = b = c = 0;
        int p, q, r;
        reset();
        try {
            if (checkForNegative)
                isNeg = stringToArrayList(question).removeAll(Data.removeNegativeWords);
            if (isNeg && checkForNegative) {
                ArrayList<String> simplifiedQuestion = getSimplifiedQuestion(question, 1);//1 means negative words remove
                StringBuilder stringBuilder = new StringBuilder();
                for (String s : simplifiedQuestion) {
                    stringBuilder.append(s).append(" ");
                }
                this.question = stringBuilder.toString();
            }

            Log.d(TAG, "googleSearch: itIsGoogle==" + itIsGoogle);
            Log.d(TAG, "googleSearch: isWikiDone==" + isWikiDone);

            if (!itIsGoogle && !isWikiDone) {
                Log.d(TAG, "googleSearch: advance search");
                return wikiBot(isNeg);

            }

            Document doc = Jsoup.connect(Data.GOOGLE_URL + URLEncoder.encode(question, "UTF-8") + "&num=30").userAgent(getRandomUserAgent()).get();
            String text = doc.body().text().toLowerCase();

            String optionAsplit[] = optionA.split(" ");
            aSize = optionAsplit.length;

            for (String words : optionAsplit) {
                if (!delete.contains(words)) {
                    p = count(words, text);
                    a += p;
                    A1.append(words).append("(").append(p).append(") ");
                } else {
                    A1.append(words).append(" ");
                }
            }

            String optionBsplit[] = optionB.split(" ");
            bSize = optionBsplit.length;

            for (String words : optionBsplit) {
                if (!delete.contains(words)) {
                    q = count(words, text);
                    b += q;
                    B2.append(words).append("(").append(q).append(") ");
                } else {
                    B2.append(words).append(" ");
                }
            }

            String optionCsplit[] = optionC.split(" ");
            cSize = optionCsplit.length;

            for (String words : optionCsplit) {
                if (!delete.contains(words)) {
                    r = count(words, text);
                    c += r;
                    C3.append(words).append("(").append(r).append(") ");
                } else {
                    C3.append(words).append(" ");
                }
            }

            p = a;
            q = b;
            r = c;
            // now p,q,r have commutative count

            if (aSize != 1)
                a = count(optionA.trim(), text);
            if (bSize != 1)
                b = count(optionB.trim(), text);
            if (cSize != 1)
                c = count(optionC.trim(), text);

            if (a == b && b == c) {
                a = p;
                b = q;
                c = r;
            }

            if (p != 0 && aSize > 1) a *= p;
            if (q != 0 && bSize > 1) b *= q;
            if (r != 0 && cSize > 1) c *= r;

            if (a == b && b == c && !isWikiDone) {
                return wikiBot(isNeg);
            }

            A1.append("=(").append(a).append(")");
            B2.append("=(").append(b).append(")");
            C3.append("=(").append(c).append(")");

            return setAnswer(isNeg);

        } catch (Exception ioe) {
            ioe.printStackTrace();
            //Crashlytics.log(ioe.getMessage());
            error = true;
            optionRed = "b";
            return "error";

        }

    }

    private String wikiBot(boolean isNeg) {
        ArrayList<String> simplifiedQuestionList = getSimplifiedQuestion(question);
        String simplifiedQuestion = getSimplifiedString(question, null);

        final WikiSearch first = new WikiSearch(optionA, simplifiedQuestionList, simplifiedQuestion);
        final WikiSearch second = new WikiSearch(optionB, simplifiedQuestionList, simplifiedQuestion);
        final WikiSearch third = new WikiSearch(optionC, simplifiedQuestionList, simplifiedQuestion);

        first.start();
        second.start();
       third.start();
        try {
            third.join();
            second.join();
            first.join();

        } catch (Exception e) {

        }
        checkForNegative = false;
        isWikiDone = true;
        itIsGoogle = true;

        a = first.recurrence;
        b = second.recurrence;
        c = third.recurrence;

        Log.d(TAG, "wikiBot: done");

        if (a == b && b == c) {
            return googleSearch();
        }

        reset();

        A1.append(optionA).append("=(").append(a).append(")");
        B2.append(optionB).append("=(").append(b).append(")");
        C3.append(optionC).append("=(").append(c).append(")");
        return setAnswer(isNeg);
    }

    private String setAnswer(boolean isNeg) {
        if (!isNeg) {
            if (a > b) {
                if (c > a) {
                    optionRed = "c";
                } else {
                    optionRed = "a";
                }
            } else if (b > c) {
                //b is gerater than a check for c and b b>c>a
                optionRed = "b";
            } else {
                // c is greater c>b>a
                optionRed = "c";
            }
        } else {
            if (a < b) {
                if (c < a) {
                    //c is most least
                    optionRed = "c";
                } else {
                    //a is most least
                    optionRed = "a";
                }
            } else if (b < c) {
                //b is least
                optionRed = "b";
            } else {
                //c is least
                optionRed = "c";
            }
        }
        return optionRed;
    }

    @NonNull
    private String getResponseFromGoogle(String simplifiedQuestion, String sub) throws IOException {
        return Jsoup.connect(Data.GOOGLE_URL + URLEncoder.encode(simplifiedQuestion + " " + sub, "UTF-8") + "&num=10").userAgent(getRandomUserAgent()).get().body().text().toLowerCase();
    }
}


