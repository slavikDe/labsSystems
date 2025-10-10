package org.example.BankSMO;

import org.example.smo_universal.Create;
import org.example.smo_universal.Element;
import org.example.smo_universal.Model;
import org.example.smo_universal.simsimple.Distribution;

import java.util.ArrayList;

public class Bank {
    private final static double CREATE_DELAY = 0.5;
    private final static double PROCESS_DELAY = 0.3;
    private final static double SIMULATION_TIME = 1000.0;

    public static void main(String[] args){
        ArrayList<Element> elements = new ArrayList<>();

        Create c = new Create(CREATE_DELAY);
        c.setName("CUSTOMER_ARRIVALS");
        elements.add(c);

        LaneSwitchingProcess lane1 = new LaneSwitchingProcess(PROCESS_DELAY);
        lane1.setName("LANE_1");
        lane1.setMaxQueue(3);

        LaneSwitchingProcess lane2 = new LaneSwitchingProcess(PROCESS_DELAY);
        lane2.setName("LANE_2");
        lane2.setMaxQueue(3);

        lane1.setOtherLane(lane2);
        lane2.setOtherLane(lane1);

        c.setPossibleRoutes(lane1, lane2);
        c.setRoutingStrategy(new BankRoutingStrategy());

        lane1.setState(1);
        lane1.setQueue(2);
        lane1.setTnext(c.getTcurr() + lane1.getDelay());

        lane2.setState(1);
        lane2.setQueue(2);
        lane2.setTnext(c.getTcurr() + lane2.getDelay());

        elements.add(lane1);
        elements.add(lane2);

        elements.forEach(element -> element.setDistribution(Distribution.EXP));
        Model model = new Model(elements);
        model.simulate(SIMULATION_TIME);

        printBankStatistics(c, lane1, lane2, SIMULATION_TIME);
    }

    private static void printBankStatistics(Create generator, LaneSwitchingProcess lane1, LaneSwitchingProcess lane2, double totalTime) {
        System.out.println("\n=== BANK STATISTICS ===");

        System.out.printf("1. Lane 1 utilization: %.2f%%\n", lane1.getDeviceUtilization(totalTime));
        System.out.printf("   Lane 2 utilization: %.2f%%\n", lane2.getDeviceUtilization(totalTime));

        double avgCustomersInBank = (lane1.getMeanQueue() + lane2.getMeanQueue()) / totalTime +
                                   (lane1.getState() + lane2.getState());
        System.out.printf("2. Average customers in bank: %.2f\n", avgCustomersInBank);

        int totalServed = lane1.getQuantity() + lane2.getQuantity();
        double avgInterval = totalTime / totalServed;
        System.out.printf("3. Average departure interval: %.2f time units\n", avgInterval);

        double arrivalRate = generator.getQuantity() / totalTime;
        double avgTimeInBank = avgCustomersInBank / arrivalRate;
        System.out.printf("4. Average time in bank: %.2f time units\n", avgTimeInBank);

        System.out.printf("5. Lane 1 avg queue: %.2f\n", lane1.getMeanQueue() / totalTime);
        System.out.printf("   Lane 2 avg queue: %.2f\n", lane2.getMeanQueue() / totalTime);

        int totalRejected = generator.getRejectedCount() + lane1.getFailure() + lane2.getFailure();
        double rejectionRate = (double) totalRejected / generator.getQuantity() * 100;
        System.out.printf("6. Rejection rate: %.2f%% (%d rejected out of %d arrivals)\n",
                         rejectionRate, totalRejected, generator.getQuantity());

        int totalSwitches = lane1.getLaneSwitches() + lane2.getLaneSwitches();
        System.out.printf("7. Total lane switches: %d\n", totalSwitches);
    }
}
