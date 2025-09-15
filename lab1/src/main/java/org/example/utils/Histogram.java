package org.example.utils;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class Histogram {

    final int[] frequencies;
    final double[] binCenters;
    final double binWidth;
    final double min;
    final double max;

    Histogram(int[] frequencies, double[] binCenters, double binWidth, double min, double max) {
        this.frequencies = frequencies;
        this.binCenters = binCenters;
        this.binWidth = binWidth;
        this.min = min;
        this.max = max;
    }

    public void drawHistogram() {
        System.out.println("ГІСТОГРАМА ЧАСТОТ:");
        int maxFreq = Arrays.stream(frequencies).max().orElse(1);
        int scale = Math.max(1, maxFreq / 50);

        for (int i = 0; i < frequencies.length; i++) {
            double left = this.min + i * this.binWidth;
            double right = left + this.binWidth;
            System.out.printf("Bin %2d [%.3f : %.3f]: \t",  i + 1, left, right);
            int stars = frequencies[i] / scale;
            for (int j = 0; j < stars; j++) {
                System.out.print("*");
            }
            System.out.printf(" (%d)\n", frequencies[i]);
        }
    }

    public static Histogram buildHistogram(List<Double> generatedSamples) {
        final int NUMBER_BINS = 20;

        double min = generatedSamples.stream().mapToDouble(v -> v).min().orElseThrow(NoSuchElementException::new);
        double max = generatedSamples.stream().mapToDouble(v -> v).max().orElseThrow(NoSuchElementException::new);

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

    public int[] getFrequencies() {return this.frequencies;}

    public int getFreedomDegree() {
        return frequencies.length - 1; // bins - m - 1, тут m=0
    }

    public double[] getBinEdges() {
        double[] edges = new double[frequencies.length + 1];
        for (int i = 0; i <= frequencies.length; i++) {
            edges[i] = min + i * binWidth;
        }
        return edges;
    }
}
