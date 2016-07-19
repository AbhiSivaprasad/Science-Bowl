package com.abhi.android.sciencebowl;

import com.facebook.AccessToken;

import java.util.ArrayList;
import java.util.List;

public final class UserInformation {

    private static String uid;

    public static String getFbUid() {
        return fbUid;
    }

    public static void setFbUid(String fbUid) {
        UserInformation.fbUid = fbUid;
    }

    private static String fbUid;
    private static Settings userSettings;
    private static List<QuestionUserAnswerPair> reviewQuestionBank;
    private static int questionsCorrect; //tracked statistics

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
}
