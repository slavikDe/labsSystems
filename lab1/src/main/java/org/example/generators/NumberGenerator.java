package org.example.generators;

import org.example.utils.Statistics;
import java.text.DecimalFormat;
import java.util.List;


public abstract class NumberGenerator {

    abstract public void generateSamples(int size);

    abstract public void checkDistribution(Statistics statistics);

    abstract public List<Double> getGeneratedSamples();

    abstract public void printStatistic(Statistics statistics) ;

    void printTheoreticalStatistics(Statistics realStats, double theoreticalMean, double theoreticalVariance, double theoreticalStdDev) {
        DecimalFormat df = new DecimalFormat("#.####");
        System.out.println("\nСтатистичні характеристики:");
        System.out.println("---------------------------");

        System.out.printf("Теоретичне середнє: %s\n", df.format(theoreticalMean));
        System.out.printf("Вибіркове середнє: %s\n", df.format(realStats.getMean()));

        System.out.printf("Теоретична дисперсія: %s\n", df.format(theoreticalVariance));
        System.out.printf("Вибіркова дисперсія: %s\n", df.format(realStats.getVariance()));

        System.out.printf("Теоретичне СКВ: %s\n", df.format(theoreticalStdDev));
        System.out.printf("Вибіркове СКВ: %s\n", df.format(realStats.getStddev()));

        System.out.println(realStats.getHistogram().toString());
    }

    void printDistributionResult(double chi2, int degreesOfFreed, double criticalValue, String distributionName) {
        System.out.printf("\nПеревірка відповідності %s закону розподілу:\n", distributionName);
        System.out.printf("Статистика χ²: %.4f\n", chi2);
        System.out.printf("Ступені свободи: %d\n", degreesOfFreed);
        System.out.printf("Критичне значення: %.4f\n", criticalValue);
        System.out.printf("Результат: %s\n", chi2 < criticalValue ?
                "ПРИЙМАЄМО гіпотезу про нормальний розподіл генеральної сукупності" :
                "ВІДХИЛЯЄМО гіпотезу про нормальний розподіл генеральної сукупності");
    }

}
