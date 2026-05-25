package com.jatp.api.controllers;

import com.jatp.automation.bridge.AutomationBridge;
import com.jatp.core.model.Widget;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * REST Controller for triggering automation actions.
 */
@RestController
@RequestMapping("/api/automation")
public class AutomationController {

    private final AutomationBridge automationBridge = new AutomationBridge();

    /**
     * Clicks a specific widget.
     * @param widgetId The ID of the widget to click.
     * @return Success or failure response.
     */
    @PostMapping("/click")
    public ResponseEntity<String> clickWidget(@RequestParam String widgetId) {
        try {
            // Mock widget for demonstration
            Widget mockWidget = new Widget(widgetId, "Button", "path", "Save", "", Map.of());
            automationBridge.click(mockWidget);
            return ResponseEntity.ok("Widget " + widgetId + " clicked successfully.");
        } catch (AutomationBridge.AutomationException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    /**
     * Enters text into a widget.
     * @param widgetId The ID of the widget.
     * @param text The text to enter.
     * @return Success or failure response.
     */
    @PostMapping("/type")
    public ResponseEntity<String> typeText(@RequestParam String widgetId, @RequestParam String text) {
        // Simplified mock for MVP
        return ResponseEntity.ok("Text '" + text + "' entered into widget " + widgetId);
    }
}
