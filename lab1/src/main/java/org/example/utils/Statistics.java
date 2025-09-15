package org.example.utils;

import org.example.NumberGenerator;

import java.util.List;

import static org.example.CompleteRandomAnalysis.StatisticalAnalyzer.calculateVariance;

public class Statistics {
//    final double mean;
//    final double variance;
//    final double stdDev;

//    Statistics(double mean, double variance, double stdDev) {
//        this.mean = mean;
//        this.variance = variance;
//        this.stdDev = stdDev;
//    }
//
//    public static Statistics calculateStatistics(NumberGenerator generator) {
//        List<Double> generatedSamples = generator.getGeneratedSamples();
//        double[] sample = generatedSamples.stream().mapToDouble(Double::doubleValue).toArray();
//        double mean = calculateMean(sample);
//
//
//        double sum = generatedSamples.stream().reduce(0.0, Double::sum);
//        double sumSquares = generatedSamples.stream().reduce(0.0, (val, accumulator) -> val * val + accumulator );
//
//        double mean = sum / generatedSamples.size();
//        double variance = (sumSquares / generatedSamples.size()) - (mean * mean);
//
//        double stdDev = Math.sqrt(variance);
//
//        return new Statistics(mean, variance, stdDev);
//    }

//    @Override
//    public String toString() {
//        return String.format(
//                "Statistics { mean = %.4f, variance = %.4f, stdDev = %.4f }",
//                mean, variance, stdDev
//        );
//    }

    public static double calculateMean(double[] sample) {
        double sum = 0;
        for (double value : sample) {
            sum += value;
        }
        return sum / sample.length;
    }

    public static double calculateVariance(double[] sample) {
        double mean = calculateMean(sample);
        double sumSquaredDiff = 0;
            for (double value : sample) {
            sumSquaredDiff += Math.pow(value - mean, 2);
        }
            return sumSquaredDiff / (sample.length - 1);
    }

    public static double calculateStandardDeviation(double[] sample) {
    return Math.sqrt(calculateVariance(sample));
}


}
