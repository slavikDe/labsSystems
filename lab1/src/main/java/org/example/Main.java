package org.example;

import org.example.generators.Exponential;
import org.example.generators.LCG;
import org.example.generators.Normal;
import org.example.generators.NumberGenerator;
import org.example.utils.Histogram;
import org.example.utils.QuickSort;
import org.example.utils.Statistics;

import java.net.DatagramPacket;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


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

        // testing time histogram
//        int size = 1_000_000;
//        int iterations = 50;
//        testTimeSortingHistogram(new Exponential(1), size, iterations);
//        testTimeSortingHistogram(new Normal(5, 2), size, iterations);
//        testTimeSortingHistogram(new LCG(1220703125L, 2147483648L, 987654321L), size, iterations);
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

    static void testTimeSorting(Class<?> clazz, int size) {
        try {
            NumberGenerator generatorType = (NumberGenerator) clazz.getDeclaredConstructor(double.class).newInstance(1.0);

            generatorType.generateSamples(size);
            double[] samples = generatorType.getGeneratedSamples().stream().mapToDouble(Double::doubleValue).toArray();

            QuickSort quickSort = new QuickSort();
            long startTime = System.nanoTime();
            quickSort.sort(samples);
            long endTime = System.nanoTime();
            System.out.println("Sorting time: " +  (endTime - startTime)/ 1000000 + " ms");

        } catch (Exception e) {
            System.err.println("Error in testTimeSorting: " + e.getMessage());
        }
    }

    static void testTimeSortingHistogram(NumberGenerator generatorType, int size, int iterations) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("TESTING SORTING TIME HISTOGRAM");
        System.out.println("Generator: " + generatorType.getClass().getSimpleName());
        System.out.println("Array size: " + size + ", Iterations: " + iterations);
        System.out.println("=".repeat(50) + "\n");

        List<Double> sortingTimes = new ArrayList<>();
        QuickSort quickSort = new QuickSort();

        for (int i = 0; i < iterations; i++) {
            generatorType.generateSamples(size);
            double[] samples = generatorType.getGeneratedSamples().stream().mapToDouble(Double::doubleValue).toArray();

            long startTime = System.nanoTime();
            quickSort.sort(samples);
            long endTime = System.nanoTime();

            double timeMs = (endTime - startTime) / 1000000.0;
            sortingTimes.add(timeMs);

            if ((i + 1) % 10 == 0) {
                System.out.printf("Completed %d/%d iterations\n", i + 1, iterations);
            }
        }

        Statistics timeStats = Statistics.calculateStatistics(sortingTimes);
        Histogram timeHistogram = Histogram.buildHistogram(sortingTimes);

        double minTime = sortingTimes.stream().mapToDouble(Double::doubleValue).min().orElse(0);
        double maxTime = sortingTimes.stream().mapToDouble(Double::doubleValue).max().orElse(0);

        System.out.println("\nSORTING TIME STATISTICS:");
        System.out.println("---------------------------");
        System.out.printf("Average sorting time: %.3f ms\n", timeStats.getMean());
        System.out.printf("Min sorting time: %.3f ms\n", minTime);
        System.out.printf("Max sorting time: %.3f ms\n", maxTime);
        System.out.printf("Standard deviation: %.3f ms\n", timeStats.getStddev());
        System.out.printf("Variance: %.3f ms²\n", timeStats.getVariance());

        System.out.println("\nSORTING TIME HISTOGRAM:");
        System.out.println(timeHistogram);


    }

}
