package org.example.smo_universal;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Create extends Element {
    private final List<Element> possibleRoutes = new ArrayList<>();

    @Setter
    private RoutingStrategy routingStrategy;
    @Getter
    private int rejectedCount = 0;

    public Create(double delay) {
        super(delay);
        super.setTnext(0.0);
    }

    public void setPossibleRoutes(Element... elements) {
        possibleRoutes.clear();
        possibleRoutes.addAll(Arrays.asList(elements));
    }

    @Override
    public void outAct() {
        super.outAct();
        super.setTnext(super.getTcurr() + super.getDelay());

        Element targetElement = null;

        if (possibleRoutes.size() == 1) {
            targetElement = possibleRoutes.get(0);
        } else if (possibleRoutes.size() > 1 && routingStrategy != null) {
            targetElement = routingStrategy.selectNext(possibleRoutes, this);
        } else if (super.getNextElement() != null) {
            targetElement = super.getNextElement();
        }

        if (targetElement != null) {
            targetElement.inAct();
        } else {
            rejectedCount++;
        }
    }

    @Override
    public void printResult() {
        super.printResult();
        System.out.println(getName() + " rejected = " + rejectedCount);
    }
}
