plugins {
    id("java")
}

group = "edu.kit.riscjblockits"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(project(":view"))
}

tasks.test {
    useJUnitPlatform()
}