package org.example;

import org.example.Models.LinerModel;
import org.example.Models.Model;
import org.example.Runner.ModelRunner;
import org.example.smo_universal.simsimple.Distribution;

/**
 * Hello world!
 *
 */
public class Main
{
//    private static final int[] numSystems = {1, 5, 10, 15, 20};
    private static final int[] numSystems = {500};
    private static final double SIMULATION_TIME = 10_000;
    private static final Distribution DISTRIBUTION_TYPE = Distribution.EXP;
    private static final int cyclesPerTest = 10;

    public static void main( String[] args ) {

        for(int nSystems : numSystems) {
            Model model = new LinerModel(nSystems, false);
            ModelRunner runner = new ModelRunner(model, SIMULATION_TIME, cyclesPerTest);
            long simulationTime = System.currentTimeMillis();
            runner.start();
            long simulationTimeEnd = System.currentTimeMillis();
            System.out.println("Global time for size: " + nSystems + ", time: " + ((simulationTimeEnd - simulationTime) / 1000) + " seconds");
        }

    }



}
