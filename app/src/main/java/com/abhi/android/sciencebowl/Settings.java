package com.abhi.android.sciencebowl;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Settings {
    private List<Subject> subjects;
    private int difficulty;
    private int[] probs;
    final private int alpha = 4;

    public Settings() {}
    public Settings(List<Subject> subjects, int difficulty) {
        this.subjects = subjects;
        this.difficulty = difficulty;
        initProbs(difficulty);
    }

    private void initProbs(int difficulty){
        probs = new int[6];
        probs[difficulty] = 70;
        int h = difficulty + 1;
        int l = difficulty - 1;
        int count = 5;
        int weight = 15;
        int sum = 0;
        if(l >= 0) {
            probs[l--] = (short) weight;
            while (l >= 0) {
                probs[l] = (short) (probs[l + 1] / 3);
                probs[l + 1] = probs[l + 1] - probs[l];
                l--;
            }
        }
        if(h <= 5) {
            probs[h++] = (short) weight;
            while (h <= 5) {
                probs[h] = (short) (probs[h - 1] / 3);
                probs[h - 1] = probs[h - 1] - probs[h];
                h++;
            }
        }
        for(int i = 0;i <= 5;i++)
        {
            if(i != difficulty)
                sum += probs[i];
        }
        probs[difficulty] += 100-sum;
    }

    public List<Subject> getSubjects() {return subjects; }
    public int getDifficulty() {

        return difficulty;
    }

    public int getRandomDiff(){
        Random rand = new Random();
        int normalized = rand.nextInt()*100;
        int i = 0;
        while(normalized >= 0){
            normalized -= probs[i++];
        }
        return i-1;
    }

    public void adjCorrect(int diff)
    {
        if(diff == 5)
            return;
        probs[diff] -= alpha;
        probs[diff+1] += alpha;
    }

    public void adjWrong(int diff)
    {
        if(diff == 0){
            probs[diff] += alpha;
            probs[diff+1] -= alpha;
            return;
        }
        if(diff == 5) {
            probs[diff] -= alpha;
            probs[diff-1] += alpha;
        }
    }

    public void setSubjects(List<Subject> subjects) {this.subjects = subjects; }
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
        initProbs(difficulty);
    }

    public static Settings getDefault(){
        List<Subject> subj = new LinkedList<Subject>();
        subj.add(Subject.EARTH);
        subj.add(Subject.MATH);
        subj.add(Subject.CHEMISTRY);
        subj.add(Subject.PHYSICS);
        subj.add(Subject.BIOLOGY);
        subj.add(Subject.ENERGY);
        subj.add(Subject.ASTRO);
        return new Settings(subj,3);
    }
}
