package org.example.Models;

import org.example.smo_universal.*;
import org.example.smo_universal.Process;

import java.util.ArrayList;

public abstract class Model {
    private final int numSystems;
    private MMOModel mmoModel;

    private int device = 1;
    private final boolean verbose;
    protected ArrayList<Element> stages;

    public Model(int nSystems, int nDevice, boolean verbose) {
        this.numSystems = nSystems;
        this.verbose = verbose;
        this.device = nDevice;
    }

    public void initialize() {
        Element.resetIdCounter();

        Create create = new Create();
        stages = new ArrayList<>();
        stages.add(create);

        for(int i = 0; i < numSystems; i++) {
            Process process = new Process();
            process.setDevices(device);
            stages.add(process);

            process.setMaxQueue(3);
        }

        addRoutes();

        Dispose dispose = new Dispose();
        stages.getLast().setNextElement(dispose);

        mmoModel = new MMOModel(stages);
    }

    public void go(double simulationTime) {
        mmoModel.simulate(simulationTime, verbose, numSystems + 1);
    }

    abstract public void addRoutes();

}
