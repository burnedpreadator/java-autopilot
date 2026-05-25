package com.jatp.analyzer.eclipse;

import com.jatp.analyzer.staticanalysis.StaticAnalysisEngine;
import com.jatp.core.model.Screen;
import com.jatp.core.model.Widget;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

/**
 * Specialized analyzer for Eclipse RCP applications.
 */
public class EclipseRCPAnalyzer extends StaticAnalysisEngine {

    /**
     * Discovers screens specifically for an Eclipse RCP project.
     * @param sourcePath Path to the project source.
     * @return Map of discovered screens.
     */
    public Map<String, Screen> analyzeRCPProject(Path sourcePath) throws IOException {
        Map<String, Screen> screens = new HashMap<>();

        // Walk through all .java files in the source path
        Files.walk(sourcePath)
             .filter(p -> p.toString().endsWith(".java"))
             .forEach(p -> {
                 try {
                     CompilationUnit cu = StaticJavaParser.parse(p);
                     cu.findAll(ClassOrInterfaceDeclaration.class).forEach(cls -> {
                         if (isEclipseView(cls)) {
                             String screenId = cls.getNameAsString();
                             List<Widget> widgets = extractWidgetsFromClass(cls);

                             Screen screen = new Screen(
                                 screenId,
                                 cls.getFullyQualifiedName().orElse(cls.getNameAsString()),
                                 new ArrayList<>(),
                                 widgets,
                                 new ArrayList<>()
                             );
                             screens.put(screenId, screen);
                         }
                     });
                 } catch (IOException e) {
                     // Log error and continue with other files
                 }
             });

        return screens;
    }

    private boolean isEclipseView(ClassOrInterfaceDeclaration cls) {
        // Check if it extends ViewPart or implements IViewPart
        return cls.getExtendedTypes().stream().anyMatch(t -> t.asString().contains("ViewPart")) ||
               cls.getImplementedTypes().stream().anyMatch(t -> t.asString().contains("IViewPart"));
    }
}
