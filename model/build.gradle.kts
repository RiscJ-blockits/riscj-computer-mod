plugins {
    id("java")
    id("org.openjfx.javafxplugin") version "0.0.8"
}

group = "edu.kit.riscjblockits"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.json:json:20231013")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.13.0")
}

tasks.test {
    useJUnitPlatform()
}

javafx {

    modules("javafx.base")


}