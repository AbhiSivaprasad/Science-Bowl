package com.abhi.android.sciencebowl;

public class Question {
    private String question;
    private Choice correct;
    private String w;
    private String x;
    private String y;
    private String z;

    Question() {}
    Question(String question, String W, String X, String Y, String Z, Choice correct) {
        this.question = question;
        this.w = W;
        this.x = X;
        this.y = Y;
        this.z = Z;
        this.correct = correct;
    }
    
    public String getQuestion() {return question;}
    public Choice getCorrect() {return correct;}
    public String getW() {return w;}
    public String getX() {return x;}
    public String getY() {return y;}
    public String getZ() {return z;}
}
