plugins {
    `java-library`
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.jetbrains:annotations:24.0.1")
    implementation("net.md-5:bungeecord-api:1.21-R0.1-SNAPSHOT") {
        exclude(group = "com.mojang", module = "brigadier")
    }
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

project.base.archivesName.set(rootProject.name)
group = "fr.flowsqy.eventannouncer"
version = "1.0.0-SNAPSHOT"

tasks.processResources {
    expand(Pair("version", version))
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}
