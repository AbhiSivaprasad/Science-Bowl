package com.abhi.android.sciencebowl;

public class QuestionAnswer {
    private Question question;
    private char answer;

    public QuestionAnswer(Question question, char answer) {
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
