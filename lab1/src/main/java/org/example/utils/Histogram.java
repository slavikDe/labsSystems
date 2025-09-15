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
        System.out.println("Інтервал\t\tЧастота\t\tВізуалізація");
        System.out.println("-".repeat(60));

        int maxFreq = Arrays.stream(this.frequencies).max().orElse(1);

        for (int i = 0; i < this.frequencies.length; i++) {
            double left = this.min + i * this.binWidth;
            double right = left + this.binWidth;

            int barLength = (int) (40.0 * this.frequencies[i] / maxFreq);
            String bar = "*".repeat(barLength);

            System.out.printf("[%.3f-%.3f)\t%d\t\t%s%n",
                    left, right, this.frequencies[i], bar);
        }
        System.out.println();
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
