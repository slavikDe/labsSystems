package org.example.generators;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.utils.Histogram;
import org.example.utils.Statistics;
import org.example.validation.LCGDistributionValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class LCG extends NumberGenerator {
    List<Double> numbers;

    final long a;
    final long c;
    final long seed;
    final Random random;

    public LCG(long a, long c, long seed) {
        numbers = new ArrayList<>();
        this.a = a;
        this.c = c;
        this.seed = seed;
        random = new Random();
    }

    @Override
    public void generateSamples(int size) {
//        System.out.println("LCG Parameters: a=" + a + ", c=" + c + ", seed=" + seed);
        numbers.clear();

        double z = seed;
        for (int i = 0; i < size; i++) {
            z = (a * z) % c;
            numbers.add( z / c);
        }
    }

    @Override
    public void checkDistribution(Statistics stats) {
        LCGDistributionValidator validator = new LCGDistributionValidator(numbers);
        Histogram histogram = stats.getHistogram();

        double chi2 = validator.calculateChi2(stats);
        int degreesOfFreedom = histogram.getFreedomDegree() - 1;
        double criticalValue = validator.getCriticalValue(degreesOfFreedom);

        printDistributionResult(chi2, degreesOfFreedom, criticalValue, validator.getDistributionName());
    }

    @Override
    public List<Double> getGeneratedSamples() {
        return numbers;
    }

    @Override
    public void printStatistic(Statistics statistics) {
        // Calculate theoretical values based on the LCG properties and generated samples
        double theoreticalMean = calculateTheoreticalMean();
        double theoreticalVariance = calculateTheoreticalVariance();
        double theoreticalStdDev = Math.sqrt(theoreticalVariance);

        printTheoreticalStatistics(statistics, theoreticalMean, theoreticalVariance, theoreticalStdDev);
    }

    private double calculateTheoreticalMean() {
        // For LCG normalized to [0,1), theoretical mean should be around 0.5
        // But we calculate it based on our specific parameters
        return (c - 1.0) / (2.0 * c);
    }

    private double calculateTheoreticalVariance() {
        // For LCG normalized to [0,1), theoretical variance
        // Based on uniform distribution properties for our specific range
        return (c * c - 1.0) / (12.0 * c * c);
    }
}
