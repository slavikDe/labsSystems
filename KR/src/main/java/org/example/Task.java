package org.example;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Task {
    private static int counter = 0;

    private double taskSize;
    private boolean isRecycle = false;
    private double finishTime = Double.MAX_VALUE;

    public Task() {}
    public Task(double taskSize) { this.taskSize = taskSize;
        counter++;
    }

}
