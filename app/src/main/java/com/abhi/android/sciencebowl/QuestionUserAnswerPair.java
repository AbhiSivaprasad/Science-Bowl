package com.abhi.android.sciencebowl;

public class QuestionUserAnswerPair {
    private Question question;
    private Choice answer;

    public QuestionUserAnswerPair(Question question, Choice answer) {
        this.question = question;
        this.answer = answer;
    }

    public Question getQuestion() {
        return question;
    }

    public Choice getAnswer() {
        return answer;
    }


}
