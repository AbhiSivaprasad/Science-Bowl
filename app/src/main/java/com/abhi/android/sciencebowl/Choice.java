package com.abhi.android.sciencebowl;

public enum Choice {
    W("W"),
    X("X"),
    Y("Y"),
    Z("Z");

    private final String choice;

    Choice(String choice) {
        this.choice = choice;
    }

    @Override
    public String toString() {
        return this.choice;
    }
}
