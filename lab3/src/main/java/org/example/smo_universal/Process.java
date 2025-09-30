package org.example.smo_universal;

public class Process extends Element {
    private int queue, maxQueue, failure;
    private double meanQueue;

    private int devices;

    public Process(double delay) {
        super(delay);
        this.queue = 0;
        this.maxQueue = Integer.MAX_VALUE;
        this.meanQueue = 0.0;
    }

    public Process(double delay, int devices) {
        super(delay);
        this.queue = 0;
        this.maxQueue = Integer.MAX_VALUE;
        this.meanQueue = 0.0;
        this.devices = devices;
    }

    @Override
    public void inAct() {
        if (super.getState() == 0) {
            super.setState(0);
            super.setTnext(super.getTcurr() + super.getDelay());
        } else {
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
        super.setTnext(Double.MAX_VALUE);
        super.setState(0);
        if (getQueue() > 0) {
            setQueue(getQueue() - 1);
            super.setState(1);
            super.setTnext(super.getTcurr() + super.getDelay());
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

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("failure = " + this.getFailure());
    }

    @Override
    public void doStatistics(double delta) {
        meanQueue = getMeanQueue() + queue * delta;
    }

    public double getMeanQueue() {
        return meanQueue;
    }
}