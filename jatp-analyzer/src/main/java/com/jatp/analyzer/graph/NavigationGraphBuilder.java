package com.jatp.analyzer.graph;

import com.jatp.core.model.Screen;
import java.util.*;

/**
 * Builds a navigation graph from discovered screens by analyzing method calls.
 */
public class NavigationGraphBuilder {

    /**
     * analyzes navigation flows between screens.
     * @param screens The map of discovered screens.
     * @return A map where keys are screenIds and values are lists of reachable screenIds.
     */
    public Map<String, List<String>> buildGraph(Map<String, Screen> screens) {
        Map<String, List<String>> graph = new HashMap<>();

        // In a real implementation, this would analyze method calls to other views
        // or look for Eclipse Command handlers that switch perspectives/views.

        screens.keySet().forEach(id -> graph.put(id, new ArrayList<>()));

        // Placeholder: For MVP, we just return an empty graph.
        // Future implementation: analyze 'reachableFrom' in Screen models.

        return graph;
    }
}
