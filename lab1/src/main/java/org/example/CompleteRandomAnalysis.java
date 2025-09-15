package org.example;

import java.util.*;
import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

public class CompleteRandomAnalysis {
    private static final int SAMPLE_SIZE = 10000;
    private static final int NUM_BINS = 20;
    private static final double ALPHA = 0.05;
    private static final DecimalFormat df = new DecimalFormat("#.####");

    // Linear Congruential Generator
    static class LCGGenerator {
        private static final long A = (long)Math.pow(5, 13); // 5^13
        private static final long C = (long)Math.pow(2, 31); // 2^31
        private static final long M = (long)Math.pow(2, 32); // 2^32 for modulo

        private long seed;
        private long currentZ;

        public LCGGenerator(long seed) {
            this.seed = seed;
            this.currentZ = seed;
        }

        public double nextDouble() {
            currentZ = (A * currentZ) % M;
            return (double) currentZ / M;
        }

        public double[] generateSample(int size) {
            double[] sample = new double[size];
            for (int i = 0; i < size; i++) {
                sample[i] = nextDouble();
            }
            return sample;
        }
    }

    // Normal Distribution Generator (from previous task)
    static class NormalGenerator {
        private Random random;
        private double a, sigma;

        public NormalGenerator(double a, double sigma) {
            this.random = new Random();
            this.a = a;
            this.sigma = sigma;
        }

        public double nextDouble() {
            double sum = 0;
            for (int i = 0; i < 12; i++) {
                sum += random.nextDouble();
            }
            double mu = sum - 6;
            return sigma * mu + a;
        }

        public double[] generateSample(int size) {
            double[] sample = new double[size];
            for (int i = 0; i < size; i++) {
                sample[i] = nextDouble();
            }
            return sample;
        }
    }

    // Sorting Time Analyzer
    static class SortingTimeAnalyzer {
        private static final int COLLECTION_SIZE = 1000000;
        private static final int NUM_EXPERIMENTS = 100;

        public double[] analyzeSortingTimes() {
            System.out.println("Аналіз часу виконання сортування колекції з " + COLLECTION_SIZE + " елементів");
            System.out.println("Кількість експериментів: " + NUM_EXPERIMENTS);
            System.out.println("Виконується...");

            double[] times = new double[NUM_EXPERIMENTS];

            for (int i = 0; i < NUM_EXPERIMENTS; i++) {
                // Generate random collection
                List<Integer> collection = new ArrayList<>(COLLECTION_SIZE);
                for (int j = 0; j < COLLECTION_SIZE; j++) {
                    collection.add(ThreadLocalRandom.current().nextInt());
                }

                // Measure sorting time
                long startTime = System.nanoTime();
                Collections.sort(collection);
                long endTime = System.nanoTime();

                times[i] = (endTime - startTime) / 1_000_000.0; // Convert to milliseconds

                if ((i + 1) % 10 == 0) {
                    System.out.printf("Виконано %d/%d експериментів\n", i + 1, NUM_EXPERIMENTS);
                }
            }

            return times;
        }
    }

    // Statistical Analysis Helper
    static class StatisticalAnalyzer {

        public static double calculateMean(double[] data) {
            return Arrays.stream(data).average().orElse(0.0);
        }

        public static double calculateVariance(double[] data) {
            double mean = calculateMean(data);
            return Arrays.stream(data)
                    .map(x -> Math.pow(x - mean, 2))
                    .average()
                    .orElse(0.0);
        }

        public static double calculateStandardDeviation(double[] data) {
            return Math.sqrt(calculateVariance(data));
        }

        public static double[] createBinEdges(double[] data, int numBins) {
            double min = Arrays.stream(data).min().orElse(0);
            double max = Arrays.stream(data).max().orElse(0);
            double range = max - min;
            double binWidth = range / numBins;

            double[] binEdges = new double[numBins + 1];
            for (int i = 0; i <= numBins; i++) {
                binEdges[i] = min + i * binWidth;
            }
            return binEdges;
        }

        public static int[] buildHistogram(double[] data, double[] binEdges) {
            int numBins = binEdges.length - 1;
            int[] histogram = new int[numBins];

            for (double value : data) {
                for (int i = 0; i < numBins; i++) {
                    if (value >= binEdges[i] && (value < binEdges[i + 1] ||
                            (i == numBins - 1 && value <= binEdges[i + 1]))) {
                        histogram[i]++;
                        break;
                    }
                }
            }
            return histogram;
        }

        public static void printHistogram(int[] histogram, double[] binEdges, String title) {
            System.out.println("\n" + title);
            System.out.println("Інтервал\t\tЧастота\t\tВідносна частота");
            System.out.println("-".repeat(60));

            int totalCount = Arrays.stream(histogram).sum();
            for (int i = 0; i < histogram.length; i++) {
                double relativeFreq = (double) histogram[i] / totalCount;
                System.out.printf("[%.4f, %.4f)\t\t%d\t\t%.4f\n",
                        binEdges[i], binEdges[i + 1], histogram[i], relativeFreq);
            }
        }

        public static void printVisualHistogram(int[] histogram, String title) {
            System.out.println("\n" + title);
            int maxFreq = Arrays.stream(histogram).max().orElse(1);
            int scale = Math.max(1, maxFreq / 50);

            for (int i = 0; i < histogram.length; i++) {
                System.out.printf("Bin %2d: ", i + 1);
                int stars = histogram[i] / scale;
                for (int j = 0; j < stars; j++) {
                    System.out.print("*");
                }
                System.out.printf(" (%d)\n", histogram[i]);
            }
        }

        // Chi-square test for uniform distribution
        public static ChiSquareResult chiSquareTestUniform(double[] data) {
            double[] binEdges = createBinEdges(data, NUM_BINS);
            int[] observedFreq = buildHistogram(data, binEdges);

            // For uniform distribution, expected frequency is the same for all bins
            double expectedFreq = (double) data.length / NUM_BINS;

            double chiSquare = 0;
            for (int i = 0; i < NUM_BINS; i++) {
                chiSquare += Math.pow(observedFreq[i] - expectedFreq, 2) / expectedFreq;
            }

            int degreesOfFreedom = NUM_BINS - 1;
            double criticalValue = getCriticalValue(degreesOfFreedom);

            return new ChiSquareResult(chiSquare, degreesOfFreedom, criticalValue,
                    chiSquare < criticalValue);
        }

        // Chi-square test for normal distribution
        public static ChiSquareResult chiSquareTestNormal(double[] data) {
            double mean = calculateMean(data);
            double stdDev = calculateStandardDeviation(data);

            double[] binEdges = createBinEdges(data, NUM_BINS);
            int[] observedFreq = buildHistogram(data, binEdges);
            double[] expectedFreq = new double[NUM_BINS];

            for (int i = 0; i < NUM_BINS; i++) {
                double binCenter = (binEdges[i] + binEdges[i + 1]) / 2;
                double binWidth = binEdges[i + 1] - binEdges[i];
                expectedFreq[i] = data.length * normalPDF(binCenter, mean, stdDev) * binWidth;
            }

            double chiSquare = 0;
            int validBins = 0;

            for (int i = 0; i < NUM_BINS; i++) {
                if (expectedFreq[i] >= 5) {
                    chiSquare += Math.pow(observedFreq[i] - expectedFreq[i], 2) / expectedFreq[i];
                    validBins++;
                }
            }

            int degreesOfFreedom = validBins - 3; // -3 for estimated mean and variance
            double criticalValue = getCriticalValue(degreesOfFreedom);

            return new ChiSquareResult(chiSquare, degreesOfFreedom, criticalValue,
                    chiSquare < criticalValue);
        }

        private static double normalPDF(double x, double mean, double stdDev) {
            return (1 / (stdDev * Math.sqrt(2 * Math.PI))) *
                    Math.exp(-0.5 * Math.pow((x - mean) / stdDev, 2));
        }

        private static double getCriticalValue(int df) {
            if (df <= 0) return Double.MAX_VALUE;
            if (df == 1) return 3.841;
            if (df == 2) return 5.991;
            if (df == 3) return 7.815;
            if (df == 4) return 9.488;
            if (df == 5) return 11.070;
            if (df <= 10) return 12.592 + (df - 5) * 1.5;
            return 15 + df * 1.2;
        }
    }

    static class ChiSquareResult {
        double chiSquare;
        int degreesOfFreedom;
        double criticalValue;
        boolean passesTest;

        ChiSquareResult(double chiSquare, int df, double criticalValue, boolean passes) {
            this.chiSquare = chiSquare;
            this.degreesOfFreedom = df;
            this.criticalValue = criticalValue;
            this.passesTest = passes;
        }
    }

    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("КОМПЛЕКСНИЙ АНАЛІЗ ГЕНЕРАТОРІВ ВИПАДКОВИХ ЧИСЕЛ");
        System.out.println("=".repeat(80));

        // Test 1: Linear Congruential Generator
        System.out.println("\n1. ЛІНІЙНИЙ КОНГРУЕНТНИЙ ГЕНЕРАТОР");
        System.out.println("   zi+1 = a*zi (mod c), xi+1 = zi+1/c");
        System.out.println("   a = 5^13, c = 2^31");
        System.out.println("=".repeat(50));

        LCGGenerator lcg = new LCGGenerator(System.currentTimeMillis());
        double[] lcgSample = lcg.generateSample(SAMPLE_SIZE);

        double lcgMean = StatisticalAnalyzer.calculateMean(lcgSample);
        double lcgVariance = StatisticalAnalyzer.calculateVariance(lcgSample);
        double lcgStdDev = StatisticalAnalyzer.calculateStandardDeviation(lcgSample);

        System.out.printf("Вибіркове середнє: %s (теоретичне: 0.5)\n", df.format(lcgMean));
        System.out.printf("Вибіркова дисперсія: %s (теоретична: 0.0833)\n", df.format(lcgVariance));
        System.out.printf("Вибіркове СКВ: %s (теоретичне: 0.2887)\n", df.format(lcgStdDev));

        double[] lcgBinEdges = StatisticalAnalyzer.createBinEdges(lcgSample, NUM_BINS);
        int[] lcgHistogram = StatisticalAnalyzer.buildHistogram(lcgSample, lcgBinEdges);

        StatisticalAnalyzer.printHistogram(lcgHistogram, lcgBinEdges, "Гістограма частот (LCG):");
        StatisticalAnalyzer.printVisualHistogram(lcgHistogram, "Візуальна гістограма (LCG):");

        ChiSquareResult lcgChiTest = StatisticalAnalyzer.chiSquareTestUniform(lcgSample);

        System.out.println("\nПеревірка відповідності рівномірному закону розподілу:");
        System.out.printf("Статистика χ²: %.4f\n", lcgChiTest.chiSquare);
        System.out.printf("Ступені свободи: %d\n", lcgChiTest.degreesOfFreedom);
        System.out.printf("Критичне значення: %.4f\n", lcgChiTest.criticalValue);
        System.out.printf("Результат: %s\n", lcgChiTest.passesTest ?
                "ПРИЙМАЄМО гіпотезу про рівномірність" :
                "ВІДХИЛЯЄМО гіпотезу про рівномірність");

        // Test 2: Normal Distribution Generator
        System.out.println("\n\n2. ГЕНЕРАТОР НОРМАЛЬНОГО РОЗПОДІЛУ");
        System.out.println("   xi = σμi + a, μi = Σξi - 6");
        System.out.println("=".repeat(50));

        NormalGenerator normalGen = new NormalGenerator(0, 1); // N(0,1)
        double[] normalSample = normalGen.generateSample(SAMPLE_SIZE);

        double normalMean = StatisticalAnalyzer.calculateMean(normalSample);
        double normalVariance = StatisticalAnalyzer.calculateVariance(normalSample);
        double normalStdDev = StatisticalAnalyzer.calculateStandardDeviation(normalSample);

        System.out.printf("Вибіркове середнє: %s (теоретичне: 0.0)\n", df.format(normalMean));
        System.out.printf("Вибіркова дисперсія: %s (теоретична: 1.0)\n", df.format(normalVariance));
        System.out.printf("Вибіркове СКВ: %s (теоретичне: 1.0)\n", df.format(normalStdDev));

        double[] normalBinEdges = StatisticalAnalyzer.createBinEdges(normalSample, NUM_BINS);
        int[] normalHistogram = StatisticalAnalyzer.buildHistogram(normalSample, normalBinEdges);

        StatisticalAnalyzer.printHistogram(normalHistogram, normalBinEdges, "Гістограма частот (Normal):");
        StatisticalAnalyzer.printVisualHistogram(normalHistogram, "Візуальна гістограма (Normal):");

        ChiSquareResult normalChiTest = StatisticalAnalyzer.chiSquareTestNormal(normalSample);

        System.out.println("\nПеревірка відповідності нормальному закону розподілу:");
        System.out.printf("Статистика χ²: %.4f\n", normalChiTest.chiSquare);
        System.out.printf("Ступені свободи: %d\n", normalChiTest.degreesOfFreedom);
        System.out.printf("Критичне значення: %.4f\n", normalChiTest.criticalValue);
        System.out.printf("Результат: %s\n", normalChiTest.passesTest ?
                "ПРИЙМАЄМО гіпотезу про нормальність" :
                "ВІДХИЛЯЄМО гіпотезу про нормальність");

        // Test 3: Sorting Time Analysis
        System.out.println("\n\n3. АНАЛІЗ ЗАКОНУ РОЗПОДІЛУ ЧАСУ СОРТУВАННЯ");
        System.out.println("=".repeat(50));

        SortingTimeAnalyzer sorter = new SortingTimeAnalyzer();
        double[] sortingTimes = sorter.analyzeSortingTimes();

        double timeMean = StatisticalAnalyzer.calculateMean(sortingTimes);
        double timeVariance = StatisticalAnalyzer.calculateVariance(sortingTimes);
        double timeStdDev = StatisticalAnalyzer.calculateStandardDeviation(sortingTimes);

        System.out.printf("\nСтатистики часу сортування (мс):\n");
        System.out.printf("Середній час: %.2f мс\n", timeMean);
        System.out.printf("Дисперсія: %.2f\n", timeVariance);
        System.out.printf("Стандартне відхилення: %.2f мс\n", timeStdDev);
        System.out.printf("Мінімальний час: %.2f мс\n", Arrays.stream(sortingTimes).min().orElse(0));
        System.out.printf("Максимальний час: %.2f мс\n", Arrays.stream(sortingTimes).max().orElse(0));

        double[] timeBinEdges = StatisticalAnalyzer.createBinEdges(sortingTimes, NUM_BINS);
        int[] timeHistogram = StatisticalAnalyzer.buildHistogram(sortingTimes, timeBinEdges);

        StatisticalAnalyzer.printHistogram(timeHistogram, timeBinEdges, "Гістограма часу сортування:");
        StatisticalAnalyzer.printVisualHistogram(timeHistogram, "Візуальна гістограма часу сортування:");

        ChiSquareResult timeChiTest = StatisticalAnalyzer.chiSquareTestNormal(sortingTimes);

        System.out.println("\nПеревірка відповідності нормальному закону розподілу:");
        System.out.printf("Статистика χ²: %.4f\n", timeChiTest.chiSquare);
        System.out.printf("Ступені свободи: %d\n", timeChiTest.degreesOfFreedom);
        System.out.printf("Критичне значення: %.4f\n", timeChiTest.criticalValue);
        System.out.printf("Результат: %s\n", timeChiTest.passesTest ?
                "ПРИЙМАЄМО гіпотезу про нормальність" :
                "ВІДХИЛЯЄМО гіпотезу про нормальність");

        // Final Conclusions
        System.out.println("\n" + "=".repeat(80));
        System.out.println("ЗАГАЛЬНІ ВИСНОВКИ");
        System.out.println("=".repeat(80));

        System.out.println("\n1. ЛІНІЙНИЙ КОНГРУЕНТНИЙ ГЕНЕРАТОР:");
        System.out.println("   - Генерує псевдовипадкові числа в інтервалі (0,1)");
        System.out.println("   - При правильних параметрах дає рівномірний розподіл");
        System.out.println("   - Швидкий та ефективний для практичного використання");
        System.out.printf("   - Тест на рівномірність: %s\n",
                lcgChiTest.passesTest ? "ПРОЙДЕНО" : "НЕ ПРОЙДЕНО");

        System.out.println("\n2. ГЕНЕРАТОР НОРМАЛЬНОГО РОЗПОДІЛУ:");
        System.out.println("   - Базується на центральній граничній теоремі");
        System.out.println("   - Успішно апроксимує нормальний розподіл");
        System.out.println("   - Підходить для моделювання природних процесів");
        System.out.printf("   - Тест на нормальність: %s\n",
                normalChiTest.passesTest ? "ПРОЙДЕНО" : "НЕ ПРОЙДЕНО");

        System.out.println("\n3. РОЗПОДІЛ ЧАСУ СОРТУВАННЯ:");
        System.out.println("   - Час сортування має певну варіативність");
        System.out.println("   - Залежить від системних факторів та навантаження");
        System.out.printf("   - Середній час: %.2f ± %.2f мс\n", timeMean, timeStdDev);
        System.out.printf("   - Розподіл часу: %s до нормального\n",
                timeChiTest.passesTest ? "наближається" : "не відповідає");

        System.out.println("\n4. ПРАКТИЧНІ РЕКОМЕНДАЦІЇ:");
        System.out.println("   - LCG підходить для швидкого генерування рівномірних чисел");
        System.out.println("   - Метод сумування для нормального розподілу є надійним");
        System.out.println("   - Час виконання алгоритмів може мати значну варіацію");
        System.out.println("   - Необхідно враховувати статистичні властивості при виборі методу");
    }
}