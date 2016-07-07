package com.abhi.android.sciencebowl;

public class QuestionUserAnswerPair {
    private Question question;
    private Choice userAnswer;

    public QuestionUserAnswerPair(Question question, Choice userAnswer) {
        this.question = question;
        this.userAnswer = userAnswer;
    }

    public Question getQuestion() {
        return question;
    }

    public Choice getAnswer() {
        return userAnswer;
    }


}
