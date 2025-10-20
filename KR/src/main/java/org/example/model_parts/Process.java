package org.example.model_parts;

import lombok.Getter;
import lombok.Setter;
import org.example.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Process extends Element {
    private PriorityQueue<Task> queue = new PriorityQueue<>(
            Comparator.comparing(Task::isRecycle, Comparator.reverseOrder())
                    .thenComparingDouble(Task::getTaskSize));

    private Map<Integer, Task> activeTasks = new HashMap<>();

    private int maxQueue, failure;
    private double meanQueue;
    private double busyTime;
    private int devices = 1; // default
    private double process_speed;

    List<Element> nextPossible = new ArrayList<>();
    List<Double> nextPossibleProbability = new ArrayList<>();

    public Process(){
        devices =4;
    }

    public Process(double delay) {
        super(delay);
        queue.clear();
        maxQueue = Integer.MAX_VALUE;
    }

    @Override
    public void inAct() {
        while (super.getState() < devices && !queue.isEmpty()) {
            Task task = queue.poll();
            double finishTime = super.getTcurr() + calculateDelay(task);

            int deviceId = 0;
            while (activeTasks.containsKey(deviceId)) {
                deviceId++;
            }
            task.setFinishTime(finishTime);
            activeTasks.put(deviceId, task);
            super.setState(super.getState() + 1);
        }

        updateTnext();
    }

    private void updateTnext() {
        if (activeTasks.isEmpty()) {
            super.setTnext(Double.MAX_VALUE);
        } else {
            double minFinishTime = Double.MAX_VALUE;
            for (Task task : activeTasks.values()) {
                if (task.getFinishTime() < minFinishTime) {
                    minFinishTime = task.getFinishTime();
                }
            }
            super.setTnext(minFinishTime);
        }
    }

    @Override
    public void outAct() {
        super.outAct();

        int completedDeviceId = 0;
        for(Integer deviceId : activeTasks.keySet()) {
            if(activeTasks.get(deviceId).getFinishTime() <= super.getTcurr()) {
                completedDeviceId =  deviceId;
            }
        }

        Task completedTask = activeTasks.remove(completedDeviceId);
        if(super.getState() > 0) {
            super.setState(super.getState() - 1);
        }

        if(!nextPossible.isEmpty()) {
            setNextElement(selectNextELement());
        }

        if (getNextElement() instanceof Process nextElement) {
            if (nextElement.getName().equals("D1")) {
                completedTask.setRecycle(true);
            }
            if (nextElement.getMaxQueue() >= nextElement.getQueue().size()) {
                nextElement.getQueue().add(completedTask);
                nextElement.inAct();
            } else {
                nextElement.increaseFailure();
            }
        }
        this.inAct();
    }

    private double calculateDelay(Task task) {
        return task.getTaskSize() / process_speed;
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("failure = " + this.getFailure());
    }

    @Override
    public void doStatistics(double delta) {
        meanQueue = getMeanQueue() + queue.size() * delta;
        int devicesInUse = Math.min(super.getState(), devices);
        busyTime += devicesInUse * delta;
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