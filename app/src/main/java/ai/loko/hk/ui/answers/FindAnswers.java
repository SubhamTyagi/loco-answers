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

package ai.loko.hk.ui.answers;



import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URLEncoder;
import java.util.ArrayList;

import ai.loko.hk.ui.data.Data;
import ai.loko.hk.ui.data.Which;
import ai.loko.hk.ui.utils.Logger;

import static ai.loko.hk.ui.data.Data.skip;
import static ai.loko.hk.ui.utils.Utils.count;
import static ai.loko.hk.ui.utils.Utils.getSimplifiedQuestion;
import static ai.loko.hk.ui.utils.Utils.getSimplifiedString;
import static ai.loko.hk.ui.utils.Utils.stringToArrayList;


/**
 * The type Find answers.
 */
@Deprecated
public class FindAnswers extends Which {

    // private static String TAG = "FindAnswers";
    // private final Context context;
    private final String A;
    final private String B;
    final private String C;
    //Answer current;
    //private boolean checkForNegative;
    //private boolean wikiDone;
    //static boolean isOcr;
    private boolean error;
    private StringBuilder A1, B2, C3;
    private String question;
    private int a = 0, b = 0, c = 0;
    private String optionRed;

    private int aSize, bSize, cSize;

    /**
     * Instantiates a new Find answers.
     *
     * @param question the question
     * @param optionA  the option a
     * @param optionB  the option b
     * @param optionC  the option c
     */
    @Deprecated
    public FindAnswers(String question, String optionA, String optionB, String optionC) {
        A = optionA.toLowerCase();
        B = optionB.toLowerCase();
        C = optionC.toLowerCase();

        // init();
        // if (question.contains("?")) {
        //     this.question = question.substring(0, question.length() - 1).toLowerCase();
        // } else

        this.question = question.toLowerCase();
        error = false;
        checkForNegative = true;
        isWikiDone = false;
    }

    private void init() {
        A1 = new StringBuilder();
        B2 = new StringBuilder();
        C3 = new StringBuilder();
    }

    /**
     * Gets option red.
     *
     * @return the option red
     */
    public String getOptionRed() {
        return optionRed;
    }

    /**
     * Is error boolean.
     *
     * @return the boolean
     */
    public boolean isError() {
        return error;
    }

    /**
     * Gets acount.
     *
     * @return the acount
     */
    public String getAcount() {
        return A1.toString();

    }

    /**
     * Gets bcount.
     *
     * @return the bcount
     */
    public String getBcount() {
        return B2.toString();
    }

    /**
     * Gets ccount.
     *
     * @return the ccount
     */
    public String getCcount() {
        return C3.toString();

    }

   /*
    public String search() {
       *//* if (A.contains("-")) {
            if (B.contains("-") && C.contains("-")) {

                // Log.d(TAG, "search: paired question");
                // this time it is not handling properly pair question
                return pairGoogleSearch();

            } else return googleSearch();
        }*//*
        return googleSearch();
    }*/


    private String pairGoogleSearch() {
        boolean isNeg;
        a = b = c = 0;
        int p, q, r;
        init();
        try {
            isNeg = stringToArrayList(question).removeAll(Data.removeNegativeWords);
            if (isNeg) {
                ArrayList<String> simplifiedQuestion = getSimplifiedQuestion(question, 1);//1 means negative words remove
                StringBuilder stringBuilder = new StringBuilder();
                for (String s : simplifiedQuestion) {
                    stringBuilder.append(s).append(" ");
                }
                this.question = stringBuilder.toString();
                // Log.d(TAG, "negative question");
            }

            String simplifiedQuestion = getSimplifiedString(question, null);
            ///Log.d(TAG, "simplified question is===" + simplifiedQuestion);

            String sub1 = A.substring(0, A.indexOf("-"));
            String sub2 = B.substring(0, B.indexOf("-"));
            String sub3 = C.substring(0, C.indexOf("-"));

            //  Log.d(TAG, "option1 prefix==" + sub1);
            //  Log.d(TAG, "option2 prefix==" + sub2);
            //  Log.d(TAG, "option3 prefix==" + sub3);


            Document doc = Jsoup.connect(Data.BASE_SEARCH_URL + URLEncoder.encode(sub1 + " " + simplifiedQuestion, "UTF-8") + "&num=10").userAgent(Data.USER_AGENT).get();
            String text1 = doc.body().text().toLowerCase();

            A1.append(sub1).append("-");
            B2.append(sub2).append("-");
            C3.append(sub3).append("-");

            sub1 = A.substring(A.indexOf("-") + 1);
            //Log.d(TAG, "final option is " + sub1);
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

            doc = Jsoup.connect(Data.BASE_SEARCH_URL + URLEncoder.encode(sub2 + " " + simplifiedQuestion, "UTF-8") + "&num=10").userAgent(Data.USER_AGENT).get();
            String text2 = doc.body().text().toLowerCase();

            sub2 = B.substring(B.indexOf("-") + 1);
            // Log.d(TAG, "final option is " + sub2);
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


            sub3 = C.substring(C.indexOf("-") + 1);
            //  Log.d(TAG, "final option is " + sub3);
            String optionC[] = sub3.split(" ");
            cSize = optionC.length;

            doc = Jsoup.connect(Data.BASE_SEARCH_URL + URLEncoder.encode(sub3 + " " + simplifiedQuestion, "UTF-8") + "&num=10").userAgent(Data.USER_AGENT).get();
            String text3 = doc.body().text().toLowerCase();

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

            if (!isNeg) {
                if (a > b) {
                    if (c > a) {
                        //c ic greater c>a>b
                        //answer = C;
                        // max = c;
                        optionRed = "c";
                    } else {
                        //a is greater a>c>b
                        //answer = A;
                        // max = a;
                        optionRed = "a";
                    }
                } else if (b > c) {//b is gerater than a check for c and b b>c>a
                    //answer = B;
                    ///max = b;
                    optionRed = "b";
                } else { // c is greater c>b>a
                    //answer = C;
                    //max = c;
                    optionRed = "c";

                }

            } else {
                if (a < b) {
                    if (c < a) {
                        //c is most least
                        //answer = C;
                        // max = c;
                        optionRed = "c";
                    } else {
                        //a is most least
                        // answer = A;
                        /// max = a;
                        optionRed = "a";
                    }
                } else if (b < c) {
                    //b is least
                    //answer = B;
                    /// max = b;
                    optionRed = "b";

                } else {
                    //c is least
                    //answer = C;
                    ///max = c;
                    optionRed = "c";
                }
            }

        } catch (Exception ioe) {
            ioe.printStackTrace();
           Logger.logException(ioe);
            //  Crashlytics.log(ioe.getMessage());
            error = true;
            optionRed = "b";
            return "error";

        }

        return optionRed;

    }

    private String search() {
        //int max = 0;
        boolean isNeg = false;
        a = b = c = 0;
        int p, q, r;
        init();

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

            if (!itIsGoogle && !isWikiDone) {
                //return wikiBot(isNeg);
                return wikiBot(isNeg);
            }

            Document doc = Jsoup.connect(Data.BASE_SEARCH_URL + URLEncoder.encode(question, "UTF-8") + "&num=30").userAgent(Data.USER_AGENT).get();

            String text = doc.body().text().toLowerCase();
            String optionA[] = A.split(" ");
            aSize = optionA.length;

            for (String words : optionA) {
                if (!skip.contains(words)) {
                    p = count(words, text);
                    a += p;
                    A1.append(words).append("(").append(p).append(") ");
                } else {
                    A1.append(words).append(" ");
                }
            }

            String optionB[] = B.split(" ");
            bSize = optionB.length;

            for (String words : optionB) {
                if (!skip.contains(words)) {
                    q = count(words, text);
                    b += q;
                    B2.append(words).append("(").append(q).append(") ");
                } else {
                    B2.append(words).append(" ");
                }
            }

            String optionC[] = C.split(" ");
            cSize = optionC.length;

            for (String words : optionC) {
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
                a = count(A.trim(), text);
            if (bSize != 1)
                b = count(B.trim(), text);
            if (cSize != 1)
                c = count(C.trim(), text);

            if (a == b && b == c) {
                a = p;
                b = q;
                c = r;
            }

            if (a == b && b == c && !isWikiDone) {
                return wikiBot(isNeg);
            }
            if (p != 0)
                a *= p;
            if (q != 0)
                b *= q;
            if (r != 0)
                c *= r;

            //if (aSize != 1)
            A1.append("=(").append(a).append(")");
            //if (bSize != 1)
            B2.append("=(").append(b).append(")");
            //if (cSize != 1)
            C3.append("=(").append(c).append(")");

            if (!isNeg) {
                if (a > b) {
                    if (c > a) {
                        //c ic greater c>a>b
                        //answer = C;
                        // max = c;
                        optionRed = "c";
                    } else {
                        //a is greater a>c>b
                        //answer = A;
                        // max = a;
                        optionRed = "a";
                    }
                } else if (b > c) {//b is gerater than a check for c and b b>c>a
                    //answer = B;
                    ///max = b;
                    optionRed = "b";
                } else { // c is greater c>b>a
                    //answer = C;
                    //max = c;
                    optionRed = "c";
                }
            } else {
                if (a < b) {
                    if (c < a) {
                        //c is most least
                        //answer = C;
                        // max = c;
                        optionRed = "c";
                    } else {
                        //a is most least
                        // answer = A;
                        /// max = a;
                        optionRed = "a";
                    }
                } else if (b < c) {
                    //b is least
                    //answer = B;
                    /// max = b;
                    optionRed = "b";

                } else {
                    //c is least
                    //answer = C;
                    ///max = c;
                    optionRed = "c";
                }
            }

        } catch (Exception ioe) {
            ioe.printStackTrace();
           Logger.logException(ioe);
            //Crashlytics.log(ioe.getMessage());
            error = true;
            optionRed = "b";
            return "error";

        }

        return optionRed;

    }

    private String wikiBot(boolean isNeg) {
        //int x, y, z;
        ArrayList<String> simplifiedQuestionList = getSimplifiedQuestion(question);
        String simplifiedQuestion = getSimplifiedString(question, null);

        final WikiSearch first = new WikiSearch(A, simplifiedQuestionList, simplifiedQuestion);
        final WikiSearch second = new WikiSearch(B, simplifiedQuestionList, simplifiedQuestion);
        final WikiSearch third = new WikiSearch(C, simplifiedQuestionList, simplifiedQuestion);

        checkForNegative = false;
        isWikiDone = true;
        itIsGoogle = true;

        first.start();
        second.start();
        third.start();

        try {
            third.join();
            second.join();
            first.join();

        } catch (Exception e) {
           Logger.logException(e);
        }


        a = first.recurrence;
        b = second.recurrence;
        c = third.recurrence;

        if (a == b && b == c) {
            checkForNegative = false;
            isWikiDone = true;
            itIsGoogle = true;
            return search();
        }

        init();

        A1.append(A).append("=(").append(a).append(")");
        B2.append(B).append("=(").append(b).append(")");
        C3.append(C).append("=(").append(c).append(")");

        if (!isNeg) {
            if (a > b) {
                if (c > a) {
                    optionRed = "c";
                } else {
                    optionRed = "a";
                }
            } else if (b > c) {//b is gerater than a check for c and b b>c>a
                optionRed = "b";
            } else { // c is greater c>b>a

                optionRed = "c";

            }
        } else {
            if (a < b) {
                if (c < a) {
                    //c is most least
                    //answer = C;
                    // max = c;
                    optionRed = "c";
                } else {
                    //a is most least
                    // answer = A;
                    //max = a;
                    optionRed = "a";
                }
            } else if (b < c) {
                //b is least
                //answer = B;
                //max = b;
                optionRed = "b";

            } else {
                //c is least
                //answer = C;
                //max = c;
                optionRed = "c";
            }
        }

        return optionRed;
    }


}

