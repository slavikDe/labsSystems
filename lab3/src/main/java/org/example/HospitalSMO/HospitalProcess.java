package org.example.HospitalSMO;

import org.example.smo_universal.Element;
import org.example.smo_universal.Process;

public class HospitalProcess extends Process {
    private int currentPatientType = 1;
    private boolean variableDelayByType = false;

    // Delay times by patient type for D1
    private static final double TYPE_1_DELAY = 15.0;
    private static final double TYPE_2_DELAY = 40.0;
    private static final double TYPE_3_DELAY = 30.0;

    public HospitalProcess() {
        super();
    }

    /**
     * Enable variable delay based on patient type (for D1)
     */
    public void setVariableDelayByType(boolean enable) {
        this.variableDelayByType = enable;
    }

    public void setCurrentPatientType(int type) {
        this.currentPatientType = type;
    }

    public int getCurrentPatientType() {
        return currentPatientType;
    }

    @Override
    public void inAct() {
        // If variable delay is enabled, override delayMean based on type
        if (variableDelayByType) {
            double originalDelay = getDelayMean();
            switch (currentPatientType) {
                case 1 -> setDelayMean(TYPE_1_DELAY);
                case 2 -> setDelayMean(TYPE_2_DELAY);
                case 3 -> setDelayMean(TYPE_3_DELAY);
            }
            super.inAct();
            setDelayMean(originalDelay); // Restore original
        } else {
            super.inAct();
        }
    }

    @Override
    public void outAct() {
        super.outAct();

        // Propagate patient type to next element
        Element next = getNextElement();
        if (next instanceof HospitalElement hospitalNext) {
            hospitalNext.setType(currentPatientType);
        } else if (next instanceof HospitalProcess hospitalProcess) {
            hospitalProcess.setCurrentPatientType(currentPatientType);
        }
    }
}
