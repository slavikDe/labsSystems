package org.example.model_parts;

import lombok.Setter;
import org.example.Task;
import org.example.simsimple.FunRand;

@Setter
public class Create extends Element {
    private double taskSizeMean;
    private double taskSizeDev;

    public Create(double delayMean, double delayDev) {
        super.setDelayDev(delayDev);
        super.setDelayMean(delayMean);
        super.setTnext(0.0);
    }

    @Override
    public void outAct() {
        super.outAct();
        super.setTnext(super.getTcurr() + super.getDelay());

        Task task  = createTask(taskSizeMean, taskSizeDev);
        if(getNextElement() instanceof Process nextElement){
            if(nextElement.getMaxQueue() > nextElement.getQueue().size()){
                nextElement.getQueue().offer(task);
                super.getNextElement().inAct();
            } else {
                nextElement.increaseFailure();
            }
        }
    }

    private Task createTask(double taskSizeMean, double taskSizeDev) {
        return new Task(FunRand.Unif(taskSizeMean - taskSizeDev, taskSizeMean + taskSizeDev));
    }
}
