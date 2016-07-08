package com.abhi.android.sciencebowl;

import java.util.ArrayList;
import java.util.List;

public final class UserInformation {
    private static String username;
    private static String uid;
    private static Settings userSettings;
    private static List<QuestionUserAnswerPair> reviewQuestionBank;
    private static int currentQuestionIndex;
    private static int questionsCorrect; //tracked statistics
    private static boolean isScoreCached;
    private static int cachedScore;

    public static String getUid() {
        return uid;
    }

    public static void setUid(String uid) {
        UserInformation.uid = uid;
    }

    public static String getUsername() {
        return username;
    }
    public static void setUsername(String username) {
        UserInformation.username = username;
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

    public static int getCurrentQuestionIndex() {return currentQuestionIndex; }
    public static void setCurrentQuestionIndex(int currentQuestionIndex) {
        UserInformation.currentQuestionIndex = currentQuestionIndex;
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
}
