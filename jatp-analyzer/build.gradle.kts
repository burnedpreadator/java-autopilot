plugins {
    id("java-library")
}

dependencies {
    implementation(project(":jatp-core"))

    // Static Analysis
    implementation("com.github.javaparser:javaparser-core:3.25.9")
    implementation("fr.inria.spoon:spoon-core:10.4.0")

    implementation("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}
