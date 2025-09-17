package org.example.validation;

import org.example.utils.Statistics;
import org.example.utils.Histogram;

import java.util.List;

public class LCGDistributionValidator implements DistributionValidator {
    private List<Double> data;

    public LCGDistributionValidator(List<Double> numbers) {
        this.data = numbers;
    }

    @Override
    public double calculateChi2(Statistics stats) {
        int n = data.size();
        int numBins = Histogram.getNumberBins();
        int[] observed = new int[numBins];
        double expected = (double) n / numBins;

        // Count samples in each bin
        for (int i = 0; i < data.size(); i++) {
            double sample =  data.get(i);
            // Ensure sample is in (0,1) and map to bin
            if (sample > 0 && sample < 1) {
                int bin = Math.min((int) (sample * numBins), numBins - 1);
                observed[bin]++;
            }
        }

        // Calculate chi-square statistic
        double chiSquare = 0.0;
        for (int i = 0; i < numBins; i++) {
            double diff = observed[i] - expected;
            chiSquare += (diff * diff) / expected;
        }

        return chiSquare;
    }

    @Override
    public double getCriticalValue(int degreesOfFreedom) {
        // Chi-square critical values for α = 0.05
        double[] criticalValues = {
            3.841, 5.991, 7.815, 9.488, 11.070, 12.592, 14.067, 15.507, 16.919, 18.307,
            19.675, 21.026, 22.362, 23.685, 24.996, 26.296, 27.587, 28.869, 30.144, 31.410
        };

        if (degreesOfFreedom >= 1 && degreesOfFreedom <= criticalValues.length) {
            return criticalValues[degreesOfFreedom - 1];
        }

        // Approximation for larger degrees of freedom
        return degreesOfFreedom + Math.sqrt(2 * degreesOfFreedom) * 1.645;
    }

    @Override
    public String getDistributionName() {
        return "Linear Congruential Generator (LCG)";
    }
}
