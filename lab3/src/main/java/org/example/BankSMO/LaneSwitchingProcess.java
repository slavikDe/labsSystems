package org.example.BankSMO;

import lombok.Getter;
import org.example.smo_universal.Process;

public class LaneSwitchingProcess extends Process {
    private LaneSwitchingProcess otherLane;
    @Getter
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
                if (otherLane != null && shouldSwitchLane()) {
                    setQueue(getQueue() - 1);
                    otherLane.addToQueue();

                    laneSwitches++;
                }
            } else {
                super.inAct();
            }
        }
    }

    private boolean shouldSwitchLane() {
        if (otherLane == null) {
            return false;
        }

        int myQueue = getQueue();
        int otherQueue = otherLane.getQueue();

        return myQueue - otherQueue >= 2;
    }

    private void addToQueue() {
        if (getQueue() < getMaxQueue()) {
            setQueue(getQueue() + 1);
        }
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("lane switches = " + laneSwitches);
    }
}
