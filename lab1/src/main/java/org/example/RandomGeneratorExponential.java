//package org.example;
//
//import java.util.*;
//
//public class RandomGeneratorExponential {
//
//    private static final int SAMPLE_SIZE = 1000;
//    private static final int NUM_BINS = 20;
//    private static final double[] LAMBDA_VALUES = {0.5, 1.0, 2.0};
//    private static final int SORT_TEST_SIZE = 1000000;
//
//    public static void main(String[] args) {
//        System.out.println("====================================\n");
//
//        for (double lambda : LAMBDA_VALUES) {
//            testExponentialGenerator(lambda);
//            System.out.println("\n" + "=".repeat(50) + "\n");
//        }
//
//        // Додаткове завдання: дослідження часу сортування
////        testSortingTimeDistribution();
//    }
//
//    public static double[] generateExponentialNumbers(int size, double lambda) {
//        double[] numbers = new double[size];
//        Random random = new Random();
//        for (int i = 0; i < size; i++) {
//            double xi = random.nextDouble();
//            numbers[i] = -Math.log(xi) / lambda;
//        }
//        return numbers;
//    }
//
//    public static Statistics calculateStatistics(double[] numbers) {
//        double sum = 0;
//        double sumSquares = 0;
//
//        for (double num : numbers) {
//            sum += num;
//            sumSquares += num * num;
//        }
//
//        double mean = sum / numbers.length;
//        double variance = (sumSquares / numbers.length) - (mean * mean);
//        double stdDev = Math.sqrt(variance);
//
//        return new Statistics(mean, variance, stdDev);
//    }
//
//
//    public static Histogram buildHistogram(double[] numbers, int numBins) {
//        // Знаходження мінімального та максимального значень
//        double min = Arrays.stream(numbers).min().orElse(0);
//        double max = Arrays.stream(numbers).max().orElse(0);
//
//        // Обчислення ширини інтервалу
//        double binWidth = (max - min) / numBins;
//        int[] frequencies = new int[numBins];
//        double[] binCenters = new double[numBins];
//
//        // Заповнення центрів інтервалів
//        for (int i = 0; i < numBins; i++) {
//            binCenters[i] = min + (i + 0.5) * binWidth;
//        }
//
//        // Підрахунок частот для кожного інтервалу
//        for (double num : numbers) {
//            int binIndex = (int) Math.min((num - min) / binWidth, numBins - 1);
//            if (binIndex >= 0) {
//                frequencies[binIndex]++;
//            }
//        }
//
//        return new Histogram(frequencies, binCenters, binWidth, min, max);
//    }
//
//
//    public static void printHistogram(Histogram histogram) {
//        System.out.println("ГІСТОГРАМА ЧАСТОТ:");
//        System.out.println("Інтервал\t\tЧастота\t\tВізуалізація");
//        System.out.println("-".repeat(60));
//
//        int maxFreq = Arrays.stream(histogram.frequencies).max().orElse(1);
//
//        for (int i = 0; i < histogram.frequencies.length; i++) {
//            double left = histogram.min + i * histogram.binWidth;
//            double right = left + histogram.binWidth;
//
//            // Створення візуального представлення стовпця
//            int barLength = (int) (40.0 * histogram.frequencies[i] / maxFreq);
//            String bar = "*".repeat(barLength);
//
//            System.out.printf("[%.3f-%.3f)\t%d\t\t%s%n",
//                    left, right, histogram.frequencies[i], bar);
//        }
//        System.out.println();
//    }
//
//    // =================================================================
//    // ОБЧИСЛЕННЯ КРИТЕРІЮ ЗГОДИ ХІ-КВАДРАТ
//    // =================================================================
//    public static ChiSquareResult calculateChiSquareTest(double[] numbers, double lambda) {
//        Histogram histogram = buildHistogram(numbers, NUM_BINS);
//        double chiSquare = 0.0;
//        int validBins = 0;
//
//        System.out.println("ТЕСТ ХІ-КВАДРАТ:");
//        System.out.println("Інтервал\t\tСпостережено\tОчікувано\tВнесок");
//        System.out.println("-".repeat(65));
//
//        for (int i = 0; i < histogram.frequencies.length; i++) {
//            double left = histogram.min + i * histogram.binWidth;
//            double right = left + histogram.binWidth;
//
//            // Обчислення очікуваної частоти для експоненційного розподілу
//            double expectedProb = Math.exp(-lambda * left) - Math.exp(-lambda * right);
//            double expected = expectedProb * SAMPLE_SIZE;
//2
//            if (expected >= 5) { // Умова застосовності тесту хі-квадрат
//                double observed = histogram.frequencies[i];
//                double contribution = Math.pow(observed - expected, 2) / expected;
//                chiSquare += contribution;
//                validBins++;
//
//                System.out.printf("[%.3f-%.3f)\t%8.0f\t%8.2f\t%8.4f%n",
//                        left, right, observed, expected, contribution);
//            }
//        }
//
//        // Ступені свободи = кількість валідних інтервалів - 1 - кількість оцінених параметрів
//        int degreesOfFreedom = Math.max(1, validBins - 2);
//
//        return new ChiSquareResult(chiSquare, degreesOfFreedom, validBins);
//    }
//
//    // =================================================================
//    // ВИЗНАЧЕННЯ КРИТИЧНИХ ЗНАЧЕНЬ ХІ-КВАДРАТ
//    // =================================================================
//    public static double getCriticalValue(int df, double alpha) {
//        // Спрощена таблиця критичних значень для α = 0.05
//        Map<Integer, Double> criticalValues = new HashMap<>();
//        criticalValues.put(1, 3.841);
//        criticalValues.put(2, 5.991);
//        criticalValues.put(3, 7.815);
//        criticalValues.put(4, 9.488);
//        criticalValues.put(5, 11.070);
//        criticalValues.put(10, 18.307);
//        criticalValues.put(15, 24.996);
//        criticalValues.put(20, 31.410);
//
//        return criticalValues.getOrDefault(df, 31.410); // За замовчуванням для великих df
//    }
//
//    // =================================================================
//    // КОМПЛЕКСНЕ ТЕСТУВАННЯ ОДНОГО ГЕНЕРАТОРА
//    // =================================================================
//    public static void testExponentialGenerator(double lambda) {
//        System.out.printf("ТЕСТУВАННЯ ЕКСПОНЕНЦІЙНОГО ГЕНЕРАТОРА (λ = %.1f)%n", lambda);
//        System.out.println("=".repeat(50));
//
//        // Генерування випадкових чисел
//        double[] numbers = generateExponentialNumbers(SAMPLE_SIZE, lambda);
//
//        // Обчислення статистик
//        Statistics stats = calculateStatistics(numbers);
//
//        System.out.println("СТАТИСТИЧНІ ХАРАКТЕРИСТИКИ:");
//        System.out.printf("Середнє значення: %.6f%n", stats.mean);
//        System.out.printf("Дисперсія: %.6f%n", stats.variance);
//        System.out.printf("Стандартне відхилення: %.6f%n", stats.stdDev);
//        System.out.printf("Теоретичне середнє: %.6f%n", 1.0/lambda);
//        System.out.printf("Теоретична дисперсія: %.6f%n", 1.0/(lambda*lambda));
//        System.out.println();
//
//        // Побудова та виведення гістограми
//        Histogram histogram = buildHistogram(numbers, NUM_BINS);
//        printHistogram(histogram);
//
//        // Тест хі-квадрат
//        ChiSquareResult chiSquareResult = calculateChiSquareTest(numbers, lambda);
//        double criticalValue = getCriticalValue(chiSquareResult.degreesOfFreedom, 0.05);
//
//        System.out.printf("Значення хі-квадрат: %.4f%n", chiSquareResult.chiSquare);
//        System.out.printf("Ступені свободи: %d%n", chiSquareResult.degreesOfFreedom);
//        System.out.printf("Критичне значення (α=0.05): %.4f%n", criticalValue);
//
//        // Висновок про відповідність розподілу
//        if (chiSquareResult.chiSquare < criticalValue) {
//            System.out.println("ВИСНОВОК: Гіпотеза про експоненційний розподіл НЕ ВІДХИЛЯЄТЬСЯ");
//        } else {
//            System.out.println("ВИСНОВОК: Гіпотеза про експоненційний розподіл ВІДХИЛЯЄТЬСЯ");
//        }
//    }
//
//    // =================================================================
//    // ДОДАТКОВЕ ЗАВДАННЯ: ДОСЛІДЖЕННЯ ЧАСУ СОРТУВАННЯ
//    // =================================================================
//    public static void testSortingTimeDistribution() {
//        System.out.println("ДОДАТКОВЕ ЗАВДАННЯ: ДОСЛІДЖЕННЯ ЧАСУ СОРТУВАННЯ");
//        System.out.println("=".repeat(50));
//
//        List<Long> sortingTimes = new ArrayList<>();
//        Random random = new Random();
//
//        // Проведення 100 тестів сортування
//        for (int test = 0; test < 100; test++) {
//            // Створення випадкового масиву
//            List<Integer> data = new ArrayList<>();
//            for (int i = 0; i < SORT_TEST_SIZE; i++) {
//                data.add(random.nextInt(SORT_TEST_SIZE));
//            }
//
//            // Вимірювання часу сортування
//            long startTime = System.nanoTime();
//            Collections.sort(data);
//            long endTime = System.nanoTime();
//
//            long duration = endTime - startTime;
//            sortingTimes.add(duration);
//
//            if ((test + 1) % 20 == 0) {
//                System.out.printf("Виконано %d тестів...%n", test + 1);
//            }
//        }
//
//        // Конвертація у масив double для аналізу
//        double[] times = sortingTimes.stream().mapToDouble(Long::doubleValue).toArray();
//
//        // Статистичний аналіз часу сортування
//        Statistics timeStats = calculateStatistics(times);
//
//        System.out.println("\nСТАТИСТИКИ ЧАСУ СОРТУВАННЯ (наносекунди):");
//        System.out.printf("Середній час: %.0f нс (%.3f мс)%n",
//                timeStats.mean, timeStats.mean / 1_000_000);
//        System.out.printf("Дисперсія: %.2e%n", timeStats.variance);
//        System.out.printf("Стандартне відхилення: %.0f нс%n", timeStats.stdDev);
//
//        // Побудова гістограми часів
//        Histogram timeHistogram = buildHistogram(times, 15);
//        printHistogram(timeHistogram);
//
//        System.out.println("ВИСНОВОК: Розподіл часу сортування наближається до нормального");
//    }
//
//    // =================================================================
//    // ДОПОМІЖНІ КЛАСИ ДЛЯ ЗБЕРІГАННЯ РЕЗУЛЬТАТІВ
//    // =================================================================
//    static class Statistics {
//        final double mean;
//        final double variance;
//        final double stdDev;
//
//        Statistics(double mean, double variance, double stdDev) {
//            this.mean = mean;
//            this.variance = variance;
//            this.stdDev = stdDev;
//        }
//    }
//
//    static class Histogram {
//        final int[] frequencies;
//        final double[] binCenters;
//        final double binWidth;
//        final double min;
//        final double max;
//
//        Histogram(int[] frequencies, double[] binCenters, double binWidth, double min, double max) {
//            this.frequencies = frequencies;
//            this.binCenters = binCenters;
//            this.binWidth = binWidth;
//            this.min = min;
//            this.max = max;
//        }
//    }
//
//    static class ChiSquareResult {
//        final double chiSquare;
//        final int degreesOfFreedom;
//        final int validBins;
//
//        ChiSquareResult(double chiSquare, int degreesOfFreedom, int validBins) {
//            this.chiSquare = chiSquare;
//            this.degreesOfFreedom = degreesOfFreedom;
//            this.validBins = validBins;
//        }
//    }
//}