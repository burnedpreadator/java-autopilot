package com.jatp.core.model;

import java.util.Map;
import java.util.Objects;

/**
 * Represents a UI element within a screen.
 */
public record Widget(
    String widgetId,
    String type,
    String path,
    String label,
    String tooltip,
    Map<String, String> metadata
) {
    public Widget {
        Objects.requireNonNull(widgetId, "widgetId must not be null");
        Objects.requireNonNull(type, "type must not be null");
        // Ensure metadata is never null to avoid NPEs during serialization/access
        metadata = (metadata == null) ? Map.of() : Map.copyOf(metadata);
    }
}
