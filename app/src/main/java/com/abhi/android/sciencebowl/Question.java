package com.abhi.android.sciencebowl;

public class Question {
    private String question;
    private Choice correct;
    private String W;
    private String X;
    private String Y;
    private String Z;

    Question() {}
    Question(String question, String W, String X, String Y, String Z, Choice correct) {
        this.question = question;
        this.W = W;
        this.X = X;
        this.Y = Y;
        this.Z = Z;
        this.correct = correct;
    }
    
    public String getQuestion() {return question;}
    public Choice getCorrect() {return correct;}
    public String getW() {return W;}
    public String getX() {return X;}
    public String getY() {return Y;}
    public String getZ() {return Z;}
}
