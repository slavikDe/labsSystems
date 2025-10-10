package org.example.generators;

import org.example.utils.Histogram;
import org.example.utils.Statistics;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.validation.NormalDistributionValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Normal extends NumberGenerator {
    final double sigma;
    final double a;

    List<Double> numbers;

    final Random random;

    public Normal(double a, double sigma) {
        numbers = new ArrayList<>();
        this.sigma = sigma;
        this.a = a;
        random = new Random();
    }

    @Override
    public void generateSamples(int size) {
//        System.out.println("Normal Parameters: μ=" + a + ", σ=" + sigma);
        numbers.clear();
        for(int i = 0; i < size; i++){
            numbers.add(generate());
        }
    }

    private double generate(){
        double sum = 0;
        for(int j = 0; j < 12; j++){
            sum += random.nextDouble();
        }
        double mu = sum - 6;
        return sigma * mu + a;
    }

    @Override
    public void checkDistribution(Statistics stats) {
        NormalDistributionValidator validator = new NormalDistributionValidator(numbers);
        Histogram histogram = stats.getHistogram();

        double chi2 = validator.calculateChi2(stats);
        int degreesOfFreed = histogram.getFreedomDegree() - 1;
        double criticalValue = validator.getCriticalValue(degreesOfFreed);

        printDistributionResult(chi2, degreesOfFreed, criticalValue, validator.getDistributionName());
    }

    @Override
    public List<Double> getGeneratedSamples() {
        return new ArrayList<>(numbers);
    }

    public void printStatistic(Statistics statistics) {
        double theoreticalMean = a;
        double theoreticalVariance = sigma * sigma;
        double theoreticalStdDev = sigma;

        printTheoreticalStatistics(statistics, theoreticalMean, theoreticalVariance, theoreticalStdDev);
    }
}
