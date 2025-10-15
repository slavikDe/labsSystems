package org.example;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Task {
    private double taskSize;
    private boolean isRecycle = false;

    public Task() {}
    public Task(double taskSize) { this.taskSize = taskSize;}

}
