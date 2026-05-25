package com.jatp.core.model;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a logical screen/view in the application.
 */
public record Screen(
    String screenId,
    String viewClass,
    List<String> reachableFrom,
    List<Widget> widgets,
    List<String> globalVariables
) {
    public Screen {
        Objects.requireNonNull(screenId, "screenId must not be null");
        reachableFrom = (reachableFrom == null) ? List.of() : List.copyOf(reachableFrom);
        widgets = (widgets == null) ? List.of() : List.copyOf(widgets);
        globalVariables = (globalVariables == null) ? List.of() : List.copyOf(globalVariables);
    }

    /**
     * Utility to find a widget by its ID within the screen.
     */
    public Optional<Widget> findWidgetById(String id) {
        return widgets.stream()
                     .filter(w -> w.widgetId().equals(id))
                     .findFirst();
    }
}
