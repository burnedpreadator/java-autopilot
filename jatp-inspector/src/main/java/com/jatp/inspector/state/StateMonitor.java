package com.jatp.inspector.state;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Monitors and tracks the state of global variables in the application.
 */
public class StateMonitor {

    private final Map<String, Object> trackedVariables = new ConcurrentHashMap<>();

    /**
     * Updates the value of a tracked variable.
     * @param name The name of the variable.
     * @param value The current value.
     */
    public void updateVariable(String name, Object value) {
        trackedVariables.put(name, value);
    }

    /**
     * Retrieves the current state of all tracked variables.
     * @return A map of variable names to values.
     */
    public Map<String, Object> getCurrentState() {
        return new HashMap<>(trackedVariables);
    }

    /**
     * Registers a variable for monitoring.
     * @param name The name of the variable to track.
     */
    public void registerVariable(String name) {
        trackedVariables.putIfAbsent(name, "NOT_YET_INITIALIZED");
    }
}
