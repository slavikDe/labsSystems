package org.example.parts;

import lombok.Getter;
import lombok.Setter;
import org.example.util.FunRand;

@Getter
@Setter
public class ArrivalPoint extends Element {
    private final String name;
    private double task_dev;
    private double task_mean;

    public ArrivalPoint(String name) {
        this.name = name;
        super.setTnext(0.0);
    }


    @Override
    public void outAct() {
        super.outAct();
        super.setTnext(super.getTcurr() + super.getDelay());

        Task task  = createTask(task_mean, task_dev);
        if(getNextElement() instanceof Stage nextElement){
            if(!nextElement.getQueue().isFull()){
                nextElement.add(task);
                super.getNextElement().inAct();
            } else {
                nextElement.getQueue().increaseFailure();
            }
        }
    }

    private Task createTask(double taskSizeMean, double taskSizeDev) {
        return new Task(FunRand.Unif(taskSizeMean - taskSizeDev, taskSizeMean + taskSizeDev));
    }

    @Override
    public void printResult() {
        System.out.println("┌─────────────────────────────────────────────────────┐");
        System.out.printf("│ %-51s │%n", "Arrival Point: " + name);
        System.out.println("├─────────────────────────────────────────────────────┤");
        System.out.printf("│ %-30s: %18d  │%n", "Total Tasks Generated", getQuantity());
        System.out.println("└─────────────────────────────────────────────────────┘");
        System.out.println();
    }


}
