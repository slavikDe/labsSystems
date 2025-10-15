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
        super.outAct(); // increment quantity
        super.setTnext(super.getTcurr() + super.getDelay());
        double taskSize = createTask(taskSizeMean, taskSizeDev);

        if(getNextElement() instanceof Process nextElement){
            if(nextElement.getMaxQueue() > nextElement.getQueue().size()){
                nextElement.getQueue().offer(new Task(taskSize));
                super.getNextElement().inAct();
            } else { // exist free place in queue
                nextElement.increaseFailure();
            }
        }
    }

    private double createTask(double taskSizeMean, double taskSizeDev) {
        return FunRand.Unif(taskSizeMean, taskSizeDev);
    }
}
