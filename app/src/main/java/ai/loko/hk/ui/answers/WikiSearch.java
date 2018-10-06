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
import ai.loko.hk.ui.utils.Logger;

import static ai.loko.hk.ui.utils.Utils.count;
import static ai.loko.hk.ui.utils.Utils.getSimplifiedString;

/**
 * The type Wiki search.
 */
class WikiSearch extends Thread {
    /**
     * The Recurrence.
     */
//String TAG="wiki";
    int recurrence;
    private String option;
    private ArrayList<String> simplifiedQuestionList;
    private String simplifiedQuestion;

    /**
     * Instantiates a new Wiki search.
     *
     * @param option                 the option
     * @param simplifiedQuestionList the simplified question list
     * @param simplifiedQuestion     the simplified question
     */
    WikiSearch(String option, ArrayList<String> simplifiedQuestionList, String simplifiedQuestion) {
        this.option = option;
        this.simplifiedQuestion = simplifiedQuestion;
        this.simplifiedQuestionList = simplifiedQuestionList;
        recurrence = 0;
    }

    @Override
    public void run() {
        try {
            Document option1wiki = Jsoup.connect(Jsoup.connect(Data.BASE_SEARCH_URL + URLEncoder.encode(simplifiedQuestion + " " + option + /* i could use inurl:wikipedia.com or site:wikipedia.com but this will easily tends to human verification */" wikipedia ", "UTF-8") + "&num=2").userAgent(Data.USER_AGENT).get().select(".g>.r>a").get(0).absUrl("href")).userAgent(Data.USER_AGENT).get();
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
            Logger.logException(e);
        }
    }

}