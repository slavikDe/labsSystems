package org.example;

import org.example.utils.Histogram;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Normal implements NumberGenerator {
    private final double sigma;
    private final double a;

    private List<Double> numbers;

    private final Random random;

    Normal(double sigma, double a) {
        numbers = new ArrayList<>();
        this.sigma = sigma;
        this.a = a;
        random = new Random();
    }

    @Override
    public void generateSamples(int size) {
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
    public void checkDistribution(Histogram histogram) {

//        double chi2 = calculateChi2();
        
    }

//    private double calculateChi2(){
//
//    }

    @Override
    public List<Double> getGeneratedSamples() {
        return numbers;
    }
}
