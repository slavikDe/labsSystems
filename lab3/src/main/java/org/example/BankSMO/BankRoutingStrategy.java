package org.example.BankSMO;

import org.example.smo_universal.Create;
import org.example.smo_universal.Element;
import org.example.smo_universal.Process;
import org.example.smo_universal.RoutingStrategy;

import java.util.List;

public class BankRoutingStrategy implements RoutingStrategy {

    @Override
    public Element selectNext(List nextElements, Element currentElement) {
        if(!(currentElement instanceof Create)) {throw new IllegalArgumentException("\"Bank routing requires create element for selecting next");}

        Process lane1, lane2;

        try {
            lane1 = (Process) nextElements.get(0);
            lane2 = (Process) nextElements.get(1);
        }catch(ClassCastException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        int queue1 = lane1.getQueue();
        int queue2 = lane2.getQueue();

        if (queue1 >= lane1.getMaxQueue() && queue2 >= lane2.getMaxQueue()) {
            return null;
        }

        if (queue1 == queue2) {
            return lane1;
        }

        if(queue1 - queue2 >= 2) {
            return lane2;
        }
        else if(queue2 - queue1 >= 2) {
            return lane1;
        }

        if (queue1 < queue2) {
            return lane1;
        } else {
            return lane2;
        }
    }


}