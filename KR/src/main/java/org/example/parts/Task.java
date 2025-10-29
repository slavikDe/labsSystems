package org.example.parts;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Task {

    private double taskSize;
    private boolean processed = false;
    private double finishTime = Double.MAX_VALUE;
    private double processingTime = Double.MAX_VALUE;

    public Task() {}
    public Task(double taskSize) { this.taskSize = taskSize;
    }



}
