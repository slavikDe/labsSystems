package org.example;

import com.sun.jdi.ClassNotLoadedException;
import org.example.utils.Histogram;
import org.example.utils.Statistics;

import java.util.List;


public class Main {
    static final int SAMPLE_SIZE = 1000;

    private static final double[] LAMBDA_VALUES = {0.5, 1, 1.5, 2.0};
    private static final double[] A = {0,5,10 }; // середнє значення (математичне очікування).
    private static final double[] Q = {1,2,3}; // середньоквадратичне відхилення (стандартне відхилення).


    public static void main(String[] args) {
        System.out.println("====================================\n");
        System.out.println("Testing exponential generator");

        for (double lambda : LAMBDA_VALUES) {
            testNumberGenerator(new Exponential(), List.of(lambda));
        }

        System.out.println("\n" + "=".repeat(50) + "\n");
        System.out.println("Testing normal generator");

        for (double a : A){
            for(double q : Q){
                testNumberGenerator(new Normal(), List.of(a,q));
            }
        }
        System.out.println("\n" + "=".repeat(50) + "\n");
        System.out.println("Testing ... generator");

    }

    private static void testNumberGenerator(NumberGenerator generatorType, List<Double> lambda) {
        System.out.println("\n" + "=".repeat(50) + "\n");

        generatorType.generateSamples(SAMPLE_SIZE, lambda);

        Statistics stats = Statistics.calculateStatistics(generatorType);
        System.out.println(stats);

        Histogram hist = Histogram.buildHistogram(generatorType.getGeneratedSamples());
        hist.drawHistogram();

        generatorType.checkDistribution(hist);
    }
}
