package org.example.smo_universal.simsimple;

public enum Distribution {
    EXP,
    NORMAL,
    UNIFORM,
    ERLAND,
    UNKNOWN;

    @Override
    public String toString() {
        return this.name();
    }
}
