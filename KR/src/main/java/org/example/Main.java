package org.example;


import org.example.parts.*;
import org.example.util.Distribution;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final double SIMULATION_TIME = 1_000_000;
    private static final double ARRIVAL_DELAY_MEAN = 300; // 300
    private static final double ARRIVAL_DELAY_DEV = 100; // 100

    private static final double TASK_SIZE_MEAN = 500;
    private static final double TASK_SIZE_DEV = 200; // 200

    private static final double PROBABILITY_OF_INCORRECT_TASK_PROCESSING = 0.05;
    private static final double PROCESS_SPEED = 0.5;

    private static final int INPUT_DEVICES = 4;
    private static final int PROCESS_DEVICES = 4;
    private static final int OUTPUT_DEVICES = 4;

    public static void main(String[] args){
        ArrivalPoint arrivalPoint = new ArrivalPoint("Arrival");
        arrivalPoint.setDelayMean(ARRIVAL_DELAY_MEAN);
        arrivalPoint.setDelayDev(ARRIVAL_DELAY_DEV);
        arrivalPoint.setTask_dev(TASK_SIZE_DEV);
        arrivalPoint.setTask_mean(TASK_SIZE_MEAN);
        arrivalPoint.setDistribution(Distribution.UNIFORM);

        Stage input = new Stage("Input", PROCESS_SPEED);
        Stage process = new Stage("Process", PROCESS_SPEED);
        Stage output = new Stage("Output", PROCESS_SPEED);

        Dispose dispose = new Dispose();

        input.setServers(INPUT_DEVICES);
        process.setServers(PROCESS_DEVICES);
        output.setServers(OUTPUT_DEVICES);

        input.setQueueCapacity(5);
        process.setQueueCapacity(5);
        output.setQueueCapacity(5);

        // routes
        arrivalPoint.setNextElement(input);
        input.setNextElement(process);
        process.setNextElement(output);
        output.addNextWithProbabilities(
                List.of(input, dispose),
                List.of(PROBABILITY_OF_INCORRECT_TASK_PROCESSING, 1 - PROBABILITY_OF_INCORRECT_TASK_PROCESSING)
        );

        ArrayList<Element> elements = new ArrayList<>(List.of(
                arrivalPoint,
                input,
                process,
                output
        ));

        ProcessingModel model = new ProcessingModel(elements, true);
        model.simulate(SIMULATION_TIME);
    }

    public static void testQueue() {
        int q1Size = 1, q2Size = 1, q3Size = 1;

        while (q1Size != 10 &&  q2Size != 10 && q3Size != 10) {
            createModelWithDiffQueues(q1Size, q2Size, q3Size, false);
            q1Size++;
            createModelWithDiffQueues(q1Size, q2Size, q3Size, false);
            q2Size++;
            createModelWithDiffQueues(q1Size, q2Size, q3Size, false);
            q3Size++;
        }
    }

    private static void createModelWithDiffQueues(int q1Size, int q2Size, int q3Size, boolean verbose) {
        reset();
        ArrivalPoint arrivalPoint = new ArrivalPoint("Arrival");
        arrivalPoint.setDelayMean(ARRIVAL_DELAY_MEAN);
        arrivalPoint.setDelayDev(ARRIVAL_DELAY_DEV);
        arrivalPoint.setTask_dev(TASK_SIZE_DEV);
        arrivalPoint.setTask_mean(TASK_SIZE_MEAN);
        arrivalPoint.setDistribution(Distribution.UNIFORM);

        Stage input = new Stage("Input", PROCESS_SPEED);
        Stage process = new Stage("Process", PROCESS_SPEED);
        Stage output = new Stage("Output", PROCESS_SPEED);

        Dispose dispose = new Dispose();

        input.setServers(INPUT_DEVICES);
        process.setServers(PROCESS_DEVICES);
        output.setServers(OUTPUT_DEVICES);

        input.setQueueCapacity(q1Size);
        process.setQueueCapacity(q2Size);
        output.setQueueCapacity(q3Size);

        // routes
        arrivalPoint.setNextElement(input);
        input.setNextElement(process);
        process.setNextElement(output);
        output.addNextWithProbabilities(
                List.of(input, dispose),
                List.of(PROBABILITY_OF_INCORRECT_TASK_PROCESSING, 1 - PROBABILITY_OF_INCORRECT_TASK_PROCESSING)
        );

        ArrayList<Element> elements = new ArrayList<>(List.of(
                arrivalPoint,
                input,
                process,
                output
        ));

        ProcessingModel model = new ProcessingModel(elements, verbose);
        model.simulate(SIMULATION_TIME);
    }

    private static void reset() {
        Element.resetIdCounter();
        Dispose.resetCounter();
    }

    public static void main2(String[] args){
        createModelWithDiffQueues(4, 4, 4, true);
//        ArrivalPoint arrivalPoint = new ArrivalPoint("Arrival");
//        arrivalPoint.setDelayMean(ARRIVAL_DELAY_MEAN);
//        arrivalPoint.setDelayDev(ARRIVAL_DELAY_DEV);
//        arrivalPoint.setTask_dev(TASK_SIZE_DEV);
//        arrivalPoint.setTask_mean(TASK_SIZE_MEAN);
//        arrivalPoint.setDistribution(Distribution.UNIFORM);
//
//        Stage input = new Stage("Input", PROCESS_SPEED);
//        Stage process = new Stage("Process", PROCESS_SPEED);
//        Stage output = new Stage("Output", PROCESS_SPEED);
//
//        Dispose dispose = new Dispose();
//
//        input.setServers(INPUT_DEVICES);
//        process.setServers(PROCESS_DEVICES);
//        output.setServers(OUTPUT_DEVICES);
//
//        input.setQueueCapacity(5);
//        process.setQueueCapacity(5);
//        output.setQueueCapacity(5);
//
//        // routes
//        arrivalPoint.setNextElement(input);
//        input.setNextElement(process);
//        process.setNextElement(output);
//        output.addNextWithProbabilities(
//                List.of(input, dispose),
//                List.of(PROBABILITY_OF_INCORRECT_TASK_PROCESSING, 1 - PROBABILITY_OF_INCORRECT_TASK_PROCESSING)
//        );
//
//        ArrayList<Element> elements = new ArrayList<>(List.of(
//                arrivalPoint,
//                input,
//                process,
//                output
//        ));
//
//        ProcessingModel model = new ProcessingModel(elements, true);
//        model.simulate(SIMULATION_TIME);
    }
}
