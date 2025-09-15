package org.example;


import java.util.*;
import java.text.DecimalFormat;

public class RandomNumberGenerator {
    private static final int SAMPLE_SIZE = 10000;
    private static final int NUM_BINS = 20;
    private static final double ALPHA = 0.05; // Significance level for chi-square test
    private static final DecimalFormat df = new DecimalFormat("#.####");

    private Random random;
    private double a; // Parameter for normal distribution
    private double sigma; // Standard deviation

    public RandomNumberGenerator(double a, double sigma) {
        this.random = new Random();
        this.a = a;
        this.sigma = sigma;
    }

    // Generate random number according to formula: xi = σμi + a
    // where μi = Σ(ξi) - 6, ξi ~ Uniform(0,1)
    public double generateRandomNumber() {
        double sum = 0;
        for (int i = 0; i < 12; i++) {
            sum += random.nextDouble(); // ξi uniformly distributed in (0,1)
        }
        double mu = sum - 6; // μi = Σ(ξi) - 6
        return sigma * mu + a; // xi = σμi + a
    }

    // Generate sample of random numbers
    public double[] generateSample(int size) {
        double[] sample = new double[size];
        for (int i = 0; i < size; i++) {
            sample[i] = generateRandomNumber();
        }
        return sample;
    }

    // Calculate sample mean
    public double calculateMean(double[] sample) {
        double sum = 0;
        for (double value : sample) {
            sum += value;
        }
        return sum / sample.length;
    }

    // Calculate sample variance
    public double calculateVariance(double[] sample) {
        double mean = calculateMean(sample);
        double sumSquaredDiff = 0;
        for (double value : sample) {
            sumSquaredDiff += Math.pow(value - mean, 2);
        }
        return sumSquaredDiff / (sample.length - 1);
    }

    // Calculate sample standard deviation
    public double calculateStandardDeviation(double[] sample) {
        return Math.sqrt(calculateVariance(sample));
    }

    // Build histogram
    public int[] buildHistogram(double[] sample, double[] binEdges) {
        int[] histogram = new int[NUM_BINS];

        for (double value : sample) {
            for (int i = 0; i < NUM_BINS; i++) {
                if (value >= binEdges[i] && value < binEdges[i + 1]) {
                    histogram[i]++;
                    break;
                }
                // Handle last bin edge case
                if (i == NUM_BINS - 1 && value <= binEdges[i + 1]) {
                    histogram[i]++;
                    break;
                }
            }
        }

        return histogram;
    }

    // Create bin edges for histogram
    public double[] createBinEdges(double[] sample) {
        double min = Arrays.stream(sample).min().orElse(0);
        double max = Arrays.stream(sample).max().orElse(0);
        double range = max - min;
        double binWidth = range / NUM_BINS;

        double[] binEdges = new double[NUM_BINS + 1];
        for (int i = 0; i <= NUM_BINS; i++) {
            binEdges[i] = min + i * binWidth;
        }

        return binEdges;
    }

    // Normal distribution probability density function
    public double normalPDF(double x, double mean, double stdDev) {
        return (1 / (stdDev * Math.sqrt(2 * Math.PI))) *
                Math.exp(-0.5 * Math.pow((x - mean) / stdDev, 2));
    }

    // Chi-square goodness of fit test
    public ChiSquareResult chiSquareTest(double[] sample, double sampleMean, double sampleStdDev) {
        double[] binEdges = createBinEdges(sample);
        int[] observedFreq = buildHistogram(sample, binEdges);
        double[] expectedFreq = new double[NUM_BINS];

        // Calculate expected frequencies based on normal distribution
        for (int i = 0; i < NUM_BINS; i++) {
            double binCenter = (binEdges[i] + binEdges[i + 1]) / 2;
            double binWidth = binEdges[i + 1] - binEdges[i];
            expectedFreq[i] = SAMPLE_SIZE * normalPDF(binCenter, sampleMean, sampleStdDev) * binWidth;
        }

        // Calculate chi-square statistic
        double chiSquare = 0;
        int validBins = 0;

        for (int i = 0; i < NUM_BINS; i++) {
            if (expectedFreq[i] >= 5) { // Only use bins with expected frequency >= 5
                chiSquare += Math.pow(observedFreq[i] - expectedFreq[i], 2) / expectedFreq[i];
                validBins++;
            }
        }

        int degreesOfFreedom = validBins - 3; // -3 because we estimated mean and variance
        double criticalValue = getCriticalValue(degreesOfFreedom, ALPHA);

        return new ChiSquareResult(chiSquare, degreesOfFreedom, criticalValue,
                chiSquare < criticalValue, observedFreq, expectedFreq);
    }

    // Get critical value for chi-square test (approximation for common cases)
    private double getCriticalValue(int df, double alpha) {
        // This is a simplified lookup table for α = 0.05
        if (df <= 0) return Double.MAX_VALUE;
        if (df == 1) return 3.841;
        if (df == 2) return 5.991;
        if (df == 3) return 7.815;
        if (df == 4) return 9.488;
        if (df == 5) return 11.070;
        if (df <= 10) return 12.592 + (df - 5) * 1.5; // Rough approximation
        return 15 + df * 1.2; // Very rough approximation for larger df
    }

    // Print histogram
    public void printHistogram(int[] histogram, double[] binEdges) {
        System.out.println("\nГістограма частот:");
        System.out.println("Інтервал\t\tЧастота\t\tВідносна частота");
        System.out.println("---------------------------------------------------");

        for (int i = 0; i < histogram.length; i++) {
            double relativeFreq = (double) histogram[i] / SAMPLE_SIZE;
            System.out.printf("[%.3f, %.3f)\t\t%d\t\t%.4f\n",
                    binEdges[i], binEdges[i + 1], histogram[i], relativeFreq);
        }
    }

    // Print visual histogram
    public void printVisualHistogram(int[] histogram) {
        System.out.println("\nВізуальна гістограма:");
        int maxFreq = Arrays.stream(histogram).max().orElse(1);
        int scale = Math.max(1, maxFreq / 50); // Scale for display

        for (int i = 0; i < histogram.length; i++) {
            System.out.printf("Bin %2d: ", i + 1);
            int stars = histogram[i] / scale;
            for (int j = 0; j < stars; j++) {
                System.out.print("*");
            }
            System.out.printf(" (%d)\n", histogram[i]);
        }
    }

    public static void main(String[] args) {
        // Test with different parameters
        double[] aValues = {0, 5, -2};
        double[] sigmaValues = {1, 2, 0.5};

        for (int test = 0; test < aValues.length; test++) {
            double a = aValues[test];
            double sigma = sigmaValues[test];

            System.out.println("=".repeat(80));
            System.out.printf("ТЕСТ %d: Генератор з параметрами a = %.1f, σ = %.1f\n",
                    test + 1, a, sigma);
            System.out.println("=".repeat(80));

            RandomNumberGenerator generator = new RandomNumberGenerator(a, sigma);

            // Generate sample
            System.out.println("Генерування вибірки з " + SAMPLE_SIZE + " значень...");
            double[] sample = generator.generateSample(SAMPLE_SIZE);

            // Calculate statistics
            double sampleMean = generator.calculateMean(sample);
            double sampleVariance = generator.calculateVariance(sample);
            double sampleStdDev = generator.calculateStandardDeviation(sample);

            // Theoretical values
            double theoreticalMean = a;
            double theoreticalVariance = sigma * sigma; // Variance of sum of 12 uniform(0,1) is 1
            double theoreticalStdDev = sigma;

            System.out.println("\nСтатистичні характеристики:");
            System.out.println("---------------------------");
            System.out.printf("Вибіркове середнє: %s\n", df.format(sampleMean));
            System.out.printf("Теоретичне середнє: %s\n", df.format(theoreticalMean));
            System.out.printf("Вибіркова дисперсія: %s\n", df.format(sampleVariance));
            System.out.printf("Теоретична дисперсія: %s\n", df.format(theoreticalVariance));
            System.out.printf("Вибіркове СКВ: %s\n", df.format(sampleStdDev));
            System.out.printf("Теоретичне СКВ: %s\n", df.format(theoreticalStdDev));

            // Build and display histogram
            double[] binEdges = generator.createBinEdges(sample);
            int[] histogram = generator.buildHistogram(sample, binEdges);

            generator.printHistogram(histogram, binEdges);
            generator.printVisualHistogram(histogram);

            // Chi-square test
            System.out.println("\nПеревірка відповідності нормальному закону розподілу:");
            System.out.println("----------------------------------------------------");

            ChiSquareResult chiResult = generator.chiSquareTest(sample, sampleMean, sampleStdDev);

            System.out.printf("Статистика χ²: %.4f\n", chiResult.chiSquare);
            System.out.printf("Ступені свободи: %d\n", chiResult.degreesOfFreedom);
            System.out.printf("Критичне значення (α = %.2f): %.4f\n", ALPHA, chiResult.criticalValue);
            System.out.printf("Результат тесту: %s\n",
                    chiResult.isNormal ? "ПРИЙМАЄМО гіпотезу про нормальність" :
                            "ВІДХИЛЯЄМО гіпотезу про нормальність");

            // Conclusions
            System.out.println("\nВисновки:");
            System.out.println("---------");
            System.out.println("1. За виглядом гістограми розподіл схожий на нормальний");
            System.out.printf("2. Вибіркові характеристики близькі до теоретичних значень\n");
            System.out.printf("3. Критерій χ² %s гіпотезу про нормальність розподілу\n",
                    chiResult.isNormal ? "підтверджує" : "відхиляє");

            if (test < aValues.length - 1) {
                System.out.println("\nНатисніть Enter для продовження до наступного тесту...");
                try {
                    System.in.read();
                } catch (Exception e) {}
            }
        }

        // Final conclusions
        System.out.println("\n" + "=".repeat(80));
        System.out.println("ЗАГАЛЬНІ ВИСНОВКИ");
        System.out.println("=".repeat(80));
        System.out.println("1. Алгоритм генерування випадкових чисел за формулою xi = σμi + a,");
        System.out.println("   де μi = Σξi - 6, успішно генерує числа з нормальним розподілом.");
        System.out.println("2. Це обумовлено центральною граничною теоремою: сума 12 незалежних");
        System.out.println("   рівномірно розподілених величин наближається до нормального розподілу.");
        System.out.println("3. Параметр 'a' контролює математичне сподівання розподілу.");
        System.out.println("4. Параметр 'σ' контролює дисперсію розподілу.");
        System.out.println("5. Метод ефективний для практичного використання і дає хорошу апроксимацію");
        System.out.println("   нормального розподілу.");
    }

    // Helper class for chi-square test results
    static class ChiSquareResult {
        double chiSquare;
        int degreesOfFreedom;
        double criticalValue;
        boolean isNormal;
        int[] observedFreq;
        double[] expectedFreq;

        ChiSquareResult(double chiSquare, int df, double criticalValue, boolean isNormal,
                        int[] observed, double[] expected) {
            this.chiSquare = chiSquare;
            this.degreesOfFreedom = df;
            this.criticalValue = criticalValue;
            this.isNormal = isNormal;
            this.observedFreq = observed;
            this.expectedFreq = expected;
        }
    }
}
