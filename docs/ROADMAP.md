# JavaAutoPilot Implementation Roadmap

## 1. Phases Overview

The project is divided into four primary phases, moving from foundational analysis to full autonomous agentic testing.

### Phase 1: Foundations & Visibility (Months 1-3) - MVP
**Goal**: Be able to analyze a Java app's structure, detect the current screen at runtime, and trigger basic actions via REST.

- [ ] **Core Infrastructure**
  - Project scaffolding (Java 21, Spring Boot).
  - Basic plugin system for LLMs.
- [ ] **Eclipse RCP/SWT Analyzer**
  - Static analysis of plugins and views using JavaParser.
  - Generation of initial screen maps.
- [ ] **Runtime Inspector**
  - Java Agent to attach to the running application.
  - Ability to dump the SWT widget tree.
  - Detection of the active Perspective/View.
- [ ] **Test Case Parser**
  - Support for Markdown and Excel $\rightarrow$ Structured JSON.
- [ ] **REST API Layer**
  - Endpoints for `/inspect`, `/navigate`, and `/status`.
- [ ] **Basic Automation Bridge**
  - Simple SWTBot integration for clicks and text entry.

### Phase 2: AI-Driven Navigation & Self-Healing (Months 4-6)
**Goal**: Use AI to navigate the app without hardcoded locators and handle UI changes.

- [ ] **AI Navigation Engine**
  - Integration with LangChain4j.
  - Prompt engineering for "Next Action" based on screen maps and runtime state.
- [ ] **Intelligent Locator System**
  - Generation of self-healing locators (mixing labels, IDs, and visual cues).
  - Locator recovery mechanism when a widget is not found.
- [ ] **Runtime Data Tracking**
  - Tracking global variables and their flow across screens.
  - State snapshots before and after actions.
- [ ] **Basic Visual Navigation**
  - Real-time screen capture and overlay of AI's intended action.

### Phase 3: Agentic Framework & API Generation (Months 7-9)
**Goal**: Deploy specialized agents for different testing roles and expose the platform as a set of APIs.

- [ ] **Multi-Agent System**
  - Implementation of Screen, Validation, Runtime, and Repair agents using LangGraph.
  - Agent coordination for complex test flows.
- [ ] **Automatic OpenAPI Generation**
  - Analyzing successful test flows $\rightarrow$ Generating corresponding REST endpoints.
  - Swagger UI for the generated APIs.
- [ ] **External Integrations**
  - Robot Framework plugin for JATP.
  - Jenkins/GitHub Actions pipeline templates.
- [ ] **Advanced Vision Integration**
  - OpenCV for complex widget detection.
  - OCR for validating text in non-accessible components.

### Phase 4: Full Autonomy & Enterprise Hardening (Months 10-12)
**Goal**: Fully autonomous test generation and production-grade stability.

- [ ] **Autonomous Test Generation**
  - AI explores the app $\rightarrow$ Generates test cases based on feature analysis.
- [ ] **Enterprise Reporting**
  - Allure/JUnit/PDF report generation.
  - Performance tracking of test executions.
- [ ] **Production Hardening**
  - Full Kafka/Redis deployment for scalability.
  - Support for diverse Java environments (legacy 1.7, OSGi).
  - Security auditing for the Java Agent.

## 2. MVP Definition (Phase 1)

The MVP will demonstrate the "Golden Path":
1. User provides a path to an Eclipse RCP app and a Markdown test case.
2. JATP analyzes the code to find the "Project Settings" view.
3. JATP launches the app and detects that the "Welcome" screen is active.
4. JATP uses SWTBot to click "Project Settings".
5. JATP validates that the "Project Settings" view is now active via the Runtime Inspector.
6. The result is returned via a REST API call.

## 3. Production Plan

- **Deployment**: Containerized via Docker/K8s.
- **Observability**: Prometheus/Grafana for monitoring the AI agent's decision latency and success rate.
- **Security**: Signed Java Agents for production environments; OAuth2 for API access.
- **Scalability**: Use Kafka to queue test executions across multiple application instances (grid testing).
