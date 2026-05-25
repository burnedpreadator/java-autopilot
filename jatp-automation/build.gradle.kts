plugins {
    id("java-library")
}

dependencies {
    implementation(project(":jatp-core"))
    implementation(project(":jatp-inspector"))

    // Automation frameworks
    implementation("org.swtbot:swtbot:0.1.0") // Placeholder, will need exact coordinates for RCPTT/SikuliX

    // Vision/OCR
    implementation("org.openpnp:opencv:4.9.0-0")

    implementation("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}
