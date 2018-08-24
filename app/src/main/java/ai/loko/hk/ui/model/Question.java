package ai.loko.hk.ui.model;

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
        this.optionA = optionA.toLowerCase();
        this.optionB = optionB.toLowerCase();
        this.optionC = optionC.toLowerCase();
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
