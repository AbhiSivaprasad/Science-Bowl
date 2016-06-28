package com.abhi.android.sciencebowl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhi on 6/26/2016.
 */
public final class UserInformation {
    private static String username;
    private static Settings userSettings;
    private static List<QuestionAnswer> reviewQuestionBank;

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
}
