package com.abhi.android.sciencebowl;
import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;

public enum DifficultyDistribution {
    ZERO(0),
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5);

    private EnumeratedIntegerDistribution eid;

    DifficultyDistribution(int difficulty) {
        double[] difficulties;
        switch (difficulty) {
            case 0:
                difficulties = new double[] {0.5, 0.3, 0.2, 0, 0, 0};
                break;
            case 1:
                difficulties = new double[] {0.2, 0.5, 0.2, 0.1, 0, 0};
                break;
            case 2:
                difficulties = new double[] {0.1, 0.15, 0.5, 0.15, 0.1, 0};
                break;
            case 3:
                difficulties = new double[] {0, 0.1, 0.15, 0.5, 0.15, 0.1};
                break;
            case 4:
                difficulties = new double[] {0, 0, 0.1, 0.2, 0.5, 0.2};
                break;
            case 5:
                difficulties = new double[] {0, 0, 0, 0.2, 0.3, 0.5};
                break;
            default:
                throw new IllegalArgumentException("Difficulty must be an integer between 0 and 5");
        }

        eid = new EnumeratedIntegerDistribution(new int[] {0, 1, 2, 3, 4, 5}, difficulties);
    }

    public int getRandomDifficulty() { return eid.sample(); }
}
