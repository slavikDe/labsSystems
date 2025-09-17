package org.example.validation;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.example.utils.Histogram;
import org.example.utils.Statistics;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExponentialDistributionValidator implements DistributionValidator {
    static final double ALPHA = 0.05;
    final double lambda;
    final List<Double> data;

    public ExponentialDistributionValidator(double lambda, List<Double> data) {
        this.lambda = lambda;
        this.data = data;
    }

    @Override
    public double calculateChi2(Statistics stats) {
        Histogram histogram = stats.getHistogram();
        double[] numbersArray = data.stream().mapToDouble(Double::doubleValue).toArray();
        double[] edges = histogram.getBinEdges(numbersArray, 20);
        int[] frequencies = histogram.getFrequencies();

        double chi2 = 0;
        int i = 0;
        while (i < frequencies.length) {
            double p = Math.exp(-lambda * edges[i]) - Math.exp(-lambda * edges[i + 1]);
            double Ei = data.size() * p;
            int Oi = frequencies[i];

            while (Ei < 5 && i < frequencies.length - 1) {
                i++;
                double pNext = Math.exp(-lambda * edges[i]) - Math.exp(-lambda * edges[i + 1]);
                Ei += data.size() * pNext;
                Oi += frequencies[i];
            }

            double diff = Oi - Ei;
            chi2 += diff * diff / Ei;

            i++;
        }

        return chi2;
    }

    @Override
    public double getCriticalValue(int degreesOfFreedom) {
        if (degreesOfFreedom <= 0) return Double.MAX_VALUE;
        ChiSquaredDistribution chiSquared = new ChiSquaredDistribution(degreesOfFreedom);
        return chiSquared.inverseCumulativeProbability(1 - ALPHA);
    }

    @Override
    public String getDistributionName() {
        return "експоненціальний";
    }
}