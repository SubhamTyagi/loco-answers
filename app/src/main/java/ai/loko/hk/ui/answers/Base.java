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

import ai.loko.hk.ui.data.Which;
import ai.loko.hk.ui.model.Question;

/**
 * The type Base.
 */
abstract class Base extends Which {
    /**
     * The Question obj.
     */
    final Question questionObj;
    /**
     * The Option a.
     */
    final String optionA, /**
     * The Option b.
     */
    optionB, /**
     * The Option c.
     */
    optionC;
    /**
     * The A 1.
     */
    StringBuilder A1, /**
     * The B 2.
     */
    B2, /**
     * The C 3.
     */
    C3;
    /**
     * The A.
     */
    int a = 0, /**
     * The B.
     */
    b = 0, /**
     * The C.
     */
    c = 0;
    /**
     * The A size.
     */
    int aSize, /**
     * The B size.
     */
    bSize, /**
     * The C size.
     */
    cSize;
    /**
     * The Question.
     */
    String question;
    /**
     * The Option red.
     */
    String optionRed;

    /**
     * Instantiates a new Base.
     *
     * @param questionObj the question obj
     */
    public Base(Question questionObj) {
        this.questionObj = questionObj;
        this.question = questionObj.getQuestionText();
        optionA = questionObj.getOptionA();
        optionB = questionObj.getOptionB();
        optionC = questionObj.getOptionC();

        reset();
        error = false;
        checkForNegative = true;
        isWikiDone = false;
    }

    /**
     * Reset.
     */
    void reset() {
        A1 = new StringBuilder();
        B2 = new StringBuilder();
        C3 = new StringBuilder();
    }

    /**
     * Gets a 1.
     *
     * @return the a 1
     */
    public String getA1() {
        return A1.toString();
    }

    /**
     * Gets b 2.
     *
     * @return the b 2
     */
    public String getB2() {
        return B2.toString();
    }

    /**
     * Gets c 3.
     *
     * @return the c 3
     */
    public String getC3() {
        return C3.toString();
    }

    /**
     * Gets answer.
     *
     * @return the answer
     */
    public String getAnswer() {
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
}
