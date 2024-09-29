plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services") // This will apply the google-services plugin
}

android {
    namespace = "com.example.opsc7312poe"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.opsc7312poe"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // Core libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Firebase libraries
    implementation(platform("com.google.firebase:firebase-bom:32.0.0")) // Use the latest BOM version
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database")

    // MPAndroidChart
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Testing libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
