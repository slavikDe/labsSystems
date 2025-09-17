package org.example.utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Histogram {
    private static final int NUMBER_BINS = 20;
    int[] frequencies;
    double[] binCenters;

    double binWidth;
    double min;
    double max;

    public Histogram(int[] frequencies, double[] binCenters, double binWidth, double min, double max) {
        this.frequencies = frequencies;
        this.binCenters = binCenters;
        this.binWidth = binWidth;
        this.min = min;
        this.max = max;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ГІСТОГРАМА ЧАСТОТ:\n");

        int maxFreq = Arrays.stream(frequencies).max().orElse(1);
        int scale = Math.max(1, maxFreq / 50);

        for (int i = 0; i < frequencies.length; i++) {
            double left = this.min + i * this.binWidth;
            double right = left + this.binWidth;
            sb.append(String.format("Bin %2d [%.3f : %.3f]: \t", i + 1, left, right));

            int stars = frequencies[i] / scale;
            sb.append("*".repeat(stars));
            sb.append(String.format(" (%d)%n", frequencies[i]));
        }

        return sb.toString();
    }

    public static Histogram buildHistogram(List<Double> generatedSamples) {
        double min = generatedSamples.stream().mapToDouble(Double::doubleValue).min().orElseThrow(NoSuchElementException::new);
        double max = generatedSamples.stream().mapToDouble(Double::doubleValue).max().orElseThrow(NoSuchElementException::new);

        double binWidth = (max - min) / NUMBER_BINS;
        int[] frequencies = new int[NUMBER_BINS];
        double[] binCenters = new double[NUMBER_BINS];

        for (int i = 0; i < NUMBER_BINS; i++) {
            binCenters[i] = min + (i + 0.5) * binWidth;
        }

        for (double num : generatedSamples) {
            int binIndex = (int) Math.min((num - min) / binWidth, NUMBER_BINS - 1);
            if (binIndex >= 0) {
                frequencies[binIndex]++;
            }
        }

        return new  Histogram(frequencies, binCenters, binWidth,  min, max);
    }

    public int getFreedomDegree() {
        return frequencies.length - 1; // bins - m - 1, тут m=0
    }

    public double[] getBinEdges(double[] data, int numBins) {
        double min = Arrays.stream(data).min().orElse(0);
        double max = Arrays.stream(data).max().orElse(0);
        double range = max - min;
        double binWidth = range / numBins;

        double[] binEdges = new double[numBins + 1];
        for (int i = 0; i <= numBins; i++) {
            binEdges[i] = min + i * binWidth;
        }
        return binEdges;
    }

    public static int getNumberBins() {
        return NUMBER_BINS;
    }

}
