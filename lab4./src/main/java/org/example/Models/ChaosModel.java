package org.example.Models;


import org.example.smo_universal.Element;
import org.example.smo_universal.Process;

import java.util.ArrayList;
import java.util.Random;

public class ChaosModel extends Model {
    public ChaosModel(int nSystems, int nDevice, boolean verbose) {
        super(nSystems, nDevice, verbose);
    }

    @Override
    public void addRoutes() {
        ArrayList<Element> stg = super.stages;

        for(int i = 0; i < stg.size() - 1; i++){
            stg.get(i).setNextElement(stg.get(i + 1));
        }

        for(int i = 2; i < stg.size() - 1; i += 2){
            Process process = (Process) stg.get(i);

            process.getNextPossible().add(stg.get(i - 1));
            process.getNextPossibleProbability().add(0.2);

            // 80% chance to go forward to next stage
            process.getNextPossible().add(stg.get(i + 1));
            process.getNextPossibleProbability().add(0.8);
        }
    }
}
