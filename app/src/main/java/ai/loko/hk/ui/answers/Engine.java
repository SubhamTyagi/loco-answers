/*
 *   Copyright (C) 2018 SHUBHAM TYAGI
 *
 *    This file is part of LoKo HacK.
 *     Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3.0 (the "License"); you may not
 *     use this file except in compliance with the License. You may obtain a copy of
 *     the License at
 *
 *     https://www.gnu.org/licenses/gpl-3.0
 *
 *    LoKo hacK is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with LoKo Hack.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 *
 */

package ai.loko.hk.ui.answers;

import android.support.annotation.NonNull;
import android.util.Log;

import com.balsikandar.crashreporter.CrashReporter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import ai.loko.hk.ui.data.Data;
import ai.loko.hk.ui.model.Question;

import static ai.loko.hk.ui.data.Data.skip;
import static ai.loko.hk.ui.utils.Utils.count;
import static ai.loko.hk.ui.utils.Utils.getSimplifiedQuestion;
import static ai.loko.hk.ui.utils.Utils.getSimplifiedString;
import static ai.loko.hk.ui.utils.Utils.stringToArrayList;

/**
 * The Search Engine.
 */
public class Engine extends Base {
    private final String TAG = "Engine";

    /**
     * Instantiates a new Engine.
     *
     * @param questionObj the question obj
     */
    public Engine(Question questionObj) {
        super(questionObj);
    }

    /**
     * Search and return result .
     *
     * @return the string
     */
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
                if (!skip.contains(words)) {
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
                if (!skip.contains(words)) {
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
                if (!skip.contains(words)) {
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
           CrashReporter.logException(ioe);
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

            Document doc = Jsoup.connect(Data.BASE_SEARCH_URL + URLEncoder.encode(question, "UTF-8") + "&num=30").userAgent(Data.USER_AGENT).get();
            String text = doc.body().text().toLowerCase();

            String optionAsplit[] = optionA.split(" ");
            aSize = optionAsplit.length;

            for (String words : optionAsplit) {
                if (!skip.contains(words)) {
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
                if (!skip.contains(words)) {
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
                if (!skip.contains(words)) {
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
            CrashReporter.logException(ioe);
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
            CrashReporter.logException(e);
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
        return Jsoup.connect(Data.BASE_SEARCH_URL + URLEncoder.encode(simplifiedQuestion + " " + sub, "UTF-8") + "&num=10").userAgent(Data.USER_AGENT).get().body().text().toLowerCase();
    }
}


