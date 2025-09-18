package org.example;

import org.example.generators.Exponential;
import org.example.generators.LCG;
import org.example.generators.Normal;
import org.example.generators.NumberGenerator;
import org.example.utils.Histogram;
import org.example.utils.Statistics;

import java.text.DecimalFormat;


public class Main {
    static final int SAMPLE_SIZE = 1000;

    private static final double[] LAMBDA_VALUES = {0.5, 1, 1.5, 2.0};
    private static final double[] A = {1, 5, -2}; // середнє значення (математичне очікування).
    private static final double[] Q = {1, 2, 0.5}; // середньоквадратичне відхилення (стандартне відхилення).


    public static void main(String[] args) {
        System.out.println("====================================\n");
        System.out.println("Testing exponential generator");

        for (double lambda : LAMBDA_VALUES) {
            testNumberGenerator(new Exponential(lambda));
        }

        System.out.println("\n" + "=".repeat(50) + "\n");
        System.out.println("Testing normal generator");

        for (double a : A){
            for(double q : Q){
                testNumberGenerator(new Normal(a, q));
            }
        }
        System.out.println("\n" + "=".repeat(50) + "\n");
        System.out.println("Testing LCG generator");

        // Required parameters: a = 5^13, c = 2^31
        long[] lcgA = {1220703125L, 1664525, 1103515245, 16807}; // a = 5^13 = 1220703125
        long[] lcgC = {2147483648L, 1013904223, 12345};          // c = 2^31 = 2147483648
        long seed = 987654321;

        for (long a : lcgA) {
            for(long c : lcgC) {
               testNumberGenerator(new LCG(a, c, seed));
            }
        }

    }

    private static void testNumberGenerator(NumberGenerator generatorType) {
        System.out.println("\n" + "=".repeat(50) + "\n");

        generatorType.generateSamples(SAMPLE_SIZE);

        Statistics observedStats = Statistics.calculateStatistics(generatorType.getGeneratedSamples());
        Histogram hist = Histogram.buildHistogram(generatorType.getGeneratedSamples());
        observedStats.setHistogram(hist);

        generatorType.printStatistic(observedStats);

        generatorType.checkDistribution(observedStats);
    }
}
