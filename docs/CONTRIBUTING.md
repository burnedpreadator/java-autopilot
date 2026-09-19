# Contributing to JavaAutoPilot

First off, thank you for considering contributing to JavaAutoPilot! It's an ambitious project to bring AI-powered testing to Java desktop applications, and we'd love your help.

## How to Contribute

### 1. Find an Issue
Check the [GitHub Issues](https://github.com/burnedpreadator/java-autopilot/issues) tab. We use labels to help you find the right task:
- `good first issue`: Perfect for newcomers. Small fixes or tests.
- `help wanted`: More complex features that need specialized expertise.
- `bug`: Something is broken and needs a fix.
- `feature`: A new capability to be added to the platform.

### 2. Setup Your Environment
- **JDK 17+**: Ensure you have OpenJDK 17 or newer installed.
- **Gradle**: Use the provided `./gradlew` wrapper.
- **IDE**: IntelliJ IDEA is recommended for the best Java/Gradle experience.

### 3. Development Workflow
1. **Fork** the repository.
2. **Create a Feature Branch**: `git checkout -b feature/my-awesome-feature`.
3. **Implement your changes**:
   - Follow the existing project structure.
   - Add unit tests for any new logic in the `src/test` directory.
   - Use records for data models in `jatp-core`.
4. **Commit and Push**:
   - Use descriptive commit messages.
   - Push to your fork.
5. **Open a Pull Request**:
   - Describe the changes you've made.
   - Link to the issue you are resolving.

## Running the jatp-Core Tests

Run `./gradlew :jatp-core:check` (`.\gradlew.bat :jatp-core:check` on Windows)
to execute the core tests and enforce 100% line and branch coverage for each
class in `com.jatp.core.model`.

The HTML coverage report is generated at
`jatp-core/build/reports/jacoco/test/html/index.html`; the JUnit test report is at
`jatp-core/build/reports/tests/test/index.html`.

The core tests cover analysis-result records, including incomplete metadata and
empty screens. They do not execute Spoon: the current analyzer uses JavaParser,
and Spoon-based navigation analysis is still a backlog item.

## Coding Standards
- **Language**: Java 21 (Targeting Java 17 for compatibility).
- **Build System**: Gradle (Kotlin DSL).
- **Style**: Follow standard Java naming conventions.
- **Dependencies**: Please discuss any new major dependencies with the maintainers before adding them.

## Communication
If you have questions or want to propose a major architectural change, please open an issue or start a discussion in the GitHub Discussions tab.

Happy coding! 🚀
