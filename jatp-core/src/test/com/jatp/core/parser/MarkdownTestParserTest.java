package com.jatp.core.parser;

import com.jatp.core.model.Step;
import com.jatp.core.model.TestCase;
import com.jatp.core.model.ValidationType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MarkdownTestParserTest {

    @Test
    void shouldParseBasicMarkdownTestCase() {
        String markdown = """
                # TC_001: Save User
                - Precondition: User is on Profile screen
                - Step 1: Click Save
                - Expected: Success message appears
                """;

        MarkdownTestParser parser = new MarkdownTestParser();

        List<TestCase> testCases = parser.parse(markdown);

        assertEquals(1, testCases.size());

        TestCase testCase = testCases.get(0);

        assertEquals("TC_001", testCase.testCaseId());
        assertEquals("Save User", testCase.title());
        assertEquals(
                List.of("User is on Profile screen"),
                testCase.preconditions()
        );

        assertEquals(1, testCase.steps().size());

        Step step = testCase.steps().get(0);

        assertEquals(1, step.stepNumber());
        assertEquals("Click Save", step.action());
        assertEquals("Success message appears", step.expectedResult());
        assertEquals(ValidationType.VISUAL, step.validationType());
    }
}
