package org.example.smo_universal;

import org.example.smo_universal.simsimple.Distribution;
import org.example.smo_universal.simsimple.FunRand;

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
        distribution = Distribution.EXP;
        tcurr = tnext;
        state = 0;
        nextElement = null;
        id = nextId;
        nextId++;
        name = "element" + id;
    }

    public Element(double delay) {
        name = "anonymus";
        tnext = 0.0;
        delayMean = delay;
        distribution = Distribution.EXP;
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
        distribution = Distribution.EXP;
        tcurr = tnext;
        state = 0;
        nextElement = null;
        id = nextId;
        nextId++;
        name = "element" + id;
    }

    public double getDelay() {
        double delay = getDelayMean();
        if (Distribution.EXP == getDistribution()) {
            delay = FunRand.Exp(getDelayMean());
        } else {
            if (Distribution.NORMAL == getDistribution()) {
                delay = FunRand.Norm(getDelayMean(),
                        getDelayDev());
            } else {
                if (Distribution.UNIFORM == getDistribution()) {
                    delay = FunRand.Unif(getDelayMean(),
                            getDelayDev());
                } else {
                    if (Distribution.ERLAND == getDistribution()) {
                        int k = (int) getDelayDev();
                        delay = FunRand.Erlang(getDelayMean(), k);
                    } else {
                        // distribution unknown
                        delay = getDelayMean();
                    }
                }
            }
        }
        return delay;
    }

    public double getDelayDev() {
        return delayDev;
    }

    public void setDelayDev(double delayDev) {
        this.delayDev = delayDev;
    }

    public Distribution getDistribution() {
        return distribution;
    }

    public void setDistribution(Distribution distribution) {
        this.distribution = distribution;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTcurr() {
        return tcurr;
    }

    public void setTcurr(double tcurr) {
        this.tcurr = tcurr;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Element getNextElement() {
        return nextElement;
    }

    public void setNextElement(Element nextElement) {
        this.nextElement = nextElement;
    }

    public void inAct() {
    }

    public void outAct() {
        quantity++;
    }

    public double getTnext() {
        return tnext;
    }

    public void setTnext(double tnext) {
        this.tnext = tnext;
    }

    public double getDelayMean() {
        return delayMean;
    }

    public void setDelayMean(double delayMean) {
        this.delayMean = delayMean;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void printResult() {
        System.out.println(getName() + " quantity = " + quantity);
    }

    public void printInfo() {
        System.out.println(getName() + " state= " + state +
                " quantity = " + quantity +
                " tnext= " + tnext);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void doStatistics(double delta) {
    }
}