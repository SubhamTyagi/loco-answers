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

package ai.trivia.hk.ui.data;

import java.util.ArrayList;
import java.util.Arrays;


public class Data {
    //public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64)";
    public static String USER_AGENT = System.getProperty("http.agent");
    public static boolean GRAYSCALE_IAMGE_FOR_OCR = false;
    public static boolean ENLARGE_IMAGE_FOR_OCR=false;
    public static boolean NORMAL_FALLBACK_MODE = true;
    private static final String[] remove = {"name",
            "does", "do", "is", "am", "are", "have", "has", "been", "did", "was", "not", "least", "never", "don't", "haven't", "didn't", "wasn't", "except", "wouldn't", "itsn't",
            "were", "had", "will", "would", "shall", "can", "should", "could", "may", "might", "need", "come", "comes", "means",
            "what", "who", "which", "whom", "why", "how", "when", "where",
            "of", "on", "these", "that", "this", "those", "there", "their", "at", "between", "from", "since", "for",
            "they", "and", "the", "a", "an", "with", "as", "by", "in", "to", "into", "also", "but",
            "i", "my", "me", "we", "our", "you", "your", "he", "his", "him", "himself", "them", "themselves", "it", "its", "myself", "she", "her", "yourselves",
            "&", ".", "?", ",", "matched", "paired", "pair"
    };
    public static final ArrayList<String> removeWords = new ArrayList<>(Arrays.asList(remove));

    public static String BASE_SEARCH_URL = "https://www.google.com/search?q=";
    public static String FALLBACK_SEARCH_ENGINE = "https://www.startpage.com/do/search?q=";

    public static String skip = "the of a an ? & and , in - ";
    public static boolean IMAGE_LOGS_STORAGE = true;
    public static boolean IS_TESSERACT_OCR_USE = false;
    public static String TESSERACT_LANGUAGE = "eng";
    public static boolean FAST_MODE_FOR_OCR = false;

    public static String TESSERACT_DATA="fast";

    private static String[] negativeWords = {"not", "least", "never", "incorrect", "incorrectly", "none", "cannot", "can't", "didn't"};
    public static final ArrayList<String> removeNegativeWords = new ArrayList<>(Arrays.asList(negativeWords));
    private static final String[] COMPARING_WORDS_ARRAY = {"among"};
    public static final ArrayList<String> COMPARATIVE_WORDS = new ArrayList<>(Arrays.asList(COMPARING_WORDS_ARRAY));

    public static boolean IS_THIS_REQUEST_FOR_OPTION_FOUR=false;
}
