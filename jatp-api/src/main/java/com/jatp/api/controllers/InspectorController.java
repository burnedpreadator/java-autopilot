package com.jatp.api.controllers;

import com.jatp.core.model.Widget;
import com.jatp.inspector.runtime.SwtTreeInspector;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * REST Controller for application runtime inspection.
 */
@RestController
@RequestMapping("/api/inspector")
public class InspectorController {

    private final SwtTreeInspector swtTreeInspector = new SwtTreeInspector();

    /**
     * Dumps the current SWT widget tree of the running application.
     * @return A list of discovered widgets.
     */
    @GetMapping("/widgets")
    public CompletableFuture<ResponseEntity<List<Widget>>> getWidgets() {
        return swtTreeInspector.dumpWidgetTree()
                .thenApply(ResponseEntity::ok);
    }

    /**
     * Checks the current active screen ID.
     * @return The ID of the currently active screen.
     */
    @GetMapping("/current-screen")
    public ResponseEntity<String> getCurrentScreen() {
        // In a real implementation, this would query the RuntimeAgent/StateMonitor
        return ResponseEntity.ok("S1_DEFAULT");
    }
}
