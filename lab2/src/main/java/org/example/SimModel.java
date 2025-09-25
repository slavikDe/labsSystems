package org.example;

import java.util.ArrayList;

public class SimModel {
    public static void main(String[] args) {
        double creatorDelay = 2.0;
        double processorDelay = 1.0;
        int processCount = 3;
        int maxQueueSize = 3;
        int numDevices = 3;
        double simulationTime = 1000.0;
        String distributionType = "exp";

        Create c = new Create(creatorDelay);
        c.setName("CREATOR");
        c.setDistribution(distributionType);

        ArrayList<Element> list = new ArrayList<>();
        list.add(c);

        Process firstProc = null;
        for (int i = 0; i < processCount; i++) {
            Process p = new Process(processorDelay, numDevices);
            p.setName("PROCESSOR" + (i + 1));
            p.setMaxQueue(maxQueueSize);
            p.setDistribution(distributionType);

            if (i == 0) {
                firstProc = p;
                Process.setFirstProcess(firstProc); // Set reference to first process
            }

            list.getLast().setNextElement(p);
            list.add(p);
        }

        Model model = new Model(list);
        model.simulate(simulationTime);
    }
}
