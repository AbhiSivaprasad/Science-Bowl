package com.abhi.android.sciencebowl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class UserInformation {

    private static String uid;
    private static Settings userSettings;
    private static List<QuestionUserAnswerPair> reviewQuestionBank;
    private static int questionsCorrect; //tracked statistics
    private static boolean isScoreCached;
    private static int cachedScore;
    private static Statistics stats;

    public static String getUid() {
        return uid;
    }
    public static void setUid(String uid) {
        UserInformation.uid = uid;
    }

    public static Settings getUserSettings() {
        return userSettings;
    }
    public static void setUserSettings(Settings userSettings) {
        UserInformation.userSettings = userSettings;
    }

    public static List<QuestionUserAnswerPair> getReviewQuestionBank() {
        if(reviewQuestionBank == null)
            return new ArrayList<QuestionUserAnswerPair>();

        return reviewQuestionBank;
    }
    public static void setReviewQuestionBank(List<QuestionUserAnswerPair> reviewQuestionBank) {
        UserInformation.reviewQuestionBank = reviewQuestionBank;
    }

    public static int getQuestionsCorrect() {return questionsCorrect; }
    public static void setQuestionsCorrect(int questionsCorrect) {
        UserInformation.questionsCorrect = questionsCorrect;
    }

    public static int getCachedScore() {return cachedScore; }
    public static void setCachedScore(int score) {
        UserInformation.cachedScore = score;
    }

    public static boolean getIsScoreCached() {return isScoreCached;}
    public static void setIsScoreCached(boolean isCached) {
        UserInformation.isScoreCached = isCached;
    }

    public static Statistics getStats() {
        if (stats == null) return new Statistics(Subject.values());
        else return stats;
    }
    public static void setStats(Statistics stats) {
        UserInformation.stats = stats;
    }
}
