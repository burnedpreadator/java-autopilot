# Repository Structure

JavaAutoPilot uses a Gradle multi-module build system to ensure a clean separation of concerns and allow for independent development and testing of components.

## Module Hierarchy

```text
java-autopilot/
├── build.gradle.kts          # Root build configuration
├── settings.gradle.kts       # Module definitions
├── gradle.properties          # Project-wide properties
├── docs/                      # Architecture, Roadmap, API specs
├── scripts/                   # Automation scripts for setup and deployment
│
├── jatp-core/                 # Shared domain models, interfaces, and utilities
│   └── src/main/java/com/jatp/core/
│       ├── model/             # Screen, Widget, TestCase, Step, State
│       ├── config/            # App configuration and plugin registries
│       └── spi/              # Service Provider Interfaces for plugins
│
├── jatp-analyzer/             # Static analysis engine
│   └── src/main/java/com/jatp/analyzer/
│       ├── static/            # JavaParser/Spoon implementations
│       ├── eclipse/           # JDT and RCP specific analysis
│       └── graph/             # Navigation and Dependency graph builders
│
├── jatp-inspector/            # Runtime agent and state extraction
│   └── src/main/java/com/jatp/inspector/
│       ├── agent/             # Java Agent bytecode manipulation
│       ├── runtime/            # SWT/AWT/JavaFX tree traversal
│       └── state/             # Global variable and job monitoring
│
├── jatp-automation/           # Hybrid automation engine
│   └── src/main/java/com/jatp/automation/
│       ├── bridge/            # Interaction strategy selector
│       ├── drivers/           # SWTBot, RCPTT, SikuliX implementations
│       └── vision/            # OpenCV and OCR integration
│
├── jatp-ai/                   # AI Orchestration and LLM integration
│   └── src/main/java/com/jatp/ai/
│       ├── providers/         # LangChain4j wrappers (OpenAI, Claude, etc.)
│       ├── agents/            # Specialized agent logic (Planner, Screen, etc.)
│       └── graph/             # LangGraph orchestration flows
│
├── jatp-api/                  # REST API and OpenAPI generation
│   └── src/main/java/com/jatp/api/
│       ├── controllers/      # Spring Boot REST endpoints
│       ├── openapi/           # Automatic API spec generator
│       └── integration/       # CI/CD and Robot Framework bridges
│
└── jatp-dashboard/            # React Frontend
    ├── package.json
    ├── src/
    │   ├── components/        # Live viewer, Timeline, Variable tracker
    │   ├── hooks/            # WebSocket and API integrations
    │   └── pages/            # Dashboard views
    └── public/
```

## Module Responsibilities

| Module | Responsibility | Dependency |
| :--- | :--- | :--- |
| `jatp-core` | Domain models and common SPIs. | None |
| `jatp-analyzer` | Static code analysis $\rightarrow$ Screen Maps. | `jatp-core` |
| `jatp-inspector` | App Attachment $\rightarrow$ Runtime State. | `jatp-core` |
| `jatp-automation` | Action Execution $\rightarrow$ UI Change. | `jatp-core`, `jatp-inspector` |
| `jatp-ai` | Reasoning $\rightarrow$ Execution Plan. | `jatp-core`, `jatp-analyzer`, `jatp-inspector`, `jatp-automation` |
| `jatp-api` | External Access $\rightarrow$ Orchestration. | `jatp-ai`, `jatp-core` |
| `jatp-dashboard` | Visualization $\rightarrow$ User Interaction. | `jatp-api` (via HTTP/WS) |

## Development Workflow

1. **Define Model**: Add new entities to `jatp-core`.
2. **Implement SPI**: Define a new interface in `jatp-core` (e.g., `WidgetLocator`).
3. **Build Provider**: Implement the interface in `jatp-automation` (e.g., `SwtBotWidgetLocator`).
4. **Integrate AI**: Use the provider within `jatp-ai` agents.
5. **Expose API**: Create a controller in `jatp-api` to trigger the flow.
