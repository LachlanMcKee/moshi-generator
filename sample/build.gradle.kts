plugins {
    java
    kotlin("jvm") version Dependencies.KOTLIN_VERSION
    kotlin("kapt") version Dependencies.KOTLIN_VERSION
}

java {
    targetCompatibility = JavaVersion.VERSION_1_8
    sourceCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Dependencies.KOTLIN_VERSION}")
    implementation("com.google.code.gson:gson:${Dependencies.GSON_VERSION}")

    implementation(project(":library"))
    kapt(project(":compiler:standard"))

    testImplementation("junit:junit:${Dependencies.JUNIT_VERSION}")
}