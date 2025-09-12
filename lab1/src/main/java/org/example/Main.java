package org.example;

import org.example.utils.Histogram;
import org.example.utils.Statistics;

import java.util.List;


public class Main {
    static final int SAMPLE_SIZE = 1000;

    private static final double[] LAMBDA_VALUES = {0.5};
    private static final double[] A = {0,5,10 }; // середнє значення (математичне очікування).
    private static final double[] Q = {1,2,3}; // середньоквадратичне відхилення (стандартне відхилення).


    public static void main(String[] args) {
        System.out.println("====================================\n");

        System.out.println("Testing exponential generator");
        for (double lambda : LAMBDA_VALUES) {
            Exponential ex = new Exponential();
            ex.generateSamples(SAMPLE_SIZE, List.of(lambda));

            Statistics stats = Statistics.calculateStatistics(ex);
            System.out.println(stats);

            Histogram hist = Histogram.buildHistogram(ex.getGeneratedSamples());
            hist.drawHistogram();

            ex.testExponentialGenerator(hist);


//            double chi = chiSquared();
        }
        System.out.println("\n" + "=".repeat(50) + "\n");

        System.out.println("Testing normal generator");

        List<double[]> params = List.of(A, Q);

//        for (double a : A){
//            for(double q : Q){
//                double[] generatedSamples = Exponential.generateExponentialSamples(SAMPLE_SIZE, );
//                Normal.testNormal(a, q, SAMPLE_SIZE);
//            }
//        }
//        System.out.println("\n" + "=".repeat(50) + "\n");
//
//        System.out.println("Test LCG");
//        System.out.println("\n" + "=".repeat(50) + "\n");
    }

//    public static void processGenerator(List<double[]> params, Function<Integer, InputParams> generator){
//        List<InputParams> input = generateParametersPairs(params);
//        input.stream().forEach(param -> {
//            generator.apply(param);
//
//        });
//    }

    private double chiSquared() {
        return 0.0;
    };


}
