package org.example.utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Statistics {
    final double mean;
    final double variance;
    final double stddev;
    Histogram histogram;

    public Statistics(double mean, double variance, double stddev) {
        this.mean = mean;
        this.variance = variance;
        this.stddev = stddev;
    }

    private static double calculateMean(List<Double> sample) {
        double sum = 0;
        for (double value : sample) {
            sum += value;
        }
        return sum / sample.size();
    }

    private static double calculateVariance(List<Double> sample) {
        double mean = calculateMean(sample);
        double sumSquaredDiff = 0;
        for (double value : sample) {
            sumSquaredDiff += Math.pow(value - mean, 2);
        }
        return sumSquaredDiff / (sample.size() - 1);
    }

    private static double calculateStandardDeviation(List<Double> sample) {
        return Math.sqrt(calculateVariance(sample));
    }

    public static Statistics calculateStatistics(List<Double> generatedSamples) {
        double mean = calculateMean(generatedSamples);
        double variance = calculateVariance(generatedSamples);
        double stddev = calculateStandardDeviation(generatedSamples);

        return new Statistics(mean, variance, stddev);
    }

}
