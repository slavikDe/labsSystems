package org.example.model_parts;

import lombok.Getter;
import lombok.Setter;
import org.example.Task;

import javax.swing.text.rtf.RTFEditorKit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

@Getter
@Setter
public class Process extends Element {
    private PriorityQueue<Task> queue = new PriorityQueue<>(
            Comparator.comparing(Task::isRecycle, Comparator.reverseOrder())
                    .thenComparingDouble(Task::getTaskSize));

    private int maxQueue, failure;
    private double meanQueue;
    private double process_speed;
    private Task currentTask;

    List<Element> nextPossible = new ArrayList<>();
    List<Double> nextPossibleProbability = new ArrayList<>();

    public Process(){ }

    public Process(double delay) {
        super(delay);
        queue.clear();
        maxQueue = Integer.MAX_VALUE;
    }

    @Override
    public void inAct() {
        if (super.getState() == 0 && !queue.isEmpty()) {
            super.setState(1);
            currentTask = queue.poll();
            super.setTnext(super.getTcurr() + getDelay());
        }
    }

    @Override
    public void outAct() {
        super.outAct();

        super.setTnext(Double.MAX_VALUE);
        super.setState(0);

        if(!nextPossible.isEmpty()){
            setNextElement(selectNextELement());
        }

        if(getNextElement() instanceof Process nextElement){
            if(nextElement.getName().equals("D1")){
                currentTask.setRecycle(true);
            }

            if(nextElement.getMaxQueue() > nextElement.getQueue().size()){
                nextElement.getQueue().add(currentTask);
                getNextElement().inAct();
            }
            else {
                nextElement.increaseFailure();
            }
        }
    }

    @Override
    public double getDelay(){
        return currentTask.getTaskSize() * process_speed;
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("failure = " + this.getFailure());
    }

    @Override
    public void doStatistics(double delta) {
            meanQueue = getMeanQueue() + queue.size() * delta;
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
        throw new RuntimeException("Wrong probabilities for " + this.getId() + " " + this.getName() + " element");
    }

    public void setNextPossibleProbability(List<Double> probabilities) {
        nextPossibleProbability.addAll(probabilities);
    }

    public void increaseFailure(){
        failure++;
    }
}