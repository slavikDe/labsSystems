package org.example.validation;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.example.utils.Histogram;
import org.example.utils.Statistics;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class NormalDistributionValidator implements DistributionValidator {
    static final double ALPHA = 0.05;
    final List<Double> data;

    public NormalDistributionValidator(List<Double> data) {
        this.data = data;
    }

    @Override
    public double calculateChi2(Statistics stats) {
        double mean = stats.getMean();
        double stdDev = stats.getStddev();
        Histogram histogram = stats.getHistogram();

        double[] numbersArray = data.stream().mapToDouble(Double::doubleValue).toArray();
        double[] binEdges = histogram.getBinEdges(numbersArray, 20);
        int[] frequencies = histogram.getFrequencies();
        int numBins = binEdges.length;
        double[] expectedFreq = new double[numBins];

        for(int i = 0; i < numBins - 1; i++){
            double binCenter = (binEdges[i] + binEdges[i + 1]) / 2;
            double binWidth = histogram.getBinWidth();
            expectedFreq[i] = data.size() * normalPDF(binCenter, mean, stdDev) * binWidth;
        }

        double chiSquare = 0;

        for (int i = 0; i < numBins; i++) {
            if (expectedFreq[i] >= 5) {
                chiSquare += Math.pow(frequencies[i] - expectedFreq[i], 2) / expectedFreq[i];
            }
        }

        return chiSquare;
    }

    @Override
    public double getCriticalValue(int degreesOfFreedom) {
        if (degreesOfFreedom <= 0) return Double.MAX_VALUE;
        ChiSquaredDistribution chiSquared = new ChiSquaredDistribution(degreesOfFreedom);
        return chiSquared.inverseCumulativeProbability(1 - ALPHA);
    }

    @Override
    public String getDistributionName() {
        return "нормальний";
    }

    private double normalPDF(double x, double mean, double stdDev){
        return (1 / (stdDev * Math.sqrt(2 * Math.PI))) *
                Math.exp(-0.5 * Math.pow((x - mean) / stdDev, 2));
    }
}