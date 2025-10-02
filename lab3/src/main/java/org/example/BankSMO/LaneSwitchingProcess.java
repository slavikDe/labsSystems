package org.example.BankSMO;

import org.example.smo_universal.Process;

public class LaneSwitchingProcess extends Process {
    private LaneSwitchingProcess otherLane;
    private int laneSwitches = 0;

    public LaneSwitchingProcess(double delay) {
        super(delay);
    }

    public void setOtherLane(LaneSwitchingProcess otherLane) {
        this.otherLane = otherLane;
    }

    @Override
    public void inAct() {
        // First, add to this lane's queue
        if (super.getState() < 1) {
            super.setState(super.getState() + 1);
            super.setTnext(super.getTcurr() + super.getDelay());
        } else {
            if (getQueue() < getMaxQueue()) {
                setQueue(getQueue() + 1);

                // Check if customer should switch lanes
                // Customer can switch if they are last in queue and difference >= 2
                if (otherLane != null && shouldSwitchLane()) {
                    // Remove from this lane
                    setQueue(getQueue() - 1);

                    // Add to other lane directly
                    otherLane.addToQueue();

                    // Increment switch counter
                    laneSwitches++;
                }
            } else {
                // Queue is full, reject
                super.inAct(); // This will increment failure counter in parent
            }
        }
    }

    private boolean shouldSwitchLane() {
        if (otherLane == null) {
            return false;
        }

        int myQueue = getQueue();
        int otherQueue = otherLane.getQueue();

        // Can switch if I'm last in queue (just added) and difference >= 2
        return myQueue - otherQueue >= 2;
    }

    private void addToQueue() {
        // Directly add to queue without switching check
        if (getQueue() < getMaxQueue()) {
            setQueue(getQueue() + 1);
        }
    }

    public int getLaneSwitches() {
        return laneSwitches;
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("lane switches = " + laneSwitches);
    }
}
