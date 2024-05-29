// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
    }
    dependencies {
        classpath ("com.android.tools.build:gradle:7.0.4")
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.0")
        classpath("com.google.gms:google-services:4.4.1")
    }
}

plugins {
    id("com.android.library") version "7.3.0" apply false
    id("com.android.application") version "8.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
}