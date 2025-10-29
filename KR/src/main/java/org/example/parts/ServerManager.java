package org.example.parts;

import java.util.ArrayList;
import java.util.List;

/**
 * put new task
 * check for free servers
 * free server with spec time
 */

public class ServerManager {
    private final List<Server> servers;
    private int freeCount;

    public ServerManager(int numServers) {
        freeCount = numServers;
        servers = new ArrayList<>();
        for (int i = 0; i < numServers; i++) {
            servers.add(new Server());
        }
    }

    public void putTask(Task task) {
        Server server = findFreeServer();
        server.setBusy(true);
        freeCount--;
        server.takeJob(task);
    }

    private Server findFreeServer() {
        for (Server server : servers) {
            if (!server.isBusy()) {
                return server;
            }
        }
        throw new RuntimeException("All servers are busy");
    }

    public boolean canProcess() {
        return  freeCount > 0;
    }

    public List<Server> getServers() {
        return servers;
    }

    public List<Task> getCompletedTasks(double tcurr) {
        List<Task> completedTasks = servers.stream()
                .filter(Server::isBusy)
                .filter(srv -> srv.getTask().getFinishTime() == tcurr)
                .peek(srv -> {
                    srv.setBusy(false);
                    freeCount++;
                })
                .map(Server::getTask)
                .toList();

        if(completedTasks.isEmpty()){
            throw new RuntimeException("Cannot find server with such task finish time " + tcurr);
        }

        return completedTasks;
    }

    public int getServersCount() {
        return servers.size();
    }

    public int getBusyServersCount() {
        return servers.size() - freeCount;
    }
}
