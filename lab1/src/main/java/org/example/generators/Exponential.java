package org.example.generators;

import org.example.utils.Histogram;
import org.example.utils.Statistics;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.validation.ExponentialDistributionValidator;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.log;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Exponential extends NumberGenerator {
    final double lambda;

    List<Double> numbers;

    public Exponential(double lambda) {
        this.lambda = lambda;
        numbers = new ArrayList<>();
    }

    @Override
    public void generateSamples(int size) {
//        System.out.println("Exponential Parameters: λ=" + lambda);
        numbers.clear();
        for (int i = 0; i < size; i++) {
            double u;
            do {
                u = Math.random();
            } while (u == 0.0);
            numbers.add(-log(u) / lambda);
        }
    }

    @Override
    public void checkDistribution(Statistics stats) {
        ExponentialDistributionValidator validator = new ExponentialDistributionValidator(lambda, numbers);
        Histogram histogram = stats.getHistogram();

        int k = histogram.getFrequencies().length;
        int degreesOfFreed = k - 1;

        double chi2 = validator.calculateChi2(stats);
        double chi2_critical = validator.getCriticalValue(degreesOfFreed);

        printDistributionResult(chi2, degreesOfFreed, chi2_critical, validator.getDistributionName());
    }

    @Override
    public List<Double> getGeneratedSamples() {
        return new ArrayList<>(numbers);
    }


    public void printStatistic(Statistics statistics) {
        double theoreticalMean = 1 / lambda;
        double theoreticalVariance = 1 / (lambda * lambda);
        double theoreticalStdDev = 1 / lambda;

        printTheoreticalStatistics(statistics, theoreticalMean, theoreticalVariance, theoreticalStdDev);
    }

}
