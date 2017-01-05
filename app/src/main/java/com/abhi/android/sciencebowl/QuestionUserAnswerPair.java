package com.abhi.android.sciencebowl;

import java.util.Objects;

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

    @Override
    public int hashCode() {
        System.out.println(question.getQuestion().hashCode());
        return question.getQuestion().hashCode();
    }

    @Override
    public boolean equals(Object a)
    {
        if(a == null)
            return false;
        if(a.getClass() != QuestionUserAnswerPair.class)
            return super.equals(a);
        return this.getQuestion().getQuestion().equals(((QuestionUserAnswerPair)a).getQuestion().getQuestion());
    }
}
