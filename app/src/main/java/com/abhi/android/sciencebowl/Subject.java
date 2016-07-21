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

    public static final int SIZE = Subject.values().length;
    public static final Subject[] SUBJECTS = Subject.values();

    private final String subject;

    Subject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return this.subject;
    }
}
