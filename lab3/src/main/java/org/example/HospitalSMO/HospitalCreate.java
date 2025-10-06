package org.example.HospitalSMO;

import org.example.smo_universal.Create;
import org.example.smo_universal.Element;

public class HospitalCreate extends Create {
    // Patient type probabilities: Type 1: 0.5, Type 2: 0.1, Type 3: 0.4
    private static final double TYPE_1_PROB = 0.5;
    private static final double TYPE_2_PROB = 0.1;

    public HospitalCreate(double delay) {
        super(delay);
    }

    @Override
    public void outAct() {
        // Generate patient type based on probabilities
        double rand = Math.random();
        int patientType;

        if (rand < TYPE_1_PROB) {
            patientType = 1;
        } else if (rand < TYPE_1_PROB + TYPE_2_PROB) {
            patientType = 2;
        } else {
            patientType = 3;
        }

        // Call parent outAct
        super.outAct();

        // Set type on the next element
        Element next = super.getNextElement();
        if (next instanceof HospitalElement hospitalElement) {
            hospitalElement.setType(patientType);
        } else if (next instanceof HospitalProcess hospitalProcess) {
            hospitalProcess.setCurrentPatientType(patientType);
        }
    }
}
