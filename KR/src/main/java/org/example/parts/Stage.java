package org.example.parts;

import lombok.Getter;
import lombok.Setter;
import org.example.parts.multi_processing.Server;
import org.example.parts.multi_processing.ServerManager;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Stage extends Element {
    private Queue queue;
    private final double speed;
    private final String name;
    ServerManager serverManager;

    private List<Element> nextPossible = new ArrayList<>();
    private List<Double> nextPossibleProbability = new ArrayList<>();

    private double busyTime = 0;
    private int reprocessedCount = 0;

    public Stage(String name, double speed) {
        this.queue = new Queue();
        this.speed = speed;
        this.name = name;
    }

    @Override
    public void inAct(){
        if(serverManager.canProcess() && !queue.isEmpty()){
            Task task = queue.poll();
            double processTime = calculateDelay(task);
            task.setFinishTime(super.getTcurr() + processTime);
            task.setProcessingTime(processTime);
            serverManager.putTask(task);
        }
        updateTnext();
    }

    @Override
    public void outAct(){
        super.outAct();
        List<Task> completedTask = serverManager.getCompletedTasks(getTcurr()); // free servers with process current task

        for(Task task : completedTask){
            Element nextElement;

            if(!nextPossible.isEmpty()) {
                nextElement = selectNextStage();
            } else {
                nextElement = super.getNextElement();
            }

            if (nextElement instanceof Stage nextStage) {
                if (nextStage.getName().equals("Input")) {
                    task.setProcessed(true);
                }
                if (!nextStage.getQueue().isFull()) {
                    nextStage.add(task);

                    nextStage.inAct();
                } else {
                    nextStage.getQueue().increaseFailure();
                }
            }
        }
        this.inAct();

    }

    public void add(Task task){
        if(task.isProcessed()){
            reprocessedCount++;
        }
        queue.add(task);
    }

    private Element selectNextStage() {
        double random = Math.random();
        double c = 0;
        for(int i = 0; i < nextPossible.size(); i++){
            c += nextPossibleProbability.get(i);
            if(random < c){
                if (nextPossible.get(i) instanceof Dispose) {
                        Dispose.counter++;
                }
                 return nextPossible.get(i);
            }
        }
        throw new RuntimeException("Wrong probabilities for " + this.getId() + " " + this.getName() + " element");
    }

    private double calculateDelay(Task task) {
        return task.getTaskSize() / speed;
    }


    private void updateTnext() {
        double minFinishTime = Double.MAX_VALUE;
        for(Server server : serverManager.getServers()){
            if(server.isBusy()){
                double finishTime = server.getTask().getFinishTime();
                if(finishTime < minFinishTime){
                    minFinishTime = finishTime;
                }
            }
        }
        super.setTnext(minFinishTime);
    }


    public void setQueueCapacity(int capacity){
        queue.setCapacity(capacity);
    }

    public void setServers(int numServers) {
        serverManager = new ServerManager(numServers);
    }

    public void addNextWithProbabilities(List<Element> next, List<Double> probabilities) {
        this.nextPossible.clear();
        this.nextPossibleProbability.clear();
        this.nextPossible.addAll(next);
        this.nextPossibleProbability.addAll(probabilities);
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("failure = " + queue.getFailure());
        System.out.println("Busy Servers = " + getServerManager().getBusyServersCount());
    }

    @Override
    public void doStatistics(double delta) {
        queue.updateMeanValue(delta);
        int devicesInUse = serverManager.getBusyServersCount();
        busyTime += devicesInUse * delta;
    }



}

