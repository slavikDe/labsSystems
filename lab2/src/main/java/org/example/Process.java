package org.example;

public class Process extends Element {
    private int queue, maxQueue, failure;
    private double meanQueue;
    private int deviceCount;
    private double totalDeviceTime;

    public Process(double delay) {
        super(delay);
        queue = 0;
        maxQueue = Integer.MAX_VALUE;
        meanQueue = 0.0;
        deviceCount = 1;
        totalDeviceTime = 0.0;
    }

    public Process(double delay, int deviceCount) {
        super(delay);
        queue = 0;
        maxQueue = Integer.MAX_VALUE;
        meanQueue = 0.0;
        this.deviceCount = deviceCount;
        totalDeviceTime = 0.0;
    }

    @Override
    public void inAct() {
        if(deviceCount > getState()){
            setState(getState() + 1); // take one device
            super.setTnext(super.getTcurr() + super.getDelay());
        }
        else {
            if (getQueue() < getMaxQueue()) {
                setQueue(getQueue() + 1);
            } else {
                failure++;
            }
        }
    }

    @Override
    public void outAct() {
        super.outAct();
        setState(getState() - 1); // free one device
        if (super.getNextElement() != null) {
            super.getNextElement().inAct();
        }
        if (getQueue() > 0) {
            setQueue(getQueue() - 1);
            setState(getState() + 1);
            super.setTnext(super.getTcurr() + super.getDelay());
        } else {
            // No queue - set to idle
            super.setTnext(Double.MAX_VALUE);
        }
    }

    public int getFailure() {
        return failure;
    }

    public int getQueue() {
        return queue;
    }

    public void setQueue(int queue) {
        this.queue = queue;
    }

    public int getMaxQueue() {
        return maxQueue;
    }

    public void setMaxQueue(int maxQueue) {
        this.maxQueue = maxQueue;
    }

    public int getDeviceCount() {
        return deviceCount;
    }

    public void setDeviceCount(int deviceCount) {
        this.deviceCount = deviceCount;
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("failure = " + this.getFailure());
        System.out.println("devices in use = " + getState() + "/" + deviceCount);
    }

    @Override
    public void doStatistics(double delta) {
        meanQueue = getMeanQueue() + queue * delta;
        totalDeviceTime += getState() * delta; // Track total device usage time
    }

    public double getDeviceUtilization(double totalTime) {
        return totalDeviceTime / (deviceCount * totalTime) * 100.0;
    }

    public double getMeanQueue() {
        return meanQueue;
    }
}