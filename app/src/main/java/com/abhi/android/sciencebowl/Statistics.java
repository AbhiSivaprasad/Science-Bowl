package com.abhi.android.sciencebowl;

import java.util.HashMap;
import java.util.Map;

public final class Statistics {
    class SubjectStatistics {
        Subject subject;
        int questionsCorrect, questionsWrong;

        SubjectStatistics(Subject subject) {
            questionsCorrect = 0;
            questionsWrong = 0;
            this.subject = subject;
        }

        public int getQuestionsCorrect() {return questionsCorrect;}
        public int getQuestionsWrong() {return questionsWrong;}
        public int getQuestionsTotal() {return getQuestionsCorrect() + getQuestionsWrong();}

        public void incrementQuestionsCorrect() {questionsCorrect++;}
        public void incrementQuestionsWrong() {questionsWrong++;}
    }

    Map<Subject, SubjectStatistics> subjectStatisticsMap;

    Statistics(Subject[] subjects) {
        subjectStatisticsMap = new HashMap<>();
        for (Subject subject : subjects) {
            subjectStatisticsMap.put(subject, new SubjectStatistics(subject));
        }
    }

    public int getQuestionsCorrect(Subject subject) {
        return subjectStatisticsMap.get(subject).getQuestionsCorrect();
    }

    public int getQuestionsWrong(Subject subject) {
        return subjectStatisticsMap.get(subject).getQuestionsWrong();
    }

    public double getQuestionsPercentage(Subject subject) {
        SubjectStatistics subjStat = subjectStatisticsMap.get(subject);
        if (subjStat.getQuestionsTotal() != 0)
            return 100.0 * subjStat.getQuestionsCorrect() / subjStat.getQuestionsTotal();
        else return 0;
    }

    public void incrementQuestionsCorrect(Subject subject) {
        subjectStatisticsMap.get(subject).incrementQuestionsCorrect();
    }

    public void incrementQuestionsWrong(Subject subject) {
        subjectStatisticsMap.get(subject).incrementQuestionsWrong();
    }
}