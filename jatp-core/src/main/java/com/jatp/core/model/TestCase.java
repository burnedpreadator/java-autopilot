package com.jatp.core.model;

import java.util.List;
import java.util.Objects;

/**
 * Represents a full test scenario comprising a series of steps.
 */
public record TestCase(
    String testCaseId,
    String title,
    List<String> preconditions,
    List<Step> steps
) {
    public TestCase {
        Objects.requireNonNull(testCaseId, "testCaseId must not be null");
        Objects.requireNonNull(title, "title must not be null");
        preconditions = (preconditions == null) ? List.of() : List.copyOf(preconditions);
        steps = (steps == null) ? List.of() : List.copyOf(steps);
    }

    /**
     * Utility to verify that steps are sequentially numbered.
     */
    public boolean areStepsSequential() {
        for (int i = 0; i < steps.size(); i++) {
            if (steps.get(i).stepNumber() != i + 1) return false;
        }
        return true;
    }
}
