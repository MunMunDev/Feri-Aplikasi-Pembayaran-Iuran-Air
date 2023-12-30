// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false

    // Dagger Hilt
    id("com.google.dagger.hilt.android") version "2.44" apply false
}

buildscript {
    repositories {
        jcenter()
        maven(url = "https://jitpack.io")
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
    }
}
