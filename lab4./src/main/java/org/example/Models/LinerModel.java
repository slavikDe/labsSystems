package org.example.Models;

import org.example.smo_universal.*;
import org.example.smo_universal.Process;

import java.util.ArrayList;

public class LinerModel extends Model {

    public LinerModel(int nSystems, int nDevice, boolean verbose) {
        super(nSystems, nDevice, verbose);
    }

    @Override
    public void addRoutes() {
        ArrayList<Element> stg = super.stages;
        for(int i = 0; i < stg.size() - 1; i++){
            stg.get(i).setNextElement(stg.get(i+1));
        }
    }
}
