package com.jatp.core.parser;

import com.jatp.core.model.Step;
import com.jatp.core.model.TestCase;
import com.jatp.core.model.ValidationType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses basic Markdown test cases into TestCase records.
 *
 * Supported format:
 *
 * # TC_001: Save User
 * - Precondition: User is on Profile screen
 * - Step 1: Click Save
 * - Expected: Success message appears
 */
public final class MarkdownTestParser {

    private static final Pattern TEST_CASE_PATTERN =
            Pattern.compile("^#\\s*([^:]+):\\s*(.+)$");

    private static final Pattern PRECONDITION_PATTERN =
            Pattern.compile("^-\\s*Precondition:\\s*(.+)$",
                    Pattern.CASE_INSENSITIVE);

    private static final Pattern STEP_PATTERN =
            Pattern.compile("^-\\s*Step\\s+(\\d+)\\s*:\\s*(.+)$",
                    Pattern.CASE_INSENSITIVE);

    private static final Pattern EXPECTED_PATTERN =
            Pattern.compile("^-\\s*Expected:\\s*(.+)$",
                    Pattern.CASE_INSENSITIVE);

    public List<TestCase> parse(String markdown) {
        Objects.requireNonNull(markdown, "markdown must not be null");

        List<TestCase> testCases = new ArrayList<>();

        String testCaseId = null;
        String title = null;
        List<String> preconditions = new ArrayList<>();
        List<Step> steps = new ArrayList<>();

        int currentStepNumber = -1;
        String currentAction = null;
        String currentExpectedResult = null;

        String[] lines = markdown.split("\\R");

        for (String line : lines) {
            String trimmedLine = line.trim();

            if (trimmedLine.isEmpty()) {
                continue;
            }

            Matcher testCaseMatcher = TEST_CASE_PATTERN.matcher(trimmedLine);

            if (testCaseMatcher.matches()) {
                if (testCaseId != null) {
                    addStepIfPresent(
                            steps,
                            currentStepNumber,
                            currentAction,
                            currentExpectedResult
                    );

                    testCases.add(new TestCase(
                            testCaseId,
                            title,
                            preconditions,
                            steps
                    ));
                }

                testCaseId = testCaseMatcher.group(1).trim();
                title = testCaseMatcher.group(2).trim();
                preconditions = new ArrayList<>();
                steps = new ArrayList<>();

                currentStepNumber = -1;
                currentAction = null;
                currentExpectedResult = null;

                continue;
            }

            Matcher preconditionMatcher =
                    PRECONDITION_PATTERN.matcher(trimmedLine);

            if (preconditionMatcher.matches()) {
                preconditions.add(preconditionMatcher.group(1).trim());
                continue;
            }

            Matcher stepMatcher = STEP_PATTERN.matcher(trimmedLine);

            if (stepMatcher.matches()) {
                addStepIfPresent(
                        steps,
                        currentStepNumber,
                        currentAction,
                        currentExpectedResult
                );

                currentStepNumber =
                        Integer.parseInt(stepMatcher.group(1));
                currentAction = stepMatcher.group(2).trim();
                currentExpectedResult = null;

                continue;
            }

            Matcher expectedMatcher = EXPECTED_PATTERN.matcher(trimmedLine);

            if (expectedMatcher.matches()) {
                currentExpectedResult =
                        expectedMatcher.group(1).trim();
            }
        }

        addStepIfPresent(
                steps,
                currentStepNumber,
                currentAction,
                currentExpectedResult
        );

        if (testCaseId != null) {
            testCases.add(new TestCase(
                    testCaseId,
                    title,
                    preconditions,
                    steps
            ));
        }

        return testCases;
    }

    private static void addStepIfPresent(
            List<Step> steps,
            int stepNumber,
            String action,
            String expectedResult) {

        if (stepNumber >= 0 && action != null) {
            steps.add(new Step(
                    stepNumber,
                    action,
                    expectedResult,
                    ValidationType.VISUAL
            ));
        }
    }
}
