package org.example.Runner;

import org.example.Models.Model;

public class ModelRunner {
    private final Model model;
    private final double time;
    private final int runNumber;

    public ModelRunner(Model model, double simulationTime, int runNumber) {
        this.model = model;
        this.time = simulationTime;
        this.runNumber = runNumber;
    }

    public void start() {
        for(int i = 0; i < runNumber; i++) {
            long start = System.currentTimeMillis();
            model.initialize();
            model.go(this.time);
            System.out.println("Cycle №" + (i+ 1) + ", time: " + (System.currentTimeMillis() - start) + "ms");
        }
    }
}
