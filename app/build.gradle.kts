plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("plugin.serialization")
    kotlin("plugin.parcelize")
}

android {
    namespace = "ru.kvf.gally"
    compileSdk = 34

    defaultConfig {
        applicationId = "ru.kvf.gally"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            debug {
                isMinifyEnabled = false
                isDebuggable = true
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_19
        targetCompatibility = JavaVersion.VERSION_19
    }
    kotlinOptions {
        jvmTarget = "19"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.bundles.orbit)
    implementation(libs.bundles.koin)
    implementation(libs.bundles.coroutines)
    implementation(libs.bundles.compose)
    implementation(libs.coil)
    implementation(libs.bundles.android)
    debugImplementation(libs.compose.debug.ui.tooling)
}
