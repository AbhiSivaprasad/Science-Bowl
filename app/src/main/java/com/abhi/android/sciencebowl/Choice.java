package com.abhi.android.sciencebowl;

public enum Choice {
    w("W"),
    x("X"),
    y("Y"),
    z("Z");

    private final String choice;

    Choice(String choice) {
        this.choice = choice;
    }

    @Override
    public String toString() {
        return this.choice;
    }
}
