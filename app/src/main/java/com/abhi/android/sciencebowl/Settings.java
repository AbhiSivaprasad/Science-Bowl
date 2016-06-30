package com.abhi.android.sciencebowl;

import java.util.List;

public class Settings {
    private List<Subject> subjects;
    private int difficulty;

    public Settings() {}
    public Settings(List<Subject> subjects, int difficulty) {
        this.subjects = subjects;
        this.difficulty = difficulty;
    }

    public List<Subject> getSubjects() {return subjects; }
    public int getDifficulty() {return difficulty; }

    public void setSubjects(List<Subject> subjects) {this.subjects = subjects; }
    public void setDifficulty(int difficulty) {this.difficulty = difficulty; }
}
