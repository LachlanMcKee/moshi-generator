plugins {
    java
    kotlin("jvm") version ProjectProperties.Versions.KOTLIN_VERSION
}

java {
    targetCompatibility = JavaVersion.VERSION_1_7
    sourceCompatibility = JavaVersion.VERSION_1_7
}

dependencies {
    api(ProjectProperties.Dependencies.KOTLIN_STD_LIB)
    api(project(":library"))
    api(project(":compiler:base"))
    api(project(":compiler:standard"))
    api(ProjectProperties.Dependencies.JUNIT)
}
