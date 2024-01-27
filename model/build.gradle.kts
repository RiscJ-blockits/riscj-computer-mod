plugins {
    id("java")
    id("org.sonarqube") version "4.4.1.3373"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

group = "edu.kit.riscjblockits"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.google.code.gson:gson:2.10.1")
}

tasks.test {
    useJUnitPlatform()
}

sonar {
    properties {
        property ("sonar.projectKey", "kit_tva_pse_ws23_risc-simulator_riscjblockits_AY1K9l9XR63KR32WthiJ")
        property ("sonar.projectName", "riscjblockits")
        property ("sonar.qualitygate.wait", true)
    }
}
