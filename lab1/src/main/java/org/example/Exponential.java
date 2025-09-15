package org.example;

import org.example.utils.Histogram;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;


import static java.lang.Math.log;
import static org.example.Main.SAMPLE_SIZE;


public class Exponential implements NumberGenerator {
    private final double ALPHA = 0.05;

    private final List<Double> numbers = new ArrayList<>();
    private double lambda;

    @Override
    public void generateSamples(int size, List<Double> params) {
        lambda = params.getFirst();

        for (int i = 0; i < size; i++) {
            double u;
            do {
                u = Math.random();
            } while (u == 0.0);
            numbers.add( -log(u) / lambda);
        }
    }

    @Override
    public void checkDistribution(Histogram histogram) {
        int k = histogram.getFrequencies().length;
        int df = k - 1;

        double chi2 = calculateChi2(histogram);
        double chi2_critical = chi2CriticalValue(ALPHA, df);

        System.out.printf("Значення хі-квадрат: %.4f%n", chi2);
        System.out.printf("Ступені свободи: %d%n", df);
        System.out.printf("Критичне значення (α=%.2f): %.4f%n", ALPHA, chi2_critical);

        if (chi2 < chi2_critical) {
            System.out.println("ВИСНОВОК: Гіпотеза про експоненційний розподіл НЕ ВІДХИЛЯЄТЬСЯ");
            System.out.printf("Вибірка узгоджується з експоненціальним законом розподілу з параметром lambda: %.2f.%n", lambda);
        } else {
            System.out.println("ВИСНОВОК: Гіпотеза про експоненційний розподіл ВІДХИЛЯЄТЬСЯ");
        }
    }

    public double chi2CriticalValue(double alpha, int df) {
        ChiSquaredDistribution chi2 = new ChiSquaredDistribution(df);
        return chi2.inverseCumulativeProbability(1 - alpha);
    }

    private double calculateChi2(Histogram histogram) {
        double[] edges = histogram.getBinEdges();
        int[] frequencies = histogram.getFrequencies();

        double chi2 = 0;
        int i = 0;
        while (i < frequencies.length) {
            double p = Math.exp(-lambda * edges[i]) - Math.exp(-lambda * edges[i + 1]);
            double Ei = SAMPLE_SIZE * p;
            int Oi = frequencies[i];

            while (Ei < 5 && i < frequencies.length - 1) {
                i++;
                double pNext = Math.exp(-lambda * edges[i]) - Math.exp(-lambda * edges[i + 1]);
                Ei += SAMPLE_SIZE * pNext;
                Oi += frequencies[i];
            }

            double diff = Oi - Ei;
            chi2 += diff * diff / Ei;

            i++;
        }

        return chi2;
    }

    @Override
    public List<Double> getGeneratedSamples() {
        return numbers;
    }


}
