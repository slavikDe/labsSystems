package org.example;

import org.example.utils.Histogram;
import org.example.utils.Statistics;

import java.util.List;

public interface NumberGenerator {

    void generateSamples(int size, List<Double> params);

    void checkDistribution(Histogram histogram);

    List<Double> getGeneratedSamples();
}
