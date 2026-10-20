package com.jatp.core.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DomainModelEdgeCaseTest {

    private static final Instant TIMESTAMP = Instant.parse("2025-01-01T12:00:00.123456789Z");
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private static Widget widget(String id) {
        return new Widget(id, "Button", "static-analysis", id, null, Map.of());
    }

    private static Step step(int number) {
        return new Step(number, "Click Save", null, ValidationType.VISUAL);
    }

    @Test
    void nullCollectionsBecomeEmptyAndOptionalFieldsRemainNull() {
        Widget widget = new Widget("save", "Button", null, null, null, null);
        Screen screen = new Screen("main", null, null, null, null);
        TestCase testCase = new TestCase("TC1", "Save", null, null);
        AppState state = new AppState("main", null, TIMESTAMP, null);

        assertAll(
                () -> assertEquals(Map.of(), widget.metadata()),
                () -> assertNull(widget.path()),
                () -> assertNull(widget.label()),
                () -> assertNull(widget.tooltip()),
                () -> assertNull(screen.viewClass()),
                () -> assertEquals(List.of(), screen.reachableFrom()),
                () -> assertEquals(List.of(), screen.widgets()),
                () -> assertEquals(List.of(), screen.globalVariables()),
                () -> assertEquals(List.of(), testCase.preconditions()),
                () -> assertEquals(List.of(), testCase.steps()),
                () -> assertTrue(testCase.areStepsSequential()),
                () -> assertEquals(Map.of(), state.activeVariables()),
                () -> assertEquals(Map.of(), state.runtimeMetadata()));
    }

    static Stream<Arguments> requiredFields() {
        return Stream.of(
                Arguments.of("widgetId", (Supplier<?>) () -> new Widget(null, "Button", null, null, null, null)),
                Arguments.of("type", (Supplier<?>) () -> new Widget("save", null, null, null, null, null)),
                Arguments.of("screenId", (Supplier<?>) () -> new Screen(null, null, null, null, null)),
                Arguments.of("testCaseId", (Supplier<?>) () -> new TestCase(null, "Save", null, null)),
                Arguments.of("title", (Supplier<?>) () -> new TestCase("TC1", null, null, null)),
                Arguments.of("action", (Supplier<?>) () -> new Step(1, null, null, ValidationType.VISUAL)),
                Arguments.of("validationType", (Supplier<?>) () -> new Step(1, "Save", null, null)),
                Arguments.of("currentScreenId", (Supplier<?>) () -> new AppState(null, null, TIMESTAMP, null)),
                Arguments.of("timestamp", (Supplier<?>) () -> new AppState("main", null, null, null)));
    }

    @ParameterizedTest(name = "{0} is required")
    @MethodSource("requiredFields")
    void rejectsMissingRequiredFields(String field, Supplier<?> constructor) {
        NullPointerException exception = assertThrows(NullPointerException.class, constructor::get);
        assertEquals(field + " must not be null", exception.getMessage());
    }

    @Test
    void screenCopiesAllListsAndExposesUnmodifiableResults() {
        Widget save = widget("save");
        List<String> reachableFrom = new ArrayList<>(List.of("login"));
        List<Widget> widgets = new ArrayList<>(List.of(save));
        List<String> variables = new ArrayList<>(List.of("user"));
        Screen screen = new Screen("main", "MainView", reachableFrom, widgets, variables);

        reachableFrom.clear();
        widgets.clear();
        variables.clear();

        assertAll(
                () -> assertEquals(List.of("login"), screen.reachableFrom()),
                () -> assertEquals(List.of(save), screen.widgets()),
                () -> assertEquals(List.of("user"), screen.globalVariables()),
                () -> assertThrows(UnsupportedOperationException.class, () -> screen.reachableFrom().add("other")),
                () -> assertThrows(UnsupportedOperationException.class, () -> screen.widgets().clear()),
                () -> assertThrows(UnsupportedOperationException.class, () -> screen.globalVariables().clear()));
    }

    @Test
    void testCaseCopiesBothListsAndExposesUnmodifiableResults() {
        Step first = step(1);
        List<String> preconditions = new ArrayList<>(List.of("App is open"));
        List<Step> steps = new ArrayList<>(List.of(first));
        TestCase testCase = new TestCase("TC1", "Save", preconditions, steps);

        preconditions.clear();
        steps.clear();

        assertAll(
                () -> assertEquals(List.of("App is open"), testCase.preconditions()),
                () -> assertEquals(List.of(first), testCase.steps()),
                () -> assertThrows(UnsupportedOperationException.class, () -> testCase.preconditions().clear()),
                () -> assertThrows(UnsupportedOperationException.class, () -> testCase.steps().clear()));
    }

    @Test
    void widgetAndAppStateCopyMapsAndExposeUnmodifiableResults() {
        Map<String, String> metadata = new HashMap<>(Map.of("discoveredBy", "StaticAnalysisEngine"));
        Map<String, Object> variables = new HashMap<>(Map.of("count", 2));
        Map<String, String> runtimeMetadata = new HashMap<>(Map.of("os", "linux"));
        Widget widget = new Widget("save", "Button", null, null, null, metadata);
        AppState state = new AppState("main", variables, TIMESTAMP, runtimeMetadata);

        metadata.clear();
        variables.clear();
        runtimeMetadata.clear();

        assertAll(
                () -> assertEquals(Map.of("discoveredBy", "StaticAnalysisEngine"), widget.metadata()),
                () -> assertEquals(Map.of("count", 2), state.activeVariables()),
                () -> assertEquals(Map.of("os", "linux"), state.runtimeMetadata()),
                () -> assertThrows(UnsupportedOperationException.class, () -> widget.metadata().clear()),
                () -> assertThrows(UnsupportedOperationException.class, () -> state.activeVariables().clear()),
                () -> assertThrows(UnsupportedOperationException.class, () -> state.runtimeMetadata().clear()));
    }

    static Stream<Arguments> nullCollectionElements() {
        return Stream.of(
                Arguments.of("reachableFrom", (Supplier<?>) () -> new Screen("main", null, Arrays.asList((String) null), null, null)),
                Arguments.of("widgets", (Supplier<?>) () -> new Screen("main", null, null, Arrays.asList((Widget) null), null)),
                Arguments.of("globalVariables", (Supplier<?>) () -> new Screen("main", null, null, null, Arrays.asList((String) null))),
                Arguments.of("preconditions", (Supplier<?>) () -> new TestCase("TC1", "Save", Arrays.asList((String) null), null)),
                Arguments.of("steps", (Supplier<?>) () -> new TestCase("TC1", "Save", null, Arrays.asList((Step) null))));
    }

    @ParameterizedTest(name = "{0} rejects null elements")
    @MethodSource("nullCollectionElements")
    void rejectsNullListElements(String field, Supplier<?> constructor) {
        assertThrows(NullPointerException.class, constructor::get, field);
    }

    @Test
    void rejectsNullMapKeysAndValues() {
        Map<String, String> nullKey = new HashMap<>();
        nullKey.put(null, "value");
        Map<String, String> nullValue = new HashMap<>();
        nullValue.put("key", null);

        for (Map<String, String> invalid : List.of(nullKey, nullValue)) {
            assertAll(
                    () -> assertThrows(NullPointerException.class,
                            () -> new Widget("save", "Button", null, null, null, invalid)),
                    () -> assertThrows(NullPointerException.class,
                            () -> new AppState("main", new HashMap<>(invalid), TIMESTAMP, null)),
                    () -> assertThrows(NullPointerException.class,
                            () -> new AppState("main", null, TIMESTAMP, invalid)));
        }
    }

    @Test
    void widgetLookupHandlesMissingNullAndDuplicateIds() {
        Widget first = widget("save");
        Widget duplicate = new Widget("save", "Button", "other-path", "Other Save", null, null);
        Screen screen = new Screen("main", "MainView", null,
                List.of(widget("cancel"), first, duplicate), null);

        assertAll(
                () -> assertSame(first, screen.findWidgetById("save").orElseThrow()),
                () -> assertTrue(screen.findWidgetById("missing").isEmpty()),
                () -> assertTrue(screen.findWidgetById(null).isEmpty()),
                () -> assertTrue(new Screen("empty", null, null, null, null).findWidgetById("save").isEmpty()));
    }

    static Stream<Arguments> stepSequences() {
        return Stream.of(
                Arguments.of(List.of(), true),
                Arguments.of(List.of(step(1)), true),
                Arguments.of(List.of(step(1), step(2), step(3)), true),
                Arguments.of(List.of(step(0)), false),
                Arguments.of(List.of(step(-1)), false),
                Arguments.of(List.of(step(2)), false),
                Arguments.of(List.of(step(1), step(3)), false),
                Arguments.of(List.of(step(1), step(1)), false),
                Arguments.of(List.of(step(2), step(1)), false));
    }

    @ParameterizedTest
    @MethodSource("stepSequences")
    void stepsMustBeNumberedFromOneInListOrder(List<Step> steps, boolean expected) {
        assertEquals(expected, new TestCase("TC1", "Save", null, steps).areStepsSequential());
    }

    @Test
    void analysisResultWithMissingOptionalFieldsSurvivesJsonRoundTrip() throws Exception {
        // Analysis may discover an ID and type without resolving paths, labels or tooltips.
        String json = """
                {"screenId":"main","widgets":[{"widgetId":"save","type":"Button"}]}
                """;
        Screen screen = mapper.readValue(json, Screen.class);
        Screen expected = new Screen("main", null, null,
                List.of(new Widget("save", "Button", null, null, null, null)), null);

        assertEquals(expected, screen);
        assertEquals(expected, mapper.readValue(mapper.writeValueAsString(screen), Screen.class));
        assertTrue(screen.findWidgetById("save").isPresent());
    }

    @Test
    void emptyAnalysisResultWithExplicitNullCollectionsSurvivesJsonRoundTrip() throws Exception {
        Screen screen = mapper.readValue("""
                {"screenId":"empty","viewClass":null,"reachableFrom":null,
                 "widgets":null,"globalVariables":null}
                """, Screen.class);

        assertEquals(new Screen("empty", null, List.of(), List.of(), List.of()), screen);
        assertEquals(screen, mapper.readValue(mapper.writeValueAsString(screen), Screen.class));
    }

    @ParameterizedTest
    @EnumSource(ValidationType.class)
    void eachValidationTypeAndNullExpectedResultSurviveJsonRoundTrip(ValidationType type) throws Exception {
        Step step = new Step(1, "Check application", null, type);
        Step restored = mapper.readValue(mapper.writeValueAsString(step), Step.class);

        assertEquals(step, restored);
        assertEquals(type, restored.validationType());
        assertNull(restored.expectedResult());
    }

    @Test
    void appStatePreservesTimestampPrecisionAndMixedVariableTypes() throws Exception {
        AppState state = new AppState("main", Map.of("count", 2, "ready", true, "user", "Ada"),
                TIMESTAMP, Map.of());
        AppState restored = mapper.readValue(mapper.writeValueAsString(state), AppState.class);

        assertEquals(state, restored);
        assertEquals(TIMESTAMP, restored.timestamp());
        assertEquals("main", restored.currentScreenId());
    }
}
