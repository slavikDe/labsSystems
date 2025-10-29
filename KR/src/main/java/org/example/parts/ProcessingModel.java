package org.example.parts;

import java.util.ArrayList;

public class ProcessingModel {
    private final ArrayList<Element> list;
    double tnext, tcurr;
    int event;
    private boolean verbose = true;
    private double globalTime;

    public ProcessingModel(ArrayList<Element> elements) {
        list = elements;
        tnext = 0.0;
        event = 0;
        tcurr = tnext;
    }

    public ProcessingModel(ArrayList<Element> elements, boolean verbose) {
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
                System.out.println("\nIt's time for event in " + list.get(event).getName() + ", time = " + tnext);
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
        if(verbose) {
            printResult();
        }
    }

    private void printQueueResult() {
        System.out.println();

        for (Element e : list) {
            if (e instanceof Stage stage) {
                printQueueProcessedStat(stage);
            }
        }
    }

    private void printQueueFailureStat(Stage stage) {
        Queue queue = stage.getQueue();
        int originalTasks = stage.getQuantity() - stage.getReprocessedCount();
        int totalAttempted = originalTasks + queue.getFailure();
        double failure = totalAttempted > 0 ? queue.getFailure() / (double) totalAttempted : 0.0;
        System.out.format("%-7s | Cap: %-2d | Fail: %6.4f%%  ", stage.getName(), queue.getCapacity(), failure);
    }
    private void printQueueProcessedStat(Stage stage) {
        Queue queue = stage.getQueue();
        System.out.format("%-7s | Cap: %-2d | AvgTimeInSys: %6.4f min ", stage.getName(), queue.getCapacity(), (globalTime / Dispose.counter) * 12 );
    }

    public void printInfo() {
        for (Element e : list) {
            e.printInfo();
        }
    }

    public void printResult() {
        System.out.println("\n");
        System.out.println("╔═════════════════════════════════════════════════════╗");
        System.out.println("║                   SIMULATION RESULTS                ║");
        System.out.println("╠═════════════════════════════════════════════════════╣");
        System.out.printf("║ %-30s: %18.2f  ║%n", "Simulation Time", globalTime);
        System.out.printf("║ %-30s: %18.2f  ║%n", "Average Time In System", (globalTime / Dispose.counter) * 12, "min" );
        System.out.println("╚═════════════════════════════════════════════════════╝");
        System.out.println();

        for (Element e : list) {
            if (e instanceof Stage stage) {
                Statistics.stageStats(stage, globalTime);
            } else {
                e.printResult();
            }
        }
    }
}