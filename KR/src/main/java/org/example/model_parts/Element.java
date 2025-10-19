package org.example.model_parts;

import lombok.Getter;
import lombok.Setter;
import org.example.simsimple.Distribution;
import org.example.simsimple.FunRand;

@Getter
@Setter
public class Element {
    private static int nextId = 0;
    private String name;
    private double tnext;
    private double delayMean, delayDev;
    private Distribution distribution;
    private int quantity;
    private double tcurr;
    private int state;
    private Element nextElement;

    private int id;

    public Element() {
        tnext = Double.MAX_VALUE;
        delayMean = 1.0;
        distribution = Distribution.EXPONENTIAL;
        tcurr = tnext;
        state = 0;
        nextElement = null;
        id = nextId;
        nextId++;
        name = "element" + id;
    }

    public Element(double delay) {
        name = "anonymous";
        tnext = 0.0;
        delayMean = delay;
        distribution = Distribution.EXPONENTIAL;
        tcurr = tnext;
        state = 0;
        nextElement = null;
        id = nextId;
        nextId++;
        name = "element" + id;
    }

    public Element(String nameOfElement, double delay) {
        name = nameOfElement;
        tnext = 0.0;
        delayMean = delay;
        distribution = Distribution.EXPONENTIAL;
        tcurr = tnext;
        state = 0;
        nextElement = null;
        id = nextId;
        nextId++;
        name = "element" + id;
    }

    public double getDelay() {
        double delay;
        switch (getDistribution()) {
            case EXPONENTIAL -> delay = FunRand.Exp(getDelayMean());
            case NORMAL -> delay = FunRand.Norm(getDelayMean(), getDelayDev());
            case UNIFORM ->  delay = FunRand.Unif(getDelayMean() - getDelayDev(), getDelayMean() + getDelayDev());
            default ->  delay = getDelayMean();
        }
      
        return delay;
    }

    public void inAct() { }

    public void outAct() {
        quantity++;
    }

    public void printResult() {
        System.out.println(getName() + " quantity = " + quantity);
    }

    public void printInfo() {
        System.out.println(getName() + " state= " + state +
                " quantity = " + quantity +
                " tnext= " + tnext);
    }

    public void doStatistics(double delta) {
    }

    public static void resetIdCounter() {
        nextId = 0;
    }

}