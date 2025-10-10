package org.example;

import org.example.simsimple.FunRand;

public class Create extends Element {
    public Create(double delayMean, double delayDev) {
        super.setDelayDev(delayDev);
        super.setDelayMean(delayMean);
        super.setTnext(0.0);
    }

    @Override
    public void outAct() {
        super.outAct();
        super.setTnext(super.getTcurr() + super.getDelay());
        getNextElement().setTaskSize(getTaskSize()); //  send task size to next element
        super.getNextElement().inAct();

    }

    public void createTask(double taskSizeMean, double taskSizeDev) {
        super.setTaskSize(FunRand.Unif(taskSizeMean, taskSizeDev));
    }
}
