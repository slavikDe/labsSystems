package org.example.parts;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class Server {
    private boolean isBusy;
    private double busyTime;
    private Task currentTask;

    Server() {
        this.isBusy = false;
        this.busyTime = 0;
    }

    public void takeJob(Task task) {
        busyTime += task.getProcessingTime();
        currentTask = task;
    }

    public Task getTask() {
        return currentTask;
    }
}
