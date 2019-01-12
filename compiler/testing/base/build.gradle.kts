plugins {
    java
    kotlin("jvm") version Dependencies.KOTLIN_VERSION
}

java {
    targetCompatibility = JavaVersion.VERSION_1_7
    sourceCompatibility = JavaVersion.VERSION_1_7
}

dependencies {
    api("org.jetbrains.kotlin:kotlin-stdlib:${Dependencies.KOTLIN_VERSION}")
    api(project(":compiler:standard"))
    api("junit:junit:${Dependencies.JUNIT_VERSION}")
}
