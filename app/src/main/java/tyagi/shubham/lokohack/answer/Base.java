package ai.loko.hk.ui.answers;

import ai.loko.hk.ui.data.Which;
import ai.loko.hk.ui.model.Question;

abstract class Base extends Which {
    final Question questionObj;
    final String optionA, optionB, optionC;
    StringBuilder A1, B2, C3;
    int a = 0, b = 0, c = 0;
    int aSize, bSize, cSize;
    String question;
    String optionRed;

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

    void reset() {
        A1 = new StringBuilder();
        B2 = new StringBuilder();
        C3 = new StringBuilder();
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

    public String getAnswer() {
        return optionRed;
    }

    public boolean isError() {
        return error;
    }
}
