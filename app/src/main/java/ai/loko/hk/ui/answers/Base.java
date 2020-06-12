

package ai.loko.hk.ui.answers;

import ai.loko.hk.ui.data.Data;
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

    protected static String BASE_URL;
    boolean isNeg;

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
        BASE_URL=Data.BASE_SEARCH_URL;
        error = false;
        checkForNegative = true;
        isFallbackDone = false;
        isSearchWithQuesAndOptInFallbackDone = false;
        isNeg = false;
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
