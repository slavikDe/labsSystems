package org.example.HospitalSMO;

import org.example.smo_universal.Element;
import org.example.smo_universal.Model;
import org.example.smo_universal.RoutingStrategy;
import org.example.smo_universal.simsimple.Distribution;

import java.util.*;

public class Hospital {
    private static final Random rand = new Random();
    private static final double SIMULATION_TIME = 1000;

    public static void main(String[] args) {

        final double CREATE_DELAY = 15.0;
        HospitalCreate create = new HospitalCreate(CREATE_DELAY);
        create.setName("Create");
        create.setDistribution(Distribution.EXP);

        HospitalProcess d1 = new HospitalProcess();
        d1.setName("D1");
        d1.setDistribution(Distribution.EXP); // type1 == exp(15) type2 == exp(40) type3 == exp(30)
        d1.setDevices(2);
        d1.setVariableDelayByType(true); // Enable variable delay based on patient type

        HospitalProcess d2 = new HospitalProcess();
        d2.setName("D2");
        d2.setDistribution(Distribution.UNIFORM); // 3, 8
        d2.setDevices(3);
        d2.setDelayDev(8.);
        d2.setDelayMean(3.0);

        HospitalProcess d3 = new HospitalProcess();
        d3.setName("D3");
        d3.setDistribution(Distribution.ERLAND); // 3, 4.5
        d3.setDevices(2);
        d3.setDelayDev(3.0); // k
        d3.setDelayMean(4.5);

        HospitalProcess d4 = new HospitalProcess();
        d4.setName("D4");
        d4.setDistribution(Distribution.ERLAND); // 2, 4
        d4.setDelayDev(2.0);
        d4.setDelayMean(4.0);


        // routes
        create.setNextElement(d1);
        d1.setPossibleRoutes(d2, d3);
        d3.setNextElement(d4);
        d4.setPossibleRoutes(d1, null);


        // select strategy
        d1.setRoutingStrategy((RoutingStrategy<HospitalElement>) (nextElements, currentElement) -> {
            if (nextElements.size() != 2) {
                throw new RuntimeException("D1 can not be routed");
            }

            int currentType = currentElement.getPossibleTypes();

            return nextElements.stream()
                    .filter(el -> (el.getPossibleTypes() == currentType))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No matching route found for D1"));
        });


       d2.setRoutingStrategy((RoutingStrategy<HospitalElement>) (nextElements, currentElement) -> {
           // dispose
           return null;
       });

        d3.setNextElement(d4);

        d4.setRoutingStrategy((RoutingStrategy<HospitalElement>) (nextElements, currentElement) -> {
            if(nextElements.size() != 1) {  throw new RuntimeException("D4 can not be routed"); }

            double random = rand.nextDouble();
            if (random < 0.5) {
                currentElement.setType(1);
                return nextElements.getFirst();
            }
            else {
                // dispose
                return null;
            }
        });

        ArrayList<Element> el = new ArrayList<>();
        el.add(create);
        el.add(d1);
        el.add(d2);
        el.add(d3);
        el.add(d4);

        Model model = new Model(el);
        model.simulate(SIMULATION_TIME);
    }
}
