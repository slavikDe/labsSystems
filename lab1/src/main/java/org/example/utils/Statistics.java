package org.example.utils;

import org.example.NumberGenerator;

import java.util.Arrays;
import java.util.List;

public class Statistics {
    final double mean;
    final double variance;
    final double stdDev;

    Statistics(double mean, double variance, double stdDev) {
        this.mean = mean;
        this.variance = variance;
        this.stdDev = stdDev;
    }

    public static Statistics calculateStatistics(NumberGenerator generator) {
        List<Double> generatedSamples = generator.getGeneratedSamples();
        double sum = generatedSamples.stream().reduce(0.0, Double::sum);
        double sumSquares = generatedSamples.stream().reduce(0.0, (val, accumulator) -> val * val + accumulator );

        double mean = sum / generatedSamples.size();
        double variance = (sumSquares / generatedSamples.size()) - (mean * mean);

        double stdDev = Math.sqrt(variance);

        return new Statistics(mean, variance, stdDev);
    }

    @Override
    public String toString() {
        return String.format(
                "Statistics { mean = %.4f, variance = %.4f, stdDev = %.4f }",
                mean, variance, stdDev
        );
    }

}
