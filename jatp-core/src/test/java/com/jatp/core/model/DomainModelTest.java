package com.jatp.core.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class DomainModelTest {

    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Test
    void testScreenSerialization() throws Exception {
        Widget widget = new Widget("w1", "Button", "path/to/btn", "Save", "Saves the form", Map.of("color", "blue"));
        Screen screen = new Screen("S1", "com.example.MainView", List.of("S0"), List.of(widget), List.of("var1"));

        String json = mapper.writeValueAsString(screen);
        Screen deserialized = mapper.readValue(json, Screen.class);

        Assertions.assertEquals(screen, deserialized);
        Assertions.assertEquals("S1", deserialized.screenId());
        Assertions.assertEquals(1, deserialized.widgets().size());
        Assertions.assertEquals("Saves the form", deserialized.widgets().get(0).tooltip());
    }

    @Test
    void testTestCaseSerialization() throws Exception {
        Step step = new Step(1, "Click Save", "Success", ValidationType.VISUAL);
        TestCase testCase = new TestCase("TC1", "Save Test", List.of("App is open"), List.of(step));

        String json = mapper.writeValueAsString(testCase);
        TestCase deserialized = mapper.readValue(json, TestCase.class);

        Assertions.assertEquals(testCase, deserialized);
        Assertions.assertTrue(deserialized.areStepsSequential());
    }

    @Test
    void testAppStateSerialization() throws Exception {
        AppState state = new AppState("S1", Map.of("user", "tanmaya"), Instant.now(), Map.of("os", "macos"));

        String json = mapper.writeValueAsString(state);
        AppState deserialized = mapper.readValue(json, AppState.class);

        Assertions.assertEquals(state, deserialized);
        Assertions.assertEquals("tanmaya", deserialized.activeVariables().get("user"));
    }

    @Test
    void testNullSafety() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            new Widget(null, "Button", "path", "label", "tooltip", Map.of());
        });

        Assertions.assertThrows(NullPointerException.class, () -> {
            new Screen(null, "view", List.of(), List.of(), List.of());
        });

        Assertions.assertThrows(NullPointerException.class, () -> {
            new Step(1, null, "res", ValidationType.VISUAL);
        });
    }

    @Test
    void testImmutability() {
        // Test that passing a mutable list doesn't allow modification
        java.util.ArrayList<String> vars = new java.util.ArrayList<>();
        vars.add("v1");
        Screen screen = new Screen("S1", "V1", List.of(), List.of(), vars);

        vars.add("v2");
        Assertions.assertEquals(1, screen.globalVariables().size(), "Screen globalVariables should be immutable");
    }
}
