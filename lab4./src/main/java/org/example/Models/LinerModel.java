package org.example.Models;

import org.example.smo_universal.*;
import org.example.smo_universal.Process;

import java.util.ArrayList;

public class LinerModel implements Model {
    private final int numberSystems;
    private MMOModel mmoModel;

    private boolean verbose = true;

    public LinerModel(int nSystems){
        this.numberSystems = nSystems;
    }
    public LinerModel(int nSystems, boolean verbose){
        this.numberSystems = nSystems;
        this.verbose = verbose;
    }

    @Override
    public void initialize() {
        Element.resetIdCounter();
        Create create = new Create();
        ArrayList<Element> stages = new ArrayList<>();
        stages.add(create);

        for(int i = 0; i < numberSystems; i++){
            Process process = new Process();
            stages.get(i).setNextElement(process);
            stages.add(process);
        }

        Dispose dispose = new Dispose();
        stages.get(numberSystems).setNextElement(dispose);

        mmoModel = new MMOModel(stages);
    }

    @Override
    public void go(double simulationTime) {
        mmoModel.simulate(simulationTime, verbose);
    }


}
