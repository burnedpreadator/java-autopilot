package com.jatp.inspector.runtime;

import com.jatp.core.model.Widget;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Widget;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Inspector for traversing the SWT widget tree at runtime.
 */
public class SwtTreeInspector {

    /**
     * Dumps the current SWT widget tree.
     * @return A future containing the list of discovered widgets.
     */
    public CompletableFuture<List<com.jatp.core.model.Widget>> dumpWidgetTree() {
        CompletableFuture<List<com.jatp.core.model.Widget>> future = new CompletableFuture<>();

        // SWT components must be accessed from the UI thread.
        Display.getDefault().asyncExec(() -> {
            try {
                List<com.jatp.core.model.Widget> widgets = new ArrayList<>();
                // Find the main shell/window
                org.eclipse.swt.widgets.Shell shell = Display.getDefault().getActiveShell();
                if (shell != null) {
                    traverseComposite(shell, widgets);
                }
                future.complete(widgets);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });

        return future;
    }

    private void traverseComposite(Composite composite, List<com.jatp.core.model.Widget> widgets) {
        for (org.eclipse.swt.widgets.Control child : composite.getChildren()) {
            // Add the widget to the list
            widgets.add(mapToCoreWidget(child));

            // If the child is also a composite, traverse its children recursively
            if (child instanceof Composite childComposite) {
                traverseComposite(childComposite, widgets);
            }
        }
    }

    private com.jatp.core.model.Widget mapToCoreWidget(Control control) {
        return new com.jatp.core.model.Widget(
            String.valueOf(control.hashCode()), // Using hashcode as temporary ID
            control.getClass().getName(),
            "runtime-path", // To be implemented: actual path in tree
            control.getText(),
            "", // Tooltips often require separate lookup
            Map.of("runtimeId", String.valueOf(control.hashCode()))
        );
    }
}
