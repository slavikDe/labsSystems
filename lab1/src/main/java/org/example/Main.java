package org.example;

import com.sun.jdi.ClassNotLoadedException;
import org.example.utils.Histogram;
import org.example.utils.Statistics;

import java.util.List;


public class Main {
    static final int SAMPLE_SIZE = 1000;

    private static final double[] LAMBDA_VALUES = {0.5, 1, 1.5, 2.0};
    private static final double[] A = {0, 5, -2}; // середнє значення (математичне очікування).
    private static final double[] Q = {1, 2, 0.5}; // середньоквадратичне відхилення (стандартне відхилення).


    public static void main(String[] args) {
        System.out.println("====================================\n");
        System.out.println("Testing exponential generator");

//        for (double lambda : LAMBDA_VALUES) {
//            testNumberGenerator(new Exponential(lambda));
//        }

        System.out.println("\n" + "=".repeat(50) + "\n");
        System.out.println("Testing normal generator");

        for (double a : A){
            for(double q : Q){
                testNumberGenerator(new Normal(a, q));
            }
        }
        System.out.println("\n" + "=".repeat(50) + "\n");
        System.out.println("Testing ... generator");

    }

    private static void testNumberGenerator(NumberGenerator generatorType) {
        System.out.println("\n" + "=".repeat(50) + "\n");

        generatorType.generateSamples(SAMPLE_SIZE);

        double mean = Statistics.calculateMean(generatorType.getGeneratedSamples().stream().mapToDouble(x -> x).toArray());
        double variance = Statistics.calculateVariance(generatorType.getGeneratedSamples().stream().mapToDouble(x -> x).toArray());
        double std = Statistics.calculateStandardDeviation(generatorType.getGeneratedSamples().stream().mapToDouble(x -> x).toArray());
        System.out.println(stats);

        Histogram hist = Histogram.buildHistogram(generatorType.getGeneratedSamples());
        hist.drawHistogram();

        generatorType.checkDistribution(hist);
    }
}
