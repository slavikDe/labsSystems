package org.example.HospitalSMO;

import org.example.smo_universal.Element;
import org.example.smo_universal.Model;
import org.example.smo_universal.RoutingStrategy;
import org.example.smo_universal.simsimple.Distribution;

import java.util.*;

public class Hospital {
    private static final Random rand = new Random();
    private static final double SIMULATION_TIME = 10000;

    static class TimeInSystemStats {
        double totalTimeType1 = 0.0;
        double totalTimeType2 = 0.0;
        double totalTimeType3 = 0.0;
        int countType1 = 0;
        int countType2 = 0;
        int countType3 = 0;

        void recordExit(int type, double timeInSystem) {
            switch (type) {
                case 1 -> { totalTimeType1 += timeInSystem; countType1++; }
                case 2 -> { totalTimeType2 += timeInSystem; countType2++; }
                case 3 -> { totalTimeType3 += timeInSystem; countType3++; }
            }
        }

        void printStats() {
            System.out.println("\n--- Time in System by Patient Type ---");
            if (countType1 > 0) {
                System.out.printf("Type 1: avg=%.2f min (n=%d)\n", totalTimeType1/countType1, countType1);
            }
            if (countType2 > 0) {
                System.out.printf("Type 2: avg=%.2f min (n=%d)\n", totalTimeType2/countType2, countType2);
            }
            if (countType3 > 0) {
                System.out.printf("Type 3: avg=%.2f min (n=%d)\n", totalTimeType3/countType3, countType3);
            }
        }
    }

    static class LabArrivalIntervalStats {
        double lastArrivalTime = -1.0;
        double totalInterval = 0.0;
        double minInterval = Double.MAX_VALUE;
        double maxInterval = 0.0;
        int count = 0;

        void recordArrival(double currentTime) {
            if (lastArrivalTime >= 0) {
                double interval = currentTime - lastArrivalTime;
                totalInterval += interval;
                minInterval = Math.min(minInterval, interval);
                maxInterval = Math.max(maxInterval, interval);
                count++;
            }
            lastArrivalTime = currentTime;
        }

        void printStats() {
            System.out.println("\n--- Lab Arrival Intervals ---");
            if (count > 0) {
                System.out.printf("Mean interval: %.2f min\n", totalInterval/count);
                System.out.printf("Min interval: %.2f min\n", minInterval);
                System.out.printf("Max interval: %.2f min\n", maxInterval);
                System.out.printf("Number of intervals: %d\n", count);
            }
        }
    }

    private static final TimeInSystemStats timeStats = new TimeInSystemStats();
    private static final LabArrivalIntervalStats labStats = new LabArrivalIntervalStats();

    public static void main(String[] args) {

        final double CREATE_DELAY = 15.0;
        HospitalCreate create = new HospitalCreate(CREATE_DELAY);
        create.setName("Create");
        create.setDistribution(Distribution.EXP);

        HospitalProcess d1 = new HospitalProcess();
        d1.setName("D1");
        d1.setDistribution(Distribution.EXP);
        d1.setDevices(2);
        d1.setMaxQueue(50);
        d1.setVariableDelayByType(true);
        d1.setUsePriorityQueue(true);

        HospitalProcess d2 = new HospitalProcess();
        d2.setName("D2_Escorts");
        d2.setDistribution(Distribution.UNIFORM); // 3, 8
        d2.setDevices(3);
        d2.setMaxQueue(100); // Allow large queue for escorts
        d2.setDelayDev(8.);
        d2.setDelayMean(3.0);

        HospitalProcess travelToLab = new HospitalProcess() {
            @Override
            public void outAct() {
                labStats.recordArrival(getTcurr());
                super.outAct();
            }
        };
        travelToLab.setName("TravelToLab");
        travelToLab.setDistribution(Distribution.UNIFORM);
        travelToLab.setDevices(999);
        travelToLab.setDelayDev(5.);
        travelToLab.setDelayMean(2.0);

        HospitalProcess d3 = new HospitalProcess();
        d3.setName("D3_LabRegistration");
        d3.setDistribution(Distribution.ERLAND);
        d3.setDevices(1);
        d3.setMaxQueue(30);
        d3.setDelayDev(3.0);
        d3.setDelayMean(4.5);

        HospitalProcess d4 = new HospitalProcess();
        d4.setName("D4_LabAnalysis");
        d4.setDistribution(Distribution.ERLAND);
        d4.setDevices(2);
        d4.setMaxQueue(20);
        d4.setDelayDev(2.0);
        d4.setDelayMean(4.0);

        HospitalProcess travelFromLab = new HospitalProcess();
        travelFromLab.setName("TravelFromLab");
        travelFromLab.setDistribution(Distribution.UNIFORM);
        travelFromLab.setDevices(999);
        travelFromLab.setDelayDev(5.);
        travelFromLab.setDelayMean(2.0);

        // routes
        create.setNextElement(d1);
        d1.setPossibleRoutes(d2, travelToLab);
        d2.setPossibleRoutes(null, null);
        travelToLab.setNextElement(d3);
        d3.setNextElement(d4);
        d4.setNextElement(travelFromLab);
        travelFromLab.setPossibleRoutes(d1, null);


        // select strategy
        d1.setRoutingStrategy((List<Element> nextElements, Element currentElement) -> {
            if (nextElements.size() != 2) {
                throw new RuntimeException("D1 can not be routed");
            }

            int currentType = ((HospitalProcess) currentElement).getCurrentPatientType();

            return nextElements.stream()
                    .filter(el -> {
                        if (el instanceof HospitalProcess hp) {
                            return hp.getName().equals("D2_Escorts") && currentType == 1 ||
                                   hp.getName().equals("TravelToLab") && (currentType == 2 || currentType == 3);
                        }
                        return false;
                    })
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No matching route found for D1"));
        });

       d2.setRoutingStrategy((List<Element> nextElements, Element currentElement) -> {
           if (currentElement instanceof HospitalProcess hp) {
               double timeInSystem = hp.getTcurr() - hp.getCurrentPatientArrivalTime();
               timeStats.recordExit(hp.getCurrentPatientType(), timeInSystem);
           }
           return null;
       });


        travelFromLab.setRoutingStrategy((List<Element> nextElements, Element currentElement) -> {
            double random = rand.nextDouble();
            if (random < 0.5) {
                if (currentElement instanceof HospitalProcess hp) {
                    double timeInSystem = hp.getTcurr() - hp.getCurrentPatientArrivalTime();
                    timeStats.recordExit(hp.getCurrentPatientType(), timeInSystem);

                    hp.setCurrentPatientType(1);
                    hp.setCurrentPatientArrivalTime(hp.getTcurr());
                }
                return nextElements.getFirst();
            }
            else {
                if (currentElement instanceof HospitalProcess hp) {
                    double timeInSystem = hp.getTcurr() - hp.getCurrentPatientArrivalTime();
                    timeStats.recordExit(hp.getCurrentPatientType(), timeInSystem);
                }
                return null;
            }
        });

        ArrayList<Element> el = new ArrayList<>();
        el.add(create);
        el.add(d1);
        el.add(d2);
        el.add(travelToLab);
        el.add(d3);
        el.add(d4);
        el.add(travelFromLab);

        Model model = new Model(el);
        model.simulate(SIMULATION_TIME);

        System.out.println("\n--- Patient Type Statistics ---");
        System.out.println("Type 1 (50% expected) created: " + create.getType1Count());
        System.out.println("Type 2 (10% expected) created: " + create.getType2Count());
        System.out.println("Type 3 (40% expected) created: " + create.getType3Count());
        System.out.println("Total: " + (create.getType1Count() + create.getType2Count() + create.getType3Count()));

        timeStats.printStats();
        labStats.printStats();
    }
}
