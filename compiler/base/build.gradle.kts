import org.gradle.internal.jvm.Jvm

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
    implementation("com.squareup:javapoet:${Dependencies.JAVAPOET_VERSION}")
    implementation(files(Jvm.current().getToolsJar()))

    testImplementation("junit:junit:${Dependencies.JUNIT_VERSION}")
}
