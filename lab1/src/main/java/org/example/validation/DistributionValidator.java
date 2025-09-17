package org.example.validation;

import org.example.utils.Statistics;

public interface DistributionValidator {
    double calculateChi2(Statistics stats);
    double getCriticalValue(int degreesOfFreedom);
    String getDistributionName();
}