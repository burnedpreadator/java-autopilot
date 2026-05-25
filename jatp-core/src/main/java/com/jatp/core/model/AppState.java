package com.jatp.core.model;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/**
 * Captures the runtime state of the application at a specific point in time.
 */
public record AppState(
    String currentScreenId,
    Map<String, Object> activeVariables,
    Instant timestamp,
    Map<String, String> runtimeMetadata
) {
    public AppState {
        Objects.requireNonNull(currentScreenId, "currentScreenId must not be null");
        Objects.requireNonNull(timestamp, "timestamp must not be null");
        activeVariables = (activeVariables == null) ? Map.of() : Map.copyOf(activeVariables);
        runtimeMetadata = (runtimeMetadata == null) ? Map.of() : Map.copyOf(runtimeMetadata);
    }
}
