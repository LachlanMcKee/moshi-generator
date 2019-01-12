plugins {
    java
    kotlin("jvm") version Dependencies.KOTLIN_VERSION
    jacoco
}

jacoco {
    toolVersion = Dependencies.JACOCO_VERSION
}

java {
    targetCompatibility = JavaVersion.VERSION_1_7
    sourceCompatibility = JavaVersion.VERSION_1_7
}

dependencies {
    testImplementation(project(":compiler:testing:base"))
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.0.0")
}
