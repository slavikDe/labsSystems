package org.example;

public class Process extends Element {
    private int queue, maxQueue, failure;
    private double meanQueue;
    private int deviceCount;
    private double totalDeviceTime;
    private static Process firstProcess; // Reference to first process in chain
    private int redirectedCount; // Count of elements redirected to first process

    public Process(double delay) {
        super(delay);
        queue = 0;
        maxQueue = Integer.MAX_VALUE;
        meanQueue = 0.0;
        deviceCount = 1;
        totalDeviceTime = 0.0;
        redirectedCount = 0;
    }

    public Process(double delay, int deviceCount) {
        super(delay);
        queue = 0;
        maxQueue = Integer.MAX_VALUE;
        meanQueue = 0.0;
        this.deviceCount = deviceCount;
        totalDeviceTime = 0.0;
        redirectedCount = 0;
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
        if (getQueue() > 0) {
            setQueue(getQueue() - 1);
            setState(getState() + 1);
            super.setTnext(super.getTcurr() + super.getDelay());
        } else {
            super.setTnext(Double.MAX_VALUE);
        }
        // Route element: 2% back to process1, 98% forward
        if (Math.random() < 0.02 && getFirstProcess() != null && this != getFirstProcess()) {
            redirectedCount++;
            getFirstProcess().inAct();
        } else if (super.getNextElement() != null) {
            super.getNextElement().inAct();
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
        System.out.println("redirected = " + redirectedCount);
    }

    @Override
    public void doStatistics(double delta) {
        meanQueue = getMeanQueue() + queue * delta;
        // Only count positive device states (devices actually in use)
        int actualDevicesInUse = Math.max(0, Math.min(getState(), deviceCount));
        totalDeviceTime += actualDevicesInUse * delta;
    }

    public double getDeviceUtilization(double totalTime) {
        return totalDeviceTime / (deviceCount * totalTime) * 100.0;
    }

    public double getMeanQueue() {
        return meanQueue;
    }

    public static void setFirstProcess(Process process) {
        firstProcess = process;
    }

    public static Process getFirstProcess() {
        return firstProcess;
    }

    public int getRedirectedCount() {
        return redirectedCount;
    }
}