package org.example;

import org.example.Models.ChaosModel;
import org.example.Models.LinerModel;
import org.example.Models.Model;
import org.example.Runner.ModelRunner;

/**
 * Hello world!
 *
 */
public class Main
{
//    private static final int[] numSystems = {1, 5, 10, 15, 20};
    private static final int[] numSystems = {10, 50, 100, 200, 400, 800};
    private static final double SIMULATION_TIME = 10_000;
    private static final int DEVICE_COUNT = 2;

    private static final int cyclesPerTest = 4;

    public static void main( String[] args ) {

        for(int nSystems : numSystems) {
            Model model = new ChaosModel(nSystems, DEVICE_COUNT, false);
            ModelRunner runner = new ModelRunner(model, SIMULATION_TIME, cyclesPerTest);
            long simulationTime = System.currentTimeMillis();
            runner.start();
            long simulationTimeEnd = System.currentTimeMillis();
            System.out.println("Global time for size: " + nSystems + ", time: " + ((simulationTimeEnd - simulationTime)) + " ms");
        }
    }
}
