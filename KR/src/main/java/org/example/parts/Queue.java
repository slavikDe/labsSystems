package org.example.parts;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.PriorityQueue;

@Getter
@Setter
public class Queue {
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private PriorityQueue<Task> tasks = new PriorityQueue<>(
            Comparator.comparing(Task::isProcessed, Comparator.reverseOrder())
                    .thenComparingDouble(Task::getTaskSize));

    private int capacity;
    private double meanValue;
    private int failure = 0;

    Queue(){
        meanValue = 0.0;
    }

    public boolean add(Task task) {
        return tasks.add(task);
    }
    public void increaseFailure(){
        failure++;
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public boolean isFull() {
        return tasks.size() == capacity;
    }

    public Task poll() { return tasks.poll(); }

    public void updateMeanValue(double delta) {
        meanValue = meanValue + tasks.size() * delta;
    }

    public int getSize() {
        return tasks.size();
    }
}
