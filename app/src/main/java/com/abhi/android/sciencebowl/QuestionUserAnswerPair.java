package com.abhi.android.sciencebowl;

public class QuestionUserAnswerPair {
    private Question question;
    private char answer;

    public QuestionUserAnswerPair(Question question, char answer) {
        this.question = question;
        this.answer = answer;
    }

    public Question getQuestion() {
        return question;
    }

    public char getAnswer() {
        return answer;
    }


}
