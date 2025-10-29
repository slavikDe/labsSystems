package org.example.parts;

public class Statistics {
    public static void stageStats(Stage stage, double globalSimTime) {
        Queue queue = stage.getQueue();
        double meanQ = queue.getMeanValue() / globalSimTime;
        int originalTasks = stage.getQuantity() - stage.getReprocessedCount();
        int totalAttempted = originalTasks + queue.getFailure();
        double failure = totalAttempted > 0 ? queue.getFailure() / (double) totalAttempted : 0.0;
        double utilization = stage.getBusyTime() / globalSimTime / stage.getServerManager().getServersCount() * 100;
        double queueState = queue.getSize();

        System.out.println("┌────────────────────────────────────────────────────┐");
        System.out.printf("│ %-50s │%n", "Stage: " + stage.getName());
        System.out.println("├────────────────────────────────────────────────────┤");
        System.out.printf("│ %-30s: %18.4f │%n", "Average Queue Length", meanQ);
        System.out.printf("│ %-30s: %17.2f%% │%n", "Failure Probability", failure * 100);
        System.out.printf("│ %-30s: %17.2f%% │%n", "Utilization", utilization);
        System.out.printf("│ %-30s: %18f │%n", "Current Queue Size", queueState);
        System.out.println("├────────────────────────────────────────────────────┤");
        System.out.printf("│ %-30s: %18d │%n", "Total Processed", stage.getQuantity());
        System.out.printf("│ %-30s: %18d │%n", "Original Tasks", originalTasks);
        System.out.printf("│ %-30s: %18d │%n", "Reprocessed Tasks", stage.getReprocessedCount());
        System.out.printf("│ %-30s: %18d │%n", "Total Failures", queue.getFailure());
        System.out.printf("│ %-30s: %18d │%n", "Number of Servers", stage.getServerManager().getServersCount());
        System.out.println("└────────────────────────────────────────────────────┘");
        System.out.println();

    }
}
