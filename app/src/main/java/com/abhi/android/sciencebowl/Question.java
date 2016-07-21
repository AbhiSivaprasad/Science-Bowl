package com.abhi.android.sciencebowl;

public class Question {
    private Subject subject;
    private String question;
    private Choice correct;
    private String W;
    private String X;
    private String Y;
    private String Z;

    Question() {}
    Question(Subject subject, String question, String W, String X, String Y, String Z, Choice correct) {
        this.subject = subject;
        this.question = question;
        this.W = W;
        this.X = X;
        this.Y = Y;
        this.Z = Z;
        this.correct = correct;
    }
    
    public String getQuestion() {return question;}
    public Choice getCorrect() {return correct;}
    public Subject getSubject() {return subject;}
    public String getW() {return W;}
    public String getX() {return X;}
    public String getY() {return Y;}
    public String getZ() {return Z;}
}
