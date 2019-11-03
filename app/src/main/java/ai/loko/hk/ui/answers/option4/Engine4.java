package ai.loko.hk.ui.answers.option4;

/*
 *   Copyright (C) 2018 SHUBHAM TYAGI
 *
 *    This file is part of Trivia Hack.
 *     Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3.0 (the "License"); you may not
 *     use this file except in compliance with the License. You may obtain a copy of
 *     the License at
 *
 *     https://www.gnu.org/licenses/gpl-3.0
 *
 *    Trivia Hack is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Trivia Hack.  If not, see <http://www.gnu.org/licenses/>.
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


import androidx.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import ai.loko.hk.ui.data.Data;
import ai.loko.hk.ui.model.option4.Question4;
import ai.loko.hk.ui.utils.Logger;

import static ai.loko.hk.ui.data.Data.skip;
import static ai.loko.hk.ui.utils.Utils.count;
import static ai.loko.hk.ui.utils.Utils.getSimplifiedQuestion;
import static ai.loko.hk.ui.utils.Utils.getSimplifiedString;
import static ai.loko.hk.ui.utils.Utils.stringToArrayList;

/**
 * The Search Engine.
 */
public class Engine4 extends Base4 {
    private final String TAG = "Engine4";

    public Engine4(Question4 questionObj) {
        super(questionObj);
    }

    //currently not used but will be...
    private String pairSearch() {
        boolean isNeg;
        a = b = c = d = 0;
        int p, q, r, s;
        reset();
        try {
            isNeg = stringToArrayList(question).removeAll(Data.removeNegativeWords);
            if (isNeg) {
                ArrayList<String> simplifiedQuestion = getSimplifiedQuestion(question, 1);//1 means negative words remove
                StringBuilder stringBuilder = new StringBuilder();
                for (String s1 : simplifiedQuestion) {
                    stringBuilder.append(s1).append(" ");
                }
                this.question = stringBuilder.toString();
            }

            String simplifiedQuestion = getSimplifiedString(question, null);

            String sub1 = optionA.substring(0, optionA.indexOf("-") - 1);
            String sub2 = optionB.substring(0, optionB.indexOf("-") - 1);
            String sub3 = optionC.substring(0, optionC.indexOf("-") - 1);
            String sub4 = optionD.substring(0, optionD.indexOf("-") - 1);


            A1.append(sub1).append("-");
            B2.append(sub2).append("-");
            C3.append(sub3).append("-");
            D4.append(sub4).append("-");

            String text1 = getResponseFromInternet(simplifiedQuestion, sub1);
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


            sub2 = optionB.substring(optionB.indexOf("-") + 1);
            String optionB[] = sub2.split(" ");
            bSize = optionB.length;
            String text2 = getResponseFromInternet(simplifiedQuestion, sub2);
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

            String text3 = getResponseFromInternet(simplifiedQuestion, sub3);

            for (String words : optionC) {
                if (!skip.contains(words)) {
                    r = count(words, text3);
                    c += r;
                    C3.append(words).append("(").append(r).append(") ");
                } else {
                    C3.append(words).append(" ");
                }
            }

            sub4 = optionD.substring(optionD.indexOf("-") + 1);
            String optionD[] = sub3.split(" ");
            cSize = optionC.length;

            String text4 = getResponseFromInternet(simplifiedQuestion, sub4);

            for (String words : optionD) {
                if (!skip.contains(words)) {
                    r = count(words, text4);
                    c += r;
                    D4.append(words).append("(").append(r).append(") ");
                } else {
                    D4.append(words).append(" ");
                }
            }

            p = a;
            q = b;
            r = c;
            s = d;

            if (aSize != 1)
                a = count(sub1.trim(), text1);
            if (bSize != 1)
                b = count(sub2.trim(), text2);
            if (cSize != 1)
                c = count(sub3.trim(), text3);

            if (dSize != 1)
                d = count(sub4.trim(), text4);

            if (a == b && b == c && a == d) {
                a = p;
                b = q;
                c = r;
                d = s;
            }

            A1.append("=(").append(a).append(")");
            B2.append("=(").append(b).append(")");
            C3.append("=(").append(c).append(")");
            D4.append("=(").append(c).append(")");

            return setAnswer(isNeg);

        } catch (Exception ioe) {
            Logger.logException(ioe);
            error = true;
            optionRed = "b";
            return "error";
        }
    }

    synchronized public String search() {
        boolean isNeg = false;
        a = b = c = d = 0;
        int p, q, r, s;
        reset();
        try {
            if (checkForNegative)
                isNeg = stringToArrayList(question).removeAll(Data.removeNegativeWords);
            if (isNeg && checkForNegative) {
                ArrayList<String> simplifiedQuestion = getSimplifiedQuestion(question, 1);//1 means negative words remove
                StringBuilder stringBuilder = new StringBuilder();
                for (String s65 : simplifiedQuestion) {
                    stringBuilder.append(s65).append(" ");
                }
                this.question = stringBuilder.toString();
            }

            Document doc = Jsoup.connect(BASE_URL + URLEncoder.encode(question, "UTF-8") + "&num=20").userAgent(Data.USER_AGENT).get();

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

            String optionDsplit[] = optionD.split(" ");
            dSize = optionDsplit.length;
            for (String words : optionDsplit) {
                if (!skip.contains(words)) {
                    s = count(words, text);
                    d += s;
                    D4.append(words).append("(").append(s).append(") ");
                } else {
                    D4.append(words).append(" ");
                }
            }

            p = a;
            q = b;
            r = c;
            s = d;
            // now p,q,r have commutative count

            if (aSize != 1)
                a = count(optionA.trim(), text);
            if (bSize != 1)
                b = count(optionB.trim(), text);
            if (cSize != 1)
                c = count(optionC.trim(), text);

            if (dSize != 1)
                d = count(optionD.trim(), text);
            if (a == b && b == c && a == d) {
                a = p;
                b = q;
                c = r;
                d = s;
            }

            if (p != 0 && aSize > 1) a *= p;
            if (q != 0 && bSize > 1) b *= q;
            if (r != 0 && cSize > 1) c *= r;

            if (s != 0 && cSize > 1) d *= s;

            if (a == b && b == c && c == d && !isFallbackDone) {
                return fallbackSearch(isNeg);
            }

            A1.append("=(").append(a).append(")");
            B2.append("=(").append(b).append(")");
            C3.append("=(").append(c).append(")");
            D4.append("=(").append(d).append(")");
            return setAnswer(isNeg);

        } catch (Exception ioe) {
            ioe.printStackTrace();
            if (!isFallbackDone) {
                return fallbackSearch(isNeg);
            }
            error = true;
            Logger.logException(ioe);

            // if there is error (bad luck) then set option 'b'
            optionRed = "b";
            return "error";
        }
    }

    private String fallbackSearch(boolean isNeg) {
        //ArrayList<String> simplifiedQuestionList = getSimplifiedQuestion(question);
        //String simplifiedQuestion = getSimplifiedString(question, null);
        checkForNegative = false;
        isFallbackDone = true;
        BASE_URL = Data.FALLBACK_SEARCH_ENGINE;
        return search();
    }

    private String setAnswer(boolean isNeg) {
        if (!isNeg) {
            if (a > b && a > c && a > d) {
                optionRed = "a";
            } else if (b > c && b > d) {
                optionRed = "b";
            } else if (c > d) {
                optionRed = "c";
            } else optionRed = "d";
        } else {
            if (a < b && a < c && a < d) {
                optionRed = "a";
            } else if (b < c && b < d && b < a) {
                optionRed = "b";
            } else if (c < d && c < a && c < b) {
                optionRed = "c";
            } else optionRed = "d";
        }

        //Log.d(TAG, "setAnswer: optionRed=="+optionRed);
        return optionRed;
    }

    @NonNull
    private String getResponseFromInternet(String simplifiedQuestion, String sub) throws IOException {
        return Jsoup.connect(BASE_URL + URLEncoder.encode(simplifiedQuestion + " " + sub, "UTF-8") + "&num=5").userAgent(Data.USER_AGENT).get().body().text().toLowerCase();
    }
}


