    package org.example;

import org.example.model_parts.Element;
import org.example.model_parts.Process;

import java.util.ArrayList;

public class Model {
    private final ArrayList<Element> list;
    double tnext, tcurr;
    int event;
    private boolean verbose = true;
    private double globalTime;

    public Model(ArrayList<Element> elements) {
        list = elements;
        tnext = 0.0;
        event = 0;
        tcurr = tnext;
    }

    public Model(ArrayList<Element> elements, boolean verbose) {
        list = elements;
        tnext = 0.0;
        event = 0;
        tcurr = tnext;
        this.verbose = verbose;
    }

    public void simulate(double time) {
        globalTime = time;
        while (tcurr < globalTime) {
            tnext = Double.MAX_VALUE;
            for (Element e : list) {
                if (e.getTnext() < tnext) {
                    tnext = e.getTnext();
                    event = e.getId();
                }
            }
            if (verbose) {
                System.out.println("\nIt's time for event in " +
                        list.get(event).getName() +
                        ", time = " + tnext);
            }
            for (Element e : list) {
                e.doStatistics(tnext - tcurr);
            }
            tcurr = tnext;
            for (Element e : list) {
                e.setTcurr(tcurr);
            }
            list.get(event).outAct();
            for (Element e : list) {
                if (e.getTnext() == tcurr) {
                    e.outAct();
                }
            }
            if (verbose) {
                printInfo();
            }
        }
        if (verbose) {
            printResult();
        }
    }

    public void printInfo() {
        for (Element e : list) {
            e.printInfo();
        }
    }

    public void printResult() {
        System.out.println("\n-------------RESULTS-------------");
        for (Element e : list) {
            e.printResult();
            if (e instanceof Process p) {
                System.out.println("mean length of queue = " +
                        p.getMeanQueue() / tcurr
                        + "\nfailure probability = " +
                        p.getFailure() / (double) p.getQuantity());

                System.out.println("Util%: " + p.getBusyTime() / globalTime * 100);
                System.out.println("In queue left: " + p.getQueue().size() + '\n');
            }
        }
    }
}