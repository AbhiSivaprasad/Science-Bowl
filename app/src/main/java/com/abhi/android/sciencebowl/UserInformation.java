package com.abhi.android.sciencebowl;

import java.util.ArrayList;
import java.util.List;

public final class UserInformation {
    private static String username;
    private static String uid;
    private static Settings userSettings;
    private static List<QuestionAnswer> reviewQuestionBank;
    private static int currentQuestionIndex;
    private static int questionsCorrect; //tracked statistics

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

    public static List<QuestionAnswer> getReviewQuestionBank() {
        if(reviewQuestionBank == null)
            return new ArrayList<QuestionAnswer>();

        return reviewQuestionBank;
    }
    public static void setReviewQuestionBank(List<QuestionAnswer> reviewQuestionBank) {
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
}
