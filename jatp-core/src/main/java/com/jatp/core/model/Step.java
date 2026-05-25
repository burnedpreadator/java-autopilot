package com.jatp.core.model;

import java.util.Objects;

/**
 * Represents a single action and its expected outcome in a test case.
 */
public record Step(
    int stepNumber,
    String action,
    String expectedResult,
    ValidationType validationType
) {
    public Step {
        Objects.requireNonNull(action, "action must not be null");
        Objects.requireNonNull(validationType, "validationType must not be null");
    }
}
