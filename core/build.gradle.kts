plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    val minSdkVersion: Int by rootProject.extra
    namespace = "ru.kvf.core"
    compileSdk = 34

    defaultConfig {
        minSdk = minSdkVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.7"
    }
}

dependencies {
    api(libs.bundles.compose)
    api(libs.bundles.android)
    api(libs.bundles.coil)
    api(libs.bundles.koin)
    api(libs.proto)
    api(libs.zoomable)
    api(libs.bundles.decompose)
    api(libs.bundles.media3)
    implementation(libs.kotlin.serialization)
    debugImplementation(libs.compose.debug.ui.tooling)
}