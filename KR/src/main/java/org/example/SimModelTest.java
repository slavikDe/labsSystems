package org.example;

import org.example.model_parts.*;
import org.example.model_parts.Process;
import org.example.simsimple.Distribution;

import java.util.ArrayList;
import java.util.List;


public class SimModelTest {
    // Base parameters (default configuration)
    private static final double BASE_SIMULATION_TIME = 100_000; // Increased from 1,000 to 100,000
    private static final double BASE_CREATE_DELAY_MEAN = 300;
    private static final double BASE_CREATE_DELAY_DEV = 100;
    private static final double BASE_TASK_SIZE_MEAN = 500;
    private static final double BASE_TASK_SIZE_DEV = 200;
    private static final double BASE_PROBABILITY_OF_INCORRECT = 0.05;
    private static final double BASE_PROCESS_SPEED = 0.5;
    private static final int BASE_MAX_QUEUE = 100;

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                              MODEL VERIFICATION AND TESTING REPORT                                                             ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");

        // Test 1: Vary CREATE_DELAY_MEAN
        System.out.println("\n\n" + "█".repeat(150));
        System.out.println("TEST SERIES 1: VARYING CREATE_DELAY_MEAN (Task arrival rate)");
        System.out.println("█".repeat(150));
        testCreateDelayMean();

//        // Test 2: Vary CREATE_DELAY_DEV
//        System.out.println("\n\n" + "█".repeat(150));
//        System.out.println("TEST SERIES 2: VARYING CREATE_DELAY_DEV (Task arrival variability)");
//        System.out.println("█".repeat(150));
//        testCreateDelayDev();

        // Test 3: Vary TASK_SIZE_MEAN
        System.out.println("\n\n" + "█".repeat(150));
        System.out.println("TEST SERIES 3: VARYING TASK_SIZE_MEAN (Average task complexity)");
        System.out.println("█".repeat(150));
        testTaskSizeMean();

//        // Test 4: Vary TASK_SIZE_DEV
//        System.out.println("\n\n" + "█".repeat(150));
//        System.out.println("TEST SERIES 4: VARYING TASK_SIZE_DEV (Task complexity variability)");
//        System.out.println("█".repeat(150));
//        testTaskSizeDev();

        // Test 5: Vary PROBABILITY_OF_INCORRECT
        System.out.println("\n\n" + "█".repeat(150));
        System.out.println("TEST SERIES 5: VARYING PROBABILITY_OF_INCORRECT_TASK_PROCESSING (Error rate)");
        System.out.println("█".repeat(150));
        testProbabilityOfIncorrect();

        // Test 6: Vary PROCESS_SPEED
        System.out.println("\n\n" + "█".repeat(150));
        System.out.println("TEST SERIES 6: VARYING PROCESS_SPEED (Processing capacity)");
        System.out.println("█".repeat(150));
        testProcessSpeed();

        // Test 7: Vary MAX_QUEUE
        System.out.println("\n\n" + "█".repeat(150));
        System.out.println("TEST SERIES 7: VARYING MAX_QUEUE (Queue capacity)");
        System.out.println("█".repeat(150));
        testMaxQueue();

        // Test 8: Vary SIMULATION_TIME
        System.out.println("\n\n" + "█".repeat(150));
        System.out.println("TEST SERIES 8: VARYING SIMULATION_TIME (Simulation duration)");
        System.out.println("█".repeat(150));
        testSimulationTime();

        System.out.println("\n\n" + "╔════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                    END OF TESTING REPORT                                                                      ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝\n");
    }

    private static void testCreateDelayMean() {
        double[] testValues = {150, 200, 300, 400, 500, 600};
        System.out.println("Testing CREATE_DELAY_MEAN with values: " + arrayToString(testValues));
        System.out.println("All other parameters held constant at base values\n");

        // Collect results
        List<SimulationResult> results = new ArrayList<>();
        for (double value : testValues) {
            SimulationResult result = runSimulationAndCollect(value, value, BASE_CREATE_DELAY_DEV, BASE_TASK_SIZE_MEAN, BASE_TASK_SIZE_DEV,
                    BASE_PROBABILITY_OF_INCORRECT, BASE_PROCESS_SPEED, BASE_MAX_QUEUE, BASE_SIMULATION_TIME);
            results.add(result);
        }

        // Print consolidated table
        printConsolidatedTable("CREATE_DELAY_MEAN", testValues, results);
    }


    private static void testTaskSizeMean() {
        double[] testValues = {200, 350, 500, 650, 800, 1000};
        System.out.println("Testing TASK_SIZE_MEAN with values: " + arrayToString(testValues));
        System.out.println("All other parameters held constant at base values\n");

        List<SimulationResult> results = new ArrayList<>();
        for (double value : testValues) {
            SimulationResult result = runSimulationAndCollect(value, BASE_CREATE_DELAY_MEAN, BASE_CREATE_DELAY_DEV, value, BASE_TASK_SIZE_DEV,
                    BASE_PROBABILITY_OF_INCORRECT, BASE_PROCESS_SPEED, BASE_MAX_QUEUE, BASE_SIMULATION_TIME);
            results.add(result);
        }
        printConsolidatedTable("TASK_SIZE_MEAN", testValues, results);
    }


    private static void testProbabilityOfIncorrect() {
        double[] testValues = {0.0, 0.02, 0.05, 0.10, 0.15, 0.20};
        System.out.println("Testing PROBABILITY_OF_INCORRECT with values: " + arrayToString(testValues));
        System.out.println("All other parameters held constant at base values\n");
        System.out.println("Process speed set to 10.0");
        List<SimulationResult> results = new ArrayList<>();
        for (double value : testValues) {
            SimulationResult result = runSimulationAndCollect(value, BASE_CREATE_DELAY_MEAN, BASE_CREATE_DELAY_DEV, BASE_TASK_SIZE_MEAN, BASE_TASK_SIZE_DEV,
                    value, 10.0, BASE_MAX_QUEUE, BASE_SIMULATION_TIME);
            results.add(result);
        }
        printConsolidatedTable("PROB_INCORRECT", testValues, results);
    }

    private static void testProcessSpeed() {
        double[] testValues = {0.25, 0.4, 0.5, 0.75, 1.0, 1.5, 10};
        System.out.println("Testing PROCESS_SPEED with values: " + arrayToString(testValues));
        System.out.println("All other parameters held constant at base values\n");

        List<SimulationResult> results = new ArrayList<>();
        for (double value : testValues) {
            SimulationResult result = runSimulationAndCollect(value, BASE_CREATE_DELAY_MEAN, BASE_CREATE_DELAY_DEV, BASE_TASK_SIZE_MEAN, BASE_TASK_SIZE_DEV,
                    BASE_PROBABILITY_OF_INCORRECT, value, BASE_MAX_QUEUE, BASE_SIMULATION_TIME);
            results.add(result);
        }
        printConsolidatedTable("PROCESS_SPEED", testValues, results);
    }

    private static void testMaxQueue() {
        int[] testValues = {20, 50, 100, 150, 200};
        System.out.println("Testing MAX_QUEUE with values: " + arrayToString(testValues));
        System.out.println("All other parameters held constant at base values\n");

        List<SimulationResult> results = new ArrayList<>();
        for (int value : testValues) {
            SimulationResult result = runSimulationAndCollect(value, BASE_CREATE_DELAY_MEAN, BASE_CREATE_DELAY_DEV, BASE_TASK_SIZE_MEAN, BASE_TASK_SIZE_DEV,
                    BASE_PROBABILITY_OF_INCORRECT, BASE_PROCESS_SPEED, value, BASE_SIMULATION_TIME);
            results.add(result);
        }
        printConsolidatedTableInt("MAX_QUEUE", testValues, results);
    }

    private static void testSimulationTime() {
        double[] testValues = {10_000, 25_000, 50_000, 100_000, 200_000, 500_000};
        System.out.println("Testing SIMULATION_TIME with values: " + arrayToString(testValues));
        System.out.println("All other parameters held constant at base values\n");

        List<SimulationResult> results = new ArrayList<>();
        for (double value : testValues) {
            SimulationResult result = runSimulationAndCollect(value, BASE_CREATE_DELAY_MEAN, BASE_CREATE_DELAY_DEV, BASE_TASK_SIZE_MEAN, BASE_TASK_SIZE_DEV,
                    BASE_PROBABILITY_OF_INCORRECT, BASE_PROCESS_SPEED, BASE_MAX_QUEUE, value);
            results.add(result);
        }
        printConsolidatedTable("SIMULATION_TIME", testValues, results);
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
        int d1Processed, d2Processed, d3Processed;
        double d1MeanQueue, d2MeanQueue, d3MeanQueue;
        double d1FailProb, d2FailProb, d3FailProb;
        int d1QueueLeft, d2QueueLeft, d3QueueLeft;
        double d1Util, d2Util, d3Util;

        SimulationResult(double paramValue, Create c, Input d1, Process d2, Output d3,
                        double simulationTime) {
            this.paramValue = paramValue;
            this.createdTasks = c.getQuantity();
            this.d1Processed = d1.getQuantity();
            this.d2Processed = d2.getQuantity();
            this.d3Processed = d3.getQuantity();

            this.d1MeanQueue = d1.getMeanQueue() / simulationTime;
            this.d2MeanQueue = d2.getMeanQueue() / simulationTime;
            this.d3MeanQueue = d3.getMeanQueue() / simulationTime;

            int d1Total = d1.getQuantity() + d1.getFailure();
            int d2Total = d2.getQuantity() + d2.getFailure();
            int d3Total = d3.getQuantity() + d3.getFailure();
            this.d1FailProb = d1Total > 0 ? d1.getFailure() / (double) d1Total : 0.0;
            this.d2FailProb = d2Total > 0 ? d2.getFailure() / (double) d2Total : 0.0;
            this.d3FailProb = d3Total > 0 ? d3.getFailure() / (double) d3Total : 0.0;

            this.d1QueueLeft = d1.getQueue().size();
            this.d2QueueLeft = d2.getQueue().size();
            this.d3QueueLeft = d3.getQueue().size();
//
//            this.d1Util = (d1.getBusyTime() / simulationTime) * 100;
//            this.d2Util = (d2.getBusyTime() / simulationTime) * 100;
//            this.d3Util = (d3.getBusyTime() / simulationTime) * 100;

            this.d1Util = Math.min((d1.getBusyTime() / simulationTime) * 100, 100.0);
            this.d2Util = Math.min((d2.getBusyTime() / simulationTime) * 100, 100.0);
            this.d3Util = Math.min((d3.getBusyTime() / simulationTime) * 100, 100.0);

        }
    }

    private static SimulationResult runSimulationAndCollect(double paramValue,
                                                            double createDelayMean, double createDelayDev,
                                                            double taskSizeMean, double taskSizeDev,
                                                            double probIncorrect, double processSpeed,
                                                            int maxQueue, double simulationTime) {
        Element.resetIdCounter();

        Create c = new Create(createDelayMean, createDelayDev);
        c.setDistribution(Distribution.UNIFORM);
        c.setTaskSizeMean(taskSizeMean);
        c.setTaskSizeDev(taskSizeDev);
        c.setName("Create");

        Input d1 = new Input();
        Process d2 = new Process();
        Output d3 = new Output();
        d1.setName("D1");
        d2.setName("D2");
        d3.setName("D3");

        d1.setProcess_speed(processSpeed);
        d2.setProcess_speed(processSpeed);
        d3.setProcess_speed(processSpeed);

        Dispose d = new Dispose();

        c.setNextElement(d1);
        d1.setNextElement(d2);
        d2.setNextElement(d3);
        d3.setNextPossible(List.of(d1, d));
        d3.setNextPossibleProbability(List.of(probIncorrect, 1 - probIncorrect));

        d1.setMaxQueue(maxQueue);
        d2.setMaxQueue(maxQueue);
        d3.setMaxQueue(maxQueue);

        ArrayList<Element> list = new ArrayList<>(List.of(c, d1, d2, d3));
        Model model = new Model(list, false);
        model.simulate(simulationTime);

        return new SimulationResult(paramValue, c, d1, d2, d3, simulationTime);
    }

    private static void printConsolidatedTable(String paramName, double[] testValues, List<SimulationResult> results) {
        System.out.println("\n┌" + "─".repeat(150) + "┐");
        System.out.println("│ CONSOLIDATED RESULTS FOR " + paramName);
        System.out.println("├" + "─".repeat(150) + "┤");

        // Header
        System.out.println("│ " + String.format("%-12s | %-10s | %-35s | %-35s | %-35s",
            paramName, "Created", "D1 (Input)", "D2 (Process)", "D3 (Output)"));
        System.out.println("│ " + String.format("%-12s | %-10s | %-35s | %-35s | %-35s",
            "", "Tasks", "Proc|MeanQ|Fail%|QLft|Util%", "Proc|MeanQ|Fail%|QLft|Util%", "Proc|MeanQ|Fail%|QLft|Util%"));
        System.out.println("├" + "─".repeat(150) + "┤");

        // Data rows
        for (SimulationResult r : results) {
            String d1Stats = String.format("%4d|%5.2f|%5.2f|%4d|%5.1f",
                r.d1Processed, r.d1MeanQueue, r.d1FailProb * 100, r.d1QueueLeft, r.d1Util);
            String d2Stats = String.format("%4d|%5.2f|%5.2f|%4d|%5.1f",
                r.d2Processed, r.d2MeanQueue, r.d2FailProb * 100, r.d2QueueLeft, r.d2Util);
            String d3Stats = String.format("%4d|%5.2f|%5.2f|%4d|%5.1f",
                r.d3Processed, r.d3MeanQueue, r.d3FailProb * 100, r.d3QueueLeft, r.d3Util);

            System.out.println("│ " + String.format("%-12.2f | %-10d | %-35s | %-35s | %-35s",
                r.paramValue, r.createdTasks, d1Stats, d2Stats, d3Stats));
        }

        System.out.println("└" + "─".repeat(150) + "┘\n");
    }

    private static void printConsolidatedTableInt(String paramName, int[] testValues, List<SimulationResult> results) {
        System.out.println("\n┌" + "─".repeat(150) + "┐");
        System.out.println("│ CONSOLIDATED RESULTS FOR " + paramName);
        System.out.println("├" + "─".repeat(150) + "┤");

        // Header
        System.out.println("│ " + String.format("%-12s | %-10s | %-35s | %-35s | %-35s",
            paramName, "Created", "D1 (Input)", "D2 (Process)", "D3 (Output)"));
        System.out.println("│ " + String.format("%-12s | %-10s | %-35s | %-35s | %-35s",
            "", "Tasks", "Proc|MeanQ|Fail%|QLft|Util%", "Proc|MeanQ|Fail%|QLft|Util%", "Proc|MeanQ|Fail%|QLft|Util%"));
        System.out.println("├" + "─".repeat(150) + "┤");

        // Data rows
        for (SimulationResult r : results) {
            String d1Stats = String.format("%4d|%5.2f|%5.2f|%4d|%5.1f",
                r.d1Processed, r.d1MeanQueue, r.d1FailProb * 100, r.d1QueueLeft, r.d1Util);
            String d2Stats = String.format("%4d|%5.2f|%5.2f|%4d|%5.1f",
                r.d2Processed, r.d2MeanQueue, r.d2FailProb * 100, r.d2QueueLeft, r.d2Util);
            String d3Stats = String.format("%4d|%5.2f|%5.2f|%4d|%5.1f",
                r.d3Processed, r.d3MeanQueue, r.d3FailProb * 100, r.d3QueueLeft, r.d3Util);

            System.out.println("│ " + String.format("%-12d | %-10d | %-35s | %-35s | %-35s",
                (int)r.paramValue, r.createdTasks, d1Stats, d2Stats, d3Stats));
        }

        System.out.println("└" + "─".repeat(150) + "┘\n");
    }
}
