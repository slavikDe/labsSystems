package org.example.HospitalSMO;

import org.example.smo_universal.Create;
import org.example.smo_universal.Element;

public class HospitalCreate extends Create {
    // Patient type probabilities: Type 1: 0.5, Type 2: 0.1, Type 3: 0.4
    private static final double TYPE_1_PROB = 0.5;
    private static final double TYPE_2_PROB = 0.1;

    private int type1Count = 0;
    private int type2Count = 0;
    private int type3Count = 0;

    public HospitalCreate(double delay) {
        super(delay);
    }

    public int getType1Count() {
        return type1Count;
    }

    public int getType2Count() {
        return type2Count;
    }

    public int getType3Count() {
        return type3Count;
    }

    @Override
    public void outAct() {
        // Generate patient type based on probabilities
        double rand = Math.random();
        int patientType;

        if (rand < TYPE_1_PROB) {
            patientType = 1;
            type1Count++;
        } else if (rand < TYPE_1_PROB + TYPE_2_PROB) {
            patientType = 2;
            type2Count++;
        } else {
            patientType = 3;
            type3Count++;
        }

        // Call parent outAct
        super.outAct();

        // Set type and arrival time on the next element
        Element next = super.getNextElement();
        if (next instanceof HospitalElement hospitalElement) {
            hospitalElement.setType(patientType);
        } else if (next instanceof HospitalProcess hospitalProcess) {
            hospitalProcess.setCurrentPatientType(patientType);
            hospitalProcess.setCurrentPatientArrivalTime(getTcurr());
        }
    }
}
