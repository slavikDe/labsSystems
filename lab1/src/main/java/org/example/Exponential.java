package org.example;

import org.example.utils.Histogram;
import org.example.utils.Statistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.log;
import static org.example.Main.SAMPLE_SIZE;

public class Exponential implements NumberGenerator {
    private List<Double> numbers = new ArrayList<>();
    private double lambda;

    @Override
    public void generateSamples(int size, List<Double> params) {
        lambda = params.getFirst();

        for (int i = 0; i < size; i++) {
            double u;
            do {
                u = Math.random();
            } while (u == 0.0);
            numbers.add( -log(u) / lambda);
        }
    }

    @Override
    public void checkDistribution(Histogram histogram) {
        // Емпіричні частоти
        int[] n = histogram.getFrequencies();

        // теоретична ймовірність
        List<Double> E = Arrays.stream(n).map(number -> (1 - log(lambda * n[numbers.indexOf(number)])) * SAMPLE_SIZE).toList();

        // results
        //stats.getHistogram().getFrequencies();

    }

    @Override
    public List<Double> getGeneratedSamples() {
        return numbers;
    }


}
