# JavaAutoPilot Architecture

## 1. Vision & Goals
JavaAutoPilot (JATP) is an AI-powered testing platform for Java desktop applications. It aims to automate the entire testing lifecycle from test case understanding to execution and validation, specifically targeting legacy and complex Java environments (Eclipse RCP, SWT, Swing, JavaFX).

### Core Objectives
- **Zero-Configuration Automation**: Minimize manual effort in creating locators.
- **Technology Agnostic**: Support various Java versions and UI frameworks.
- **AI-Driven**: Use LLMs for high-level planning and low-level visual/structural reasoning.
- **Pluggable**: LLM providers and automation backends should be swappable via configuration.

## 2. High-Level Architecture

JATP follows a layered architecture to decouple AI reasoning from the low-level Java runtime manipulation.

### Layer 1: Interface Layer (API & Dashboard)
- **REST API**: Exposes endpoints for test execution, screen navigation, and system monitoring.
- **UI Dashboard**: A React-based frontend for visualizing the application state, test progress, and AI planning.
- **OpenAPI Integration**: Automatically generates API specs for discovered test flows.

### Layer 2: Orchestration Layer (AI Agents & Planner)
- **Test Planner Agent**: Converts natural language test cases into a structured execution plan.
- **Screen Agent**: Handles high-level navigation and screen identification.
- **Validation Agent**: Performs assertions and verifies runtime data.
- **Repair Agent**: Detects locator failures and attempts self-healing using AI.
- **Coordination**: Managed via LangGraph to maintain state and handle loops/retries.

### Layer 3: Analysis Layer (Static & Dynamic Understanding)
- **Source Code Engine**: Uses JavaParser and Spoon to analyze the codebase, generating screen maps and navigation graphs.
- **Runtime Inspector**: Attaches to the target Java process to detect active views, widgets, and global variables.
- **Test Case Parser**: Extracts structured steps from DOCX, PDF, Excel, etc.

### Layer 4: Execution Layer (Hybrid Automation Engine)
- **Automation Bridge**: A strategy-based engine that selects the best interaction method:
  1. Internal API $\rightarrow$ Reflection $\rightarrow$ Framework API (SWT/RCP) $\rightarrow$ Accessibility $\rightarrow$ CV/OCR $\rightarrow$ Mouse/Keyboard.
- **Visual Navigation**: Captures screens and simulates inputs in a "headed" mode for visibility.
- **State Monitor**: Tracks global variables and background jobs in real-time.

### Layer 5: Infrastructure Layer
- **Persistence**: PostgreSQL for test cases, screen maps, and execution history.
- **Caching**: Redis for session state and frequently accessed metadata.
- **Messaging**: Kafka for asynchronous eventing between the agentic layer and the execution engine.

## 3. Module Breakdown

### `jatp-core`
- Shared domain models (Screen, Widget, TestCase, Step).
- Configuration management.
- Plugin interfaces.

### `jatp-analyzer`
- Static analysis engine (JavaParser, Spoon).
- Eclipse JDT integration.
- Graph generators (Navigation, Dependency).

### `jatp-inspector`
- Runtime agent (Java Agent).
- Widget tree traversal.
- State extraction (Global variables, Active views).

### `jatp-automation`
- Hybrid interaction strategies.
- Integration with SWTBot, RCPTT, SikuliX.
- Screen capture and OCR.

### `jatp-ai`
- LLM Provider abstractions (LangChain4j).
- Agent definitions (Planning, Navigation, Validation).
- Prompt templates.

### `jatp-api`
- Spring Boot REST controllers.
- OpenAPI generator.
- Integration with CI/CD.

### `jatp-dashboard`
- React/TypeScript frontend.
- WebSocket integration for live screen updates.

## 4. Technology Stack

| Component | Technology |
| :--- | :--- |
| Language | Java 21 |
| Framework | Spring Boot 3.x |
| AI Orchestration | LangGraph, LangChain4j |
| Static Analysis | JavaParser, Spoon, Eclipse JDT |
| Automation | SWTBot, RCPTT, SikuliX, Playwright (for web-based parts) |
| Vision/OCR | OpenCV, Tesseract/Cloud Vision |
| Database | PostgreSQL |
| Messaging/Cache | Kafka, Redis |
| Frontend | React, TypeScript, Tailwind CSS |
| API | Spring Doc / OpenAPI 3.0 |

## 5. Execution Flow: A Single Test Step

1. **Request**: `POST /testcases/execute` $\rightarrow$ `jatp-api`.
2. **Planning**: `jatp-ai` Test Planner analyzes the test case $\rightarrow$ Generates a step: "Open FFD Screen".
3. **Analysis**: `jatp-ai` Screen Agent checks `jatp-analyzer` (static map) and `jatp-inspector` (current runtime state).
4. **Action**: `jatp-automation` selects the best method (e.g., Eclipse Command) $\rightarrow$ Executes interaction.
5. **Verification**: `jatp-ai` Validation Agent queries `jatp-inspector` $\rightarrow$ Verifies "FFD Screen" is active.
6. **Update**: Result stored in PostgreSQL; UI Dashboard updated via WebSocket.
