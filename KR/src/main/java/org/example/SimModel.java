package org.example;

import org.example.simsimple.Distribution;

import java.util.ArrayList;
import java.util.List;


public class SimModel {
    private static final double CREATE_DELAY_DEV = 10; // 100
    private static final double CREATE_DELAY_MEAN = 10; // 300
    private static final double SIMULATION_TIME = 50_000;
    private static final double TASK_SIZE_MEAN = 500;
    private static final double TASK_SIZE_DEV = 200;
    private static final double PROBABILITY_OF_INCORRECT_TASK_PROCESSING = 0.05;
    private static final double PROCESS_SPEED = 0.5;

    public static void main(String[] args) {
        Create c = new Create(CREATE_DELAY_MEAN, CREATE_DELAY_DEV);
        c.setDistribution(Distribution.UNIFORM);
        c.setTaskSizeMean(TASK_SIZE_MEAN);
        c.setTaskSizeDev(TASK_SIZE_DEV);
        c.setName("Create");

        Process d1 = new Process();
        Process d2 = new Process();
        Process d3 = new Process();
        d1.setName("D1");
        d2.setName("D2");
        d3.setName("D3");

        d1.setProcess_speed(PROCESS_SPEED);
        d2.setProcess_speed(PROCESS_SPEED);
        d3.setProcess_speed(PROCESS_SPEED);

        Dispose d = new Dispose();

        // routes
        c.setNextElement(d1);
        d1.setNextElement(d2);
        d2.setNextElement(d3);
        d3.setNextPossible(List.of(d1, d));
        d3.setNextPossibleProbability(List.of(PROBABILITY_OF_INCORRECT_TASK_PROCESSING, 1 - PROBABILITY_OF_INCORRECT_TASK_PROCESSING));

        // queue size
        d1.setMaxQueue(3);
        d2.setMaxQueue(3);
        d3.setMaxQueue(3);

        ArrayList<Element> list = new ArrayList<>(List.of(c, d1, d2, d3));
        Model model = new Model(list);
        model.simulate(SIMULATION_TIME);

//        Create c = new Create(2.0);
//        Process p = new Process(1.0);
//        System.out.println("id0 = " + c.getId() + " id1=" + p.getId());
//        c.setNextElement(p);
//        p.setMaxQueue(5);
//        c.setName("CREATOR");
//        p.setName("PROCESSOR");
//        c.setDistribution("exp");
//        p.setDistribution("exp");
//        ArrayList<Element> list = new ArrayList<>();
//        list.add(c);
//        list.add(p);
//        Model model = new Model(list);
//        model.simulate(1000.0);
    }
}
