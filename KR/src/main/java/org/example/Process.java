package org.example;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Process extends Element {
    private int queue, maxQueue, failure;
    private double meanQueue;
    private double process_speed;

    List<Element> nextPossible = new ArrayList<>();
    List<Double> nextPossibleProbability = new ArrayList<>();

    public Process(){ }

    public Process(double delay) {
        super(delay);
        queue = 0;
        maxQueue = Integer.MAX_VALUE;
    }

    @Override
    public void inAct() {
        if (super.getState() == 0) {
            super.setState(1);
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
        if(!nextPossible.isEmpty()){
            setNextElement(selectNextELement());
        }
        if(getNextElement() == null){
            return; // dispose
        }
        super.setTnext(Double.MAX_VALUE);
        super.setState(0);
        getNextElement().setTaskSize(getTaskSize()); //  send task size to next element
        getNextElement().inAct();
        if (getQueue() > 0) {
            setQueue(getQueue() - 1);
            super.setState(1);
            super.setTnext(super.getTcurr() + super.getDelay());
        }
    }

    @Override
    public double getDelay(){
        return getTaskSize() * process_speed;
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("failure = " + this.getFailure());
    }

    @Override
    public void doStatistics(double delta) {
        meanQueue = getMeanQueue() + queue * delta;
    }

    private Element selectNextELement(){
        double random = Math.random();
        double c = 0;
        for(int i = 0; i < nextPossible.size(); i++){
            c += nextPossibleProbability.get(i);
            if(random < c){
               return nextPossible.get(i);
            }
        }
        throw new RuntimeException("Wrong probabilities for " + getTaskSize() + " element");
    }

    public void setNextPossibleProbability(List<Double> probabilities) {
        nextPossibleProbability.addAll(probabilities);
    }
}