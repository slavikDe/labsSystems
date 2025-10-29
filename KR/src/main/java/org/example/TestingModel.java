package org.example;

import org.example.parts.*;
import org.example.util.Distribution;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for validation model with different input params and pretty print
 */
public class TestingModel {
    // Base parameters (default configuration)
    private static final double BASE_SIMULATION_TIME = 1_000_000;
    private static final double BASE_ARRIVAL_DELAY_MEAN = 300;
    private static final double BASE_ARRIVAL_DELAY_DEV = 100;
    private static final double BASE_TASK_SIZE_MEAN = 500;
    private static final double BASE_TASK_SIZE_DEV = 200;
    private static final double BASE_PROBABILITY_OF_INCORRECT = 0.05;
    private static final double BASE_PROCESS_SPEED = 0.5;
    private static final int BASE_QUEUE_CAPACITY = 25;
    private static final int BASE_DEVICES = 4;

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                              MODEL VERIFICATION AND TESTING REPORT                                                             ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");

        // Test 1: Vary ARRIVAL_DELAY_MEAN
        System.out.println("\n\n" + "█".repeat(150));
        System.out.println("TEST SERIES 1: VARYING ARRIVAL_DELAY_MEAN (Task arrival rate)");
        System.out.println("█".repeat(150));
        testArrivalDelayMean();

        // Test 2: Vary TASK_SIZE_MEAN
        System.out.println("\n\n" + "█".repeat(150));
        System.out.println("TEST SERIES 2: VARYING TASK_SIZE_MEAN (Average task complexity)");
        System.out.println("█".repeat(150));
        testTaskSizeMean();

        // Test 3: Vary PROBABILITY_OF_INCORRECT
        System.out.println("\n\n" + "█".repeat(150));
        System.out.println("TEST SERIES 3: VARYING PROBABILITY_OF_INCORRECT_TASK_PROCESSING (Error rate)");
        System.out.println("█".repeat(150));
        testProbabilityOfIncorrect();

        // Test 4: Vary PROCESS_SPEED
        System.out.println("\n\n" + "█".repeat(150));
        System.out.println("TEST SERIES 4: VARYING PROCESS_SPEED (Processing capacity)");
        System.out.println("█".repeat(150));
        testProcessSpeed();

        // Test 5: Vary QUEUE_CAPACITY
        System.out.println("\n\n" + "█".repeat(150));
        System.out.println("TEST SERIES 5: VARYING QUEUE_CAPACITY (Queue capacity)");
        System.out.println("█".repeat(150));
        testQueueCapacity();

        // Test 6: Vary DEVICES
        System.out.println("\n\n" + "█".repeat(150));
        System.out.println("TEST SERIES 6: VARYING NUMBER OF DEVICES (Server count)");
        System.out.println("█".repeat(150));
        testDevices();

        System.out.println("\n\n╔════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                    END OF TESTING REPORT                                                                      ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝\n");
    }

    private static void testArrivalDelayMean() {
        double[] testValues = {150, 200, 300, 400, 500, 600};
        System.out.println("Testing ARRIVAL_DELAY_MEAN with values: " + arrayToString(testValues));
        System.out.println("All other parameters held constant at base values\n");

        List<SimulationResult> results = new ArrayList<>();
        for (double value : testValues) {
            SimulationResult result = runSimulation(
                    value, value, BASE_ARRIVAL_DELAY_DEV,
                    BASE_TASK_SIZE_MEAN, BASE_TASK_SIZE_DEV,
                    BASE_PROBABILITY_OF_INCORRECT, BASE_PROCESS_SPEED,
                    BASE_QUEUE_CAPACITY, BASE_DEVICES, BASE_SIMULATION_TIME);
            results.add(result);
        }

        printConsolidatedTable("ARRIVAL_DELAY", testValues, results);
    }

    private static void testTaskSizeMean() {
        double[] testValues = {200, 350, 500, 650, 800, 1000};
        System.out.println("Testing TASK_SIZE_MEAN with values: " + arrayToString(testValues));
        System.out.println("All other parameters held constant at base values\n");

        List<SimulationResult> results = new ArrayList<>();
        for (double value : testValues) {
            SimulationResult result = runSimulation(
                    value, BASE_ARRIVAL_DELAY_MEAN, BASE_ARRIVAL_DELAY_DEV,
                    value, BASE_TASK_SIZE_DEV,
                    BASE_PROBABILITY_OF_INCORRECT, BASE_PROCESS_SPEED,
                    BASE_QUEUE_CAPACITY, BASE_DEVICES, BASE_SIMULATION_TIME);
            results.add(result);
        }

        printConsolidatedTable("TASK_SIZE_MEAN", testValues, results);
    }

    private static void testProbabilityOfIncorrect() {
        double[] testValues = {0.0, 0.02, 0.05, 0.10, 0.15, 0.20};
        System.out.println("Testing PROBABILITY_OF_INCORRECT with values: " + arrayToString(testValues));
        System.out.println("All other parameters held constant at base values\n");

        List<SimulationResult> results = new ArrayList<>();
        for (double value : testValues) {
            SimulationResult result = runSimulation(
                    value, BASE_ARRIVAL_DELAY_MEAN, BASE_ARRIVAL_DELAY_DEV,
                    BASE_TASK_SIZE_MEAN, BASE_TASK_SIZE_DEV,
                    value, BASE_PROCESS_SPEED,
                    BASE_QUEUE_CAPACITY, BASE_DEVICES, BASE_SIMULATION_TIME);
            results.add(result);
        }

        printConsolidatedTable("PROB_INCORRECT", testValues, results);
    }

    private static void testProcessSpeed() {
        double[] testValues = {0.25, 0.4, 0.5, 0.75, 1.0, 1.5};
        System.out.println("Testing PROCESS_SPEED with values: " + arrayToString(testValues));
        System.out.println("All other parameters held constant at base values\n");

        List<SimulationResult> results = new ArrayList<>();
        for (double value : testValues) {
            SimulationResult result = runSimulation(
                    value, BASE_ARRIVAL_DELAY_MEAN, BASE_ARRIVAL_DELAY_DEV,
                    BASE_TASK_SIZE_MEAN, BASE_TASK_SIZE_DEV,
                    BASE_PROBABILITY_OF_INCORRECT, value,
                    BASE_QUEUE_CAPACITY, BASE_DEVICES, BASE_SIMULATION_TIME);
            results.add(result);
        }

        printConsolidatedTable("PROCESS_SPEED", testValues, results);
    }

    private static void testQueueCapacity() {
        int[] testValues = {1, 2, 3, 4, 5};
        System.out.println("Testing QUEUE_CAPACITY with values: " + arrayToString(testValues));
        System.out.println("All other parameters held constant at base values\n");

        List<SimulationResult> results = new ArrayList<>();
        for (int value : testValues) {
            SimulationResult result = runSimulation(
                    value, BASE_ARRIVAL_DELAY_MEAN, BASE_ARRIVAL_DELAY_DEV,
                    BASE_TASK_SIZE_MEAN, BASE_TASK_SIZE_DEV,
                    BASE_PROBABILITY_OF_INCORRECT, BASE_PROCESS_SPEED,
                    value, BASE_DEVICES, BASE_SIMULATION_TIME);
            results.add(result);
        }

        printConsolidatedTableInt("QUEUE_CAPACITY", testValues, results);
    }

    private static void testDevices() {
        int[] testValues = {1, 2, 3, 4, 5, 6, 8};
        System.out.println("Testing NUMBER_OF_DEVICES with values: " + arrayToString(testValues));
        System.out.println("All other parameters held constant at base values\n");

        List<SimulationResult> results = new ArrayList<>();
        for (int value : testValues) {
            SimulationResult result = runSimulation(
                    value, BASE_ARRIVAL_DELAY_MEAN, BASE_ARRIVAL_DELAY_DEV,
                    BASE_TASK_SIZE_MEAN, BASE_TASK_SIZE_DEV,
                    BASE_PROBABILITY_OF_INCORRECT, BASE_PROCESS_SPEED,
                    BASE_QUEUE_CAPACITY, value, BASE_SIMULATION_TIME);
            results.add(result);
        }

        printConsolidatedTableInt("NUM_DEVICES",  testValues, results);
    }

    private static String arrayToString(double[] arr) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    private static String arrayToString(int[] arr) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    // Helper class to store simulation results
    static class SimulationResult {
        double paramValue;
        int createdTasks;
        int inputProcessed, inputOriginal, inputReprocessed;
        int processProcessed, processOriginal, processReprocessed;
        int outputProcessed, outputOriginal, outputReprocessed;
        double inputMeanQueue, processMeanQueue, outputMeanQueue;
        double inputFailProb, processFailProb, outputFailProb;
        int inputQueueLeft, processQueueLeft, outputQueueLeft;
        double inputUtil, processUtil, outputUtil;

        SimulationResult(double paramValue, ArrivalPoint arrival, Stage input, Stage process, Stage output,
                         double simulationTime) {
            this.paramValue = paramValue;
            this.createdTasks = arrival.getQuantity();

            // Input stage (receives from Arrival)
            this.inputProcessed = input.getQuantity();
            this.inputReprocessed = input.getReprocessedCount();
            this.inputOriginal = inputProcessed - inputReprocessed;
            this.inputMeanQueue = input.getQueue().getMeanValue() / simulationTime;
            int inputReceived = inputOriginal; // Input receives original tasks from Arrival
            int inputTotalAttempted = inputReceived + input.getQueue().getFailure();
            this.inputFailProb = inputTotalAttempted > 0 ? input.getQueue().getFailure() / (double) inputTotalAttempted : 0.0;
            this.inputQueueLeft = input.getQueue().getSize();
            this.inputUtil = (input.getBusyTime() / simulationTime) * 100 / input.getServerManager().getServersCount();

            // Process stage (receives from Input)
            this.processProcessed = process.getQuantity();
            this.processReprocessed = process.getReprocessedCount();
            this.processOriginal = processProcessed - processReprocessed;
            this.processMeanQueue = process.getQueue().getMeanValue() / simulationTime;
            // Process receives only original (non-reprocessed) tasks from Input
            int processReceived = inputOriginal;
            int processTotalAttempted = processReceived + process.getQueue().getFailure();
            this.processFailProb = processTotalAttempted > 0 ? process.getQueue().getFailure() / (double) processTotalAttempted : 0.0;
            this.processQueueLeft = process.getQueue().getSize();
            this.processUtil = (process.getBusyTime() / simulationTime) * 100 / process.getServerManager().getServersCount();

            // Output stage (receives from Process)
            this.outputProcessed = output.getQuantity();
            this.outputReprocessed = output.getReprocessedCount();
            this.outputOriginal = outputProcessed - outputReprocessed;
            this.outputMeanQueue = output.getQueue().getMeanValue() / simulationTime;
            // Output receives only original (non-reprocessed) tasks from Process
            int outputReceived = processOriginal;
            int outputTotalAttempted = outputReceived + output.getQueue().getFailure();
            this.outputFailProb = outputTotalAttempted > 0 ? output.getQueue().getFailure() / (double) outputTotalAttempted : 0.0;
            this.outputQueueLeft = output.getQueue().getSize();
            this.outputUtil = (output.getBusyTime() / simulationTime) * 100 / output.getServerManager().getServersCount();
        }
    }

    private static SimulationResult runSimulation(double paramValue,
                                                  double arrivalDelayMean, double arrivalDelayDev,
                                                  double taskSizeMean, double taskSizeDev,
                                                  double probIncorrect, double processSpeed,
                                                  int queueCapacity, int devices, double simulationTime) {
        Element.resetIdCounter();

        ArrivalPoint arrivalPoint = new ArrivalPoint("Arrival");
        arrivalPoint.setDelayMean(arrivalDelayMean);
        arrivalPoint.setDelayDev(arrivalDelayDev);
        arrivalPoint.setDistribution(Distribution.UNIFORM);
        arrivalPoint.setTask_mean(taskSizeMean);
        arrivalPoint.setTask_dev(taskSizeDev);

        Stage input = new Stage("Input", processSpeed);
        Stage process = new Stage("Process", processSpeed);
        Stage output = new Stage("Output", processSpeed);

        Dispose dispose = new Dispose();

        input.setServers(devices);
        process.setServers(devices);
        output.setServers(devices);

        input.setQueueCapacity(queueCapacity);
        process.setQueueCapacity(queueCapacity);
        output.setQueueCapacity(queueCapacity);

        // Routes
        arrivalPoint.setNextElement(input);
        input.setNextElement(process);
        process.setNextElement(output);
        output.addNextWithProbabilities(
                List.of(input, dispose),
                List.of(probIncorrect, 1 - probIncorrect)
        );

        ArrayList<Element> elements = new ArrayList<>(List.of(
                arrivalPoint,
                input,
                process,
                output
        ));

        ProcessingModel model = new ProcessingModel(elements, false);
        model.simulate(simulationTime);

        return new SimulationResult(paramValue, arrivalPoint, input, process, output, simulationTime);
    }

    private static void printConsolidatedTable(String paramName, double[] testValues, List<SimulationResult> results) {
        System.out.println("\n┌" + "─".repeat(170) + "┐");
        System.out.println("│ CONSOLIDATED RESULTS FOR " + paramName);
        System.out.println("├" + "─".repeat(170) + "┤");

        // Header
        System.out.println("│ " + String.format("%-15s | %-10s | %-45s | %-45s | %-45s",
                paramName, "Created", "INPUT", "PROCESS", "OUTPUT"));
        System.out.println("│ " + String.format("%-15s | %-10s | %-45s | %-45s | %-45s",
                "", "Tasks", "Total|Repr|MeanQ|Fail%|Util%", "Total|Repr|MeanQ|Fail%|Util%", "Total|Repr|MeanQ|Fail%|Util%"));
        System.out.println("├" + "─".repeat(170) + "┤");

        // Data rows
        for (SimulationResult r : results) {
            String inputStats = String.format("%4d|%4d|%5.2f|%5.2f|%5.1f",
                    r.inputProcessed, r.inputReprocessed, r.inputMeanQueue, r.inputFailProb * 100, r.inputUtil);
            String processStats = String.format("%4d|%4d|%5.2f|%5.2f|%5.1f",
                    r.processProcessed, r.processReprocessed, r.processMeanQueue, r.processFailProb * 100, r.processUtil);
            String outputStats = String.format("%4d|%4d|%5.2f|%5.2f|%5.1f",
                    r.outputProcessed, r.outputReprocessed, r.outputMeanQueue, r.outputFailProb * 100, r.outputUtil);

            System.out.println("│ " + String.format("%-15.2f | %-10d | %-45s | %-45s | %-45s",
                    r.paramValue, r.createdTasks, inputStats, processStats, outputStats));
        }

        System.out.println("└" + "─".repeat(170) + "┘\n");
    }

    private static void printConsolidatedTableInt(String paramName, int[] testValues, List<SimulationResult> results) {
        System.out.println("\n┌" + "─".repeat(170) + "┐");
        System.out.println("│ CONSOLIDATED RESULTS FOR " + paramName);
        System.out.println("├" + "─".repeat(170) + "┤");

        // Header
        System.out.println("│ " + String.format("%-15s | %-10s | %-45s | %-45s | %-45s",
                paramName, "Created", "INPUT", "PROCESS", "OUTPUT"));
        System.out.println("│ " + String.format("%-15s | %-10s | %-45s | %-45s | %-45s",
                "", "Tasks", "Total|Repr|MeanQ|Fail%|Util%", "Total|Repr|MeanQ|Fail%|Util%", "Total|Repr|MeanQ|Fail%|Util%"));
        System.out.println("├" + "─".repeat(170) + "┤");

        // Data rows
        for (SimulationResult r : results) {
            String inputStats = String.format("%4d|%4d|%5.2f|%5.2f|%5.1f",
                    r.inputProcessed, r.inputReprocessed, r.inputMeanQueue, r.inputFailProb * 100, r.inputUtil);
            String processStats = String.format("%4d|%4d|%5.2f|%5.2f|%5.1f",
                    r.processProcessed, r.processReprocessed, r.processMeanQueue, r.processFailProb * 100, r.processUtil);
            String outputStats = String.format("%4d|%4d|%5.2f|%5.2f|%5.1f",
                    r.outputProcessed, r.outputReprocessed, r.outputMeanQueue, r.outputFailProb * 100, r.outputUtil);

            System.out.println("│ " + String.format("%-15d | %-10d | %-45s | %-45s | %-45s",
                    (int) r.paramValue, r.createdTasks, inputStats, processStats, outputStats));
        }

        System.out.println("└" + "─".repeat(170) + "┘\n");
    }
}
