plugins {
    id("java-library")
}

dependencies {
    implementation(project(":jatp-core"))

    // Bytecode manipulation for Java Agent
    implementation("net.bytebuddy:byte-buddy:1.14.12")
    implementation("net.bytebuddy:byte-buddy-agent:1.14.12")

    implementation("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}
