package com.abhi.android.sciencebowl;

/**
 * Created by abhi on 6/26/2016.
 */
public final class UserInformation {
    private static String username;
    private static Settings userSettings;

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
}
