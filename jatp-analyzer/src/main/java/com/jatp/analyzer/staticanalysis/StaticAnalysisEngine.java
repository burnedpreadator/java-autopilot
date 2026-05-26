package com.jatp.analyzer.staticanalysis;

import com.jatp.core.model.Screen;
import com.jatp.core.model.Widget;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;

/**
 * Engine for performing static analysis on Java source code to discover application structure.
 */
public class StaticAnalysisEngine {

    private static final Set<String> UI_SUPERCLASSES = Set.of(
        "Composite", "Shell", "Dialog", "TrayDialog", "TitleAreaDialog",
        "JFrame", "JPanel", "JDialog", "JInternalFrame",
        "ViewPart", "EditorPart", "MultiPageEditorPart", "FormPage"
    );

    /**
     * Analyzes a source directory by recursively walking it to find all Java files,
     * parsing each one, and discovering screen/view classes and their widgets.
     *
     * @param sourcePath The path to the Java source code.
     * @return A map of screenId to Screen objects.
     */
    public Map<String, Screen> analyzeSource(Path sourcePath) throws IOException {
        Map<String, Screen> discoveredScreens = new HashMap<>();

        try (Stream<Path> files = Files.walk(sourcePath)) {
            List<Path> javaFiles = files
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".java"))
                .collect(Collectors.toList());

            for (Path javaFile : javaFiles) {
                try {
                    CompilationUnit cu = StaticJavaParser.parse(javaFile);
                    List<ClassOrInterfaceDeclaration> screenClasses =
                        cu.findAll(ClassOrInterfaceDeclaration.class).stream()
                           .filter(c -> isScreenCandidate(c))
                           .collect(Collectors.toList());

                    for (ClassOrInterfaceDeclaration cls : screenClasses) {
                        String screenId = cls.getFullyQualifiedName()
                            .orElse(cls.getNameAsString());
                        String viewClass = screenId;
                        List<Widget> widgets = extractWidgetsFromClass(cls);

                        discoveredScreens.put(screenId,
                            new Screen(screenId, viewClass, List.of(), widgets, List.of()));
                    }
                } catch (Exception e) {
                    System.err.println("[StaticAnalysis] Failed to parse: "
                        + javaFile + " - " + e.getMessage());
                }
            }
        }

        return discoveredScreens;
    }

    /**
     * Determines if a class declaration is likely a UI screen/view.
     */
    private boolean isScreenCandidate(ClassOrInterfaceDeclaration cls) {
        if (cls.isInterface() || cls.isAbstract()) {
            return false;
        }
        if (cls.getExtendedTypes().stream()
               .anyMatch(t -> UI_SUPERCLASSES.contains(t.getNameAsString()))) {
            return true;
        }
        String name = cls.getNameAsString();
        return name.endsWith("View") || name.endsWith("Screen")
            || name.endsWith("Dialog") || name.endsWith("Panel")
            || name.endsWith("Editor") || name.endsWith("Page");
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
                String name = field.getVariable(0).getNameAsString();
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
