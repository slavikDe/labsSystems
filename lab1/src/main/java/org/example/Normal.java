package org.example;

import org.example.utils.Histogram;

import java.util.ArrayList;
import java.util.List;

public class Normal implements NumberGenerator {
    private List<Double> numbers;
    private double sigma;
    private double a;

    Normal() {
        numbers = new ArrayList<>();
    }

    @Override
    public void generateSamples(int size, List<Double> params) {
        this.sigma = params.get(0);
        this.a = params.get(1);

        for(int i = 0; i < size; i++){
            numbers.add(generate());
        }
    }

    private double generate(){
        double sum = 0;
        for(int j = 0; j < 12; j++){
            sum += Math.random();
        }
        double u = sum - 6;
        return sigma * u + a;
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
