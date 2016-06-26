package com.abhi.android.sciencebowl;

/**
 * Created by abhi on 6/26/2016.
 */
public class Settings {
    private String subject;
    private int difficulty;

    public Settings() {}
    public Settings(String subject, int difficulty) {
        this.subject = subject;
        this.difficulty = difficulty;
    }

    public String getSubject() {return subject; }
    public int getDifficulty() {return difficulty; }

    public void setSubject(String subject) {this.subject = subject; }
    public void setDifficulty(int difficulty) {this.difficulty = difficulty; }
}
