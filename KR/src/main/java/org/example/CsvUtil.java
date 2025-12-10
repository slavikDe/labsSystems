package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;

public class CsvUtil {

    private static final String FILE_NAME = "stats.csv";
    private static final String QUEUE_DIAGNOSTIC_FILE = "queue_diagnostics.csv";
    private static boolean headerWritten = true;

    /**
     * Writes a single record (row) to the CSV file.
     * If the file doesn't exist yet, writes the header first.
     *
     * @param time current time value
     * @param avgQ current average queue value
     */
    public static synchronized void writeInFile(String stage, double time, double avgQ) {
        try {
            File file = new File(FILE_NAME);
            boolean append = file.exists();

            try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {
                // write header if first time
                if (!append || !headerWritten) {
                    writer.println("stage,time,avgQ");
                    headerWritten = true;
                }

                writer.printf("%s,%.2f,%.4f%n", stage, time, avgQ);
            }

        } catch (IOException e) {
            System.err.println("Error writing to CSV: " + e.getMessage());
        }
    }

    /**
     * Clears the CSV file (use before a new experiment).
     */
    public static void resetFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, false))) {
            writer.println("time,avgQ");
            headerWritten = true;
        } catch (IOException e) {
            System.err.println("Error resetting CSV: " + e.getMessage());
        }
    }

    /**
     * Resets and initializes the queue diagnostics CSV file with header.
     */
    public static void resetQueueDiagnosticFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(QUEUE_DIAGNOSTIC_FILE, false))) {
            // Write comprehensive header
            writer.println("InputQueueCap,ProcessQueueCap,OutputQueueCap," +
                          "TotalQueueCap,TasksCreated,TasksCompleted,SystemThroughput," +
                          "InputProcessed,InputOriginal,InputReprocessed,InputFailures,InputFailRate," +
                          "InputMeanQueueLen,InputUtilization," +
                          "ProcessProcessed,ProcessOriginal,ProcessReprocessed,ProcessFailures,ProcessFailRate," +
                          "ProcessMeanQueueLen,ProcessUtilization," +
                          "OutputProcessed,OutputOriginal,OutputReprocessed,OutputFailures,OutputFailRate," +
                          "OutputMeanQueueLen,OutputUtilization," +
                          "TotalFailures,AvgFailureRate,AvgUtilization,CostBenefitScore");
        } catch (IOException e) {
            System.err.println("Error resetting queue diagnostic CSV: " + e.getMessage());
        }
    }

    /**
     * Writes a comprehensive queue diagnostic record to CSV.
     */
//    public static synchronized void writeQueueDiagnostic(
//            int inputQCap, int processQCap, int outputQCap,
//            int tasksCreated, int tasksCompleted, double systemThroughput,
//            Main.StageMetrics inputMetrics, Main.StageMetrics processMetrics, Main.StageMetrics outputMetrics,
//            int totalQueueCap, int totalFailures, double totalFailureRate,
//            double avgUtilization, double costBenefitScore) {
//
//        try (PrintWriter writer = new PrintWriter(new FileWriter(QUEUE_DIAGNOSTIC_FILE, true))) {
//            writer.printf("%d,%d,%d,%d,%d,%d,%.4f," +
//                         "%d,%d,%d,%d,%.6f,%.4f,%.2f," +
//                         "%d,%d,%d,%d,%.6f,%.4f,%.2f," +
//                         "%d,%d,%d,%d,%.6f,%.4f,%.2f," +
//                         "%d,%.6f,%.2f,%.4f%n",
//                    // Queue capacities
//                    inputQCap, processQCap, outputQCap, totalQueueCap,
//                    // System metrics
//                    tasksCreated, tasksCompleted, systemThroughput,
//                    // Input stage
//                    inputMetrics.processed, inputMetrics.processedOriginal, inputMetrics.reprocessed,
//                    inputMetrics.failures, inputMetrics.failureRate, inputMetrics.meanQueueLength,
//                    inputMetrics.utilization,
//                    // Process stage
//                    processMetrics.processed, processMetrics.processedOriginal, processMetrics.reprocessed,
//                    processMetrics.failures, processMetrics.failureRate, processMetrics.meanQueueLength,
//                    processMetrics.utilization,
//                    // Output stage
//                    outputMetrics.processed, outputMetrics.processedOriginal, outputMetrics.reprocessed,
//                    outputMetrics.failures, outputMetrics.failureRate, outputMetrics.meanQueueLength,
//                    outputMetrics.utilization,
//                    // Aggregate metrics
//                    totalFailures, totalFailureRate, avgUtilization, costBenefitScore
//            );
//        } catch (IOException e) {
//            System.err.println("Error writing queue diagnostic: " + e.getMessage());
//        }
//    }
}
