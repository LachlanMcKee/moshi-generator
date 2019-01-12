buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("com.vanniktech:gradle-maven-publish-plugin:0.7.0")
    }
}

plugins {
    java
    kotlin("jvm") version Dependencies.KOTLIN_VERSION
    id("com.vanniktech.maven.publish") version "0.7.0"
}

java {
    targetCompatibility = JavaVersion.VERSION_1_7
    sourceCompatibility = JavaVersion.VERSION_1_7
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Dependencies.KOTLIN_VERSION}")

    implementation(project(":library"))
    implementation(project(":compiler:base"))

    implementation("com.squareup:javapoet:${Dependencies.JAVAPOET_VERSION}")
    implementation("com.google.auto:auto-common:0.6")
    implementation("com.google.code.gson:gson:${Dependencies.GSON_VERSION}")
}
