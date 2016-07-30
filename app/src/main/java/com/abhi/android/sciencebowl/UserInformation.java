package com.abhi.android.sciencebowl;

import com.facebook.AccessToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class UserInformation {

    private static String uid;

    public static String getFbUid() {
        return fbUid;
    }

    public static void setFbUid(String fbUid) {
        UserInformation.fbUid = fbUid;
    }

    private static String fbUid;

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        UserInformation.name = name;
    }

    private static String name;
    private static Settings userSettings;
    private static List<QuestionUserAnswerPair> reviewQuestionBank;
    private static int questionsCorrect; //tracked statistics
    private static boolean isScoreCached;
    private static int cachedScore;
    private static Statistics stats;

    public static AccessToken getFbToken() {
        return fbToken;
    }

    public static void setFbToken(AccessToken fbToken) {
        UserInformation.fbToken = fbToken;
    }

    private static AccessToken fbToken;

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
