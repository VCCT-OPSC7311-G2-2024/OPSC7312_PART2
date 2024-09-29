// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google() // Add the Google repository
        mavenCentral()
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.2") // Add this for Google Services
    }
}

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
}

allprojects {

}

