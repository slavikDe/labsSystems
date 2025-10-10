package org.example.smo_universal;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class Process extends Element {
    private final List<Element> possibleRoutes = new ArrayList<>();

    private RoutingStrategy<Element> routingStrategy;
    private int queue, maxQueue, failure;
    private double meanQueue;
    private double totalDeviceTime = 0.0;

    private int devices = 1;

    public Process() {
        this.queue = 0;
        this.maxQueue = Integer.MAX_VALUE;
        this.meanQueue = 0.0;
    }

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

    /**
     * Set possible routes for multi-route output
     * @param elements possible next elements
     */
    public final void setPossibleRoutes(Element... elements) {
        possibleRoutes.clear();
        possibleRoutes.addAll(Arrays.asList(elements));
    }

    @Override
    public void inAct() {
        if (super.getState() < devices) {
            super.setState(super.getState() + 1);
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
        super.setState(super.getState() - 1);
        if (getQueue() > 0) {
            setQueue(getQueue() - 1);
            super.setState(super.getState() + 1);
            super.setTnext(super.getTcurr() + super.getDelay());
        } else {
            super.setTnext(Double.MAX_VALUE);
        }

        // Multi-route logic: select next element using routing strategy
        Element targetElement = null;

        if (possibleRoutes.size() == 1) {
            // Single route - use it directly
            targetElement = possibleRoutes.getFirst();
        } else if (possibleRoutes.size() > 1 && routingStrategy != null) {
            // Multiple routes - use routing strategy to select
            targetElement = routingStrategy.selectNext(possibleRoutes, this);
        } else if (super.getNextElement() != null) {
            // Fallback to old single nextElement
            targetElement = super.getNextElement();
        }

        // Allow subclasses to customize propagation (e.g., set patient type)
        propagateToNextElement(targetElement);
    }

    /**
     * Hook method for subclasses to customize how elements are propagated.
     * Default behavior: simply call inAct() on target.
     * Subclasses can override to add custom logic (e.g., setting patient type).
     */
    protected void propagateToNextElement(Element targetElement) {
        if (targetElement != null) {
            targetElement.inAct();
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
    public void doStatistics(double delta) {
        meanQueue = getMeanQueue() + queue * delta;
        int devicesInUse = Math.min(super.getState(), devices);
        totalDeviceTime += devicesInUse * delta;
    }

    public double getMeanQueue() {
        return meanQueue;
    }

    public double getDeviceUtilization(double totalTime) {
        return (totalDeviceTime / (devices * totalTime)) * 100.0;
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("failure = " + this.getFailure());
        System.out.println("devices in use = " + super.getState() + "/" + devices);
    }
}
