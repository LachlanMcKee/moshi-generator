plugins {
    java
    kotlin("jvm") version Dependencies.KOTLIN_VERSION
    jacoco
}

jacoco {
    toolVersion = Dependencies.JACOCO_VERSION
}

java {
    targetCompatibility = JavaVersion.VERSION_1_8
    sourceCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(project(":compiler:testing:base"))
    implementation("com.google.truth:truth:0.34")
    implementation("com.google.testing.compile:compile-testing:0.11")
    implementation(files(org.gradle.internal.jvm.Jvm.current().getToolsJar()))
}
