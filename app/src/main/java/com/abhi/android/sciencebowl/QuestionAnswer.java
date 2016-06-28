package com.abhi.android.sciencebowl;

/**
 * Created by abhi on 6/27/2016.
 */
public class QuestionAnswer {
    private Question mQuestion;
    private char mAnswer;

    public QuestionAnswer(Question question, char answer) {
        this.mQuestion = question;
        this.mAnswer = answer;
    }

    public Question getQuestion() {
        return mQuestion;
    }

    public char getAnswer() {
        return mAnswer;
    }
}
