package org.example.HospitalSMO;

import org.example.smo_universal.Element;
import org.example.smo_universal.Process;

import java.util.ArrayList;
import java.util.List;

public class HospitalProcess extends Process {
    private int currentPatientType = 1;
    private boolean variableDelayByType = false;
    private boolean usePriorityQueue = false;
    private double currentPatientArrivalTime = 0.0;

    // Priority queue: store patient types waiting in queue
    private final List<Integer> queuedPatientTypes = new ArrayList<>();

    // Delay times by patient type for D1
    private static final double TYPE_1_DELAY = 15.0;
    private static final double TYPE_2_DELAY = 40.0;
    private static final double TYPE_3_DELAY = 30.0;

    public HospitalProcess() {
        super();
    }

    public void setCurrentPatientArrivalTime(double arrivalTime) {
        this.currentPatientArrivalTime = arrivalTime;
    }

    public double getCurrentPatientArrivalTime() {
        return currentPatientArrivalTime;
    }

    public void setUsePriorityQueue(boolean enable) {
        this.usePriorityQueue = enable;
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
        // Check if patient will go into queue BEFORE calling super.inAct()
        boolean willQueue = super.getState() >= getDevices();

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

        // AFTER super.inAct(), check if patient was actually queued
        // Store patient type in priority queue when they enter queue
        if (usePriorityQueue && willQueue && getQueue() > queuedPatientTypes.size()) {
            queuedPatientTypes.add(currentPatientType);
        }
    }

    /**
     * Get next patient from queue with priority logic:
     * - Search for Type 1 first
     * - If no Type 1, take first in queue (FIFO)
     */
    private Integer getNextPatientFromQueue() {
        if (queuedPatientTypes.isEmpty()) {
            return null;
        }

        // Search for Type 1 first
        for (int i = 0; i < queuedPatientTypes.size(); i++) {
            if (queuedPatientTypes.get(i) == 1) {
                return queuedPatientTypes.remove(i);
            }
        }

        // No Type 1 found, take first from queue (FIFO)
        return queuedPatientTypes.remove(0);
    }

    @Override
    public void outAct() {
        // If using priority queue and there's a queue, select next patient by priority
        if (usePriorityQueue && getQueue() > 0 && !queuedPatientTypes.isEmpty()) {
            Integer nextPatientType = getNextPatientFromQueue();
            if (nextPatientType != null) {
                currentPatientType = nextPatientType;
            }
        }

        // Call parent outAct - this will route and call next element's inAct()
        // We need to set patient type on next element BEFORE super.outAct() is called
        // But super.outAct() selects the next element via routing strategy
        // So we override the routing flow to propagate type correctly

        super.outAct();
    }

    @Override
    protected void propagateToNextElement(Element targetElement) {
        // Set patient type and arrival time before calling inAct on target
        if (targetElement instanceof HospitalProcess hp) {
            hp.setCurrentPatientType(currentPatientType);
            hp.setCurrentPatientArrivalTime(currentPatientArrivalTime);
        } else if (targetElement instanceof HospitalElement he) {
            he.setType(currentPatientType);
        }

        // Now call inAct on the target with correct type
        if (targetElement != null) {
            targetElement.inAct();
        }
    }
}
