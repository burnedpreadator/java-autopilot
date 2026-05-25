plugins {
    id("java-library")
}

dependencies {
    implementation(project(":jatp-core"))
    implementation(project(":jatp-analyzer"))
    implementation(project(":jatp-inspector"))
    implementation(project(":jatp-automation"))

    // AI Orchestration
    implementation("dev.langchain4j:langchain4j-core:0.28.0")
    implementation("dev.langchain4j:langchain4j-open-ai:0.28.0")
    // implementation("dev.langchain4j:langchain4j-anthropic:0.28.0")
    implementation("io.github.langchain4j:langchain4j-ollama:0.28.0")

    implementation("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}
