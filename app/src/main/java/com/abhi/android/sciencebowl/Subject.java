package com.abhi.android.sciencebowl;

public enum Subject {
    // string literals below are inconsequential, just need to be unique.
    EARTH("Earth"),
    BIOLOGY("Biology"),
    PHYSICS("Physics"),
    CHEMISTRY("Chemistry"),
    ENERGY("Energy"),
    MATH("Math"),
    ASTRO("Astronomy");

    private final String subject;

    Subject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return this.subject;
    }
}
