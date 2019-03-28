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

package ai.loko.hk.ui.answers.option4;

import ai.loko.hk.ui.data.Data;
import ai.loko.hk.ui.data.Which;
import ai.loko.hk.ui.model.option4.Question4;


abstract class Base4 extends Which {
    protected static String BASE_URL;
    final Question4 questionObj;
    final String optionA;
    final String optionB;
    final String optionC;
    final String optionD;
    StringBuilder A1;
    StringBuilder B2;
    StringBuilder C3;
    StringBuilder D4;
    int a = 0,
            b = 0,
            c = 0,
            d = 0;
    int aSize,
            bSize,
            cSize,
            dSize;

    String question;
    String optionRed;

    public Base4(Question4 questionObj) {
        this.questionObj = questionObj;
        this.question = questionObj.getQuestionText();
        optionA = questionObj.getOptionA();
        optionB = questionObj.getOptionB();
        optionC = questionObj.getOptionC();
        optionD = questionObj.getOptionD();

        reset();
        BASE_URL = Data.BASE_SEARCH_URL;
        error = false;
        checkForNegative = true;
        isFallbackDone = false;
    }

    void reset() {
        A1 = new StringBuilder();
        B2 = new StringBuilder();
        C3 = new StringBuilder();
        D4 = new StringBuilder();
    }

    public String getA1() {
        return A1.toString();
    }

    public String getB2() {
        return B2.toString();
    }

    public String getC3() {
        return C3.toString();
    }

    public String getD4() {
        return D4.toString();
    }

    public String getAnswer() {
        return optionRed;
    }

    public boolean isError() {
        return error;
    }
}
