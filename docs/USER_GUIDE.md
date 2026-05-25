# User Guide: JavaAutoPilot (Phase 1 MVP)

Welcome to JavaAutoPilot! This guide explains how to get started with the current MVP implementation.

## 1. Overview
JavaAutoPilot (JATP) provides a set of tools to analyze Java desktop applications (specifically Eclipse RCP/SWT) and automate interactions with them via a REST API.

### Current Capabilities (Phase 1)
- **Static Analysis**: Discover screens and widgets from source code.
- **Runtime Inspection**: Attach to a running app to dump the SWT widget tree.
- **Hybrid Automation**: Click and type into widgets using multiple strategies (e.g., SWTBot).
- **REST API**: Control the entire process remotely.

---

## 2. Getting Started

### Prerequisites
- Java 17+ (JDK)
- Gradle (or use the provided `./gradlew`)

### Building the Project
Run the following command from the project root:
```bash
./gradlew build
```

### Running the API Server
The API server is the main entry point for controlling JavaAutoPilot.
```bash
./gradlew :jatp-api:bootRun
```
The server will start on `http://localhost:8080`.

---

## 3. Using the API

### Runtime Inspection
**Get All Widgets**
`GET /api/inspector/widgets`
- **Returns**: A list of all discovered widgets in the currently active window.
- **Use Case**: Discovering the IDs and paths of elements you want to interact with.

**Get Current Screen**
`GET /api/inspector/current-screen`
- **Returns**: The ID of the currently active screen.

### Automation Actions
**Click a Widget**
`POST /api/automation/click?widgetId={ID}`
- **Action**: Finds the widget by ID and attempts to click it using the Hybrid Automation Bridge.
- **Example**: `POST http://localhost:8080/api/automation/click?widgetId=saveButton`

**Type Text into a Widget**
`POST /api/automation/type?widgetId={ID}&text={TEXT}`
- **Action**: Sets the text value of the specified widget.
- **Example**: `POST http://localhost:8080/api/automation/type?widgetId=usernameField&text=admin`

---

## 4. Developer API (Programmatic Use)

If you are building extensions, you can use the core engines directly:

### Static Analysis
Use `EclipseRCPAnalyzer` to scan a source directory:
```java
EclipseRCPAnalyzer analyzer = new EclipseRCPAnalyzer();
Map<String, Screen> screens = analyzer.analyzeRCPProject(Paths.get("path/to/source"));
```

### Runtime Inspection
Use `SwtTreeInspector` to dump the current UI tree:
```java
SwtTreeInspector inspector = new SwtTreeInspector();
inspector.dumpWidgetTree().thenAccept(widgets -> {
    widgets.forEach(w -> System.out.println(w.label()));
});
```
