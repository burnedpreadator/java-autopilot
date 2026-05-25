package com.jatp.analyzer.staticanalysis;

import com.jatp.core.model.Screen;
import com.jatp.core.model.Widget;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;

/**
 * Engine for performing static analysis on Java source code to discover application structure.
 */
public class StaticAnalysisEngine {

    /**
     * Analyzes a source directory and returns a map of discovered screens.
     * @param sourcePath The path to the Java source code.
     * @return A map of screenId to Screen objects.
     */
    public Map<String, Screen> analyzeSource(Path sourcePath) throws IOException {
        // This is a simplified implementation for the MVP.
        // In a real scenario, it would recursively walk the directory.
        Map<String, Screen> discoveredScreens = new HashMap<>();

        // For the MVP, we'll implement basic discovery of classes that might be screens.
        // This will be expanded in the Eclipse-specific analyzer.
        return discoveredScreens;
    }

    /**
     * Simple helper to extract potential widgets from a class's field declarations.
     */
    public List<Widget> extractWidgetsFromClass(ClassOrInterfaceDeclaration classDec) {
        List<Widget> widgets = new ArrayList<>();

        // Look for fields that are common SWT/Swing types
        for (FieldDeclaration field : classDec.getFields()) {
            String type = field.getElementType().asString();
            if (isUiComponent(type)) {
                String name = field.getVariable(0).getName();
                widgets.add(new Widget(
                    name,
                    type,
                    "static-analysis",
                    name, // Simplified: using name as label
                    null,
                    Map.of("discoveredBy", "StaticAnalysisEngine")
                ));
            }
        }

        return widgets;
    }

    private boolean isUiComponent(String type) {
        return type.contains("Button") ||
               type.contains("Text") ||
               type.contains("Label") ||
               type.contains("Combo") ||
               type.contains("Table") ||
               type.contains("Canvas");
    }
}
