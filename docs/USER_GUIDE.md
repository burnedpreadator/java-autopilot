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

### Configuration
Before running the server, you must configure the AI provider and the source code path.

1. **API Key**: Set the `AI_API_KEY` environment variable.
   ```bash
   export AI_API_KEY="your-api-key-here"
   ```
2. **Source Path**: Edit `jatp-api/src/main/resources/application.yml` and set `jatp.analyzer.source-path` to the absolute path of your Java project.

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

---

## 5. Example Eclipse RCP Project Paths

When configuring `jatp.analyzer.source-path`, the value should point to the root directory of your Eclipse RCP project.

### Windows Examples

```text
C:\workspace\my-rcp-app
C:\projects\eclipse-rcp-demo
D:\development\customer-portal
```

### Linux Examples

```text
/home/user/workspace/my-rcp-app
/home/user/projects/eclipse-rcp-demo
```

### macOS Examples

```text
/Users/user/workspace/my-rcp-app
/Users/user/projects/eclipse-rcp-demo
```

Example configuration:

```yaml
jatp:
  analyzer:
    source-path: C:\workspace\my-rcp-app
```

---

## 6. Sample Test Cases

The following examples demonstrate how JavaAutoPilot can be used to automate common Eclipse RCP workflows.

### Test Case 1: Login Screen Validation

**Objective**

Verify that a user can successfully log in.

**Steps**

1. Launch the application.
2. Locate the `usernameField` widget.
3. Enter `admin`.
4. Locate the `passwordField` widget.
5. Enter `password123`.
6. Click the `loginButton`.

**Expected Result**

The application navigates to the main dashboard screen.

---

### Test Case 2: Save Customer Form

**Objective**

Verify that customer information can be saved successfully.

**Steps**

1. Open the Customer Management screen.
2. Enter a customer name.
3. Enter a customer email address.
4. Click the `saveButton`.

**Expected Result**

A success message is displayed and the customer record is saved.

---

### Test Case 3: Screen Discovery

**Objective**

Verify that the runtime inspector can detect the active screen.

**Steps**

1. Launch the application.
2. Navigate to a known screen.
3. Call:

```http
GET /api/inspector/current-screen
```

**Expected Result**

The API returns the correct active screen identifier.

---

### Test Case 4: Widget Inspection

**Objective**

Verify that widgets can be discovered through runtime inspection.

**Steps**

1. Launch the application.
2. Call:

```http
GET /api/inspector/widgets
```

**Expected Result**

The API returns a list of available widgets with identifiers and metadata.
