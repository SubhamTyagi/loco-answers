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

package ai.trivia.hk.ui.model;

public class Question {

    private final String questionText;
    private final String optionA;
    private final String optionB;
    private final String optionC;
    private boolean isNegative;
    private String answer;


    public boolean isNegative() {
        return isNegative;
    }

    public Question setNegative(boolean negative) {
        isNegative = negative;
        return this;
    }

    public Question(String questionText, String optionA, String optionB, String optionC) {
        if (questionText.contains("?")) {
            this.questionText = questionText.substring(0, questionText.length() - 1).toLowerCase();
        } else
            this.questionText = questionText.toLowerCase();
        this.optionA = optionA.toLowerCase().replace(".","");
        this.optionB = optionB.toLowerCase().replace(".","");
        this.optionC = optionC.toLowerCase().replace(".","");
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getOptionA() {
        return optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public String getOptionC() {
        return optionC;
    }

}
