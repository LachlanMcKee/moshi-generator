plugins {
    java
    kotlin("jvm") version ProjectProperties.Versions.KOTLIN_VERSION
    jacoco
}

jacoco {
    toolVersion = ProjectProperties.Versions.JACOCO_VERSION
}

java {
    targetCompatibility = JavaVersion.VERSION_1_7
    sourceCompatibility = JavaVersion.VERSION_1_7
}

dependencies {
    testImplementation(project(":compiler:testing:base"))
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.0.0")
}
