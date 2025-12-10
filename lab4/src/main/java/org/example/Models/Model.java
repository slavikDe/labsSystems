package org.example.Models;

import org.example.smo_universal.Create;

public interface Model {
    void initialize();
    void go(double simulationTime);
}
