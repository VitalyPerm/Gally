plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    val minSdkVersion: Int by rootProject.extra
    namespace = "ru.kvf.favorite"
    compileSdk = 34

    defaultConfig {
        minSdk = minSdkVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {
    implementation(project(":core"))
    implementation(libs.bundles.compose)
    implementation(libs.bundles.android)
    implementation(libs.bundles.orbit)
    debugImplementation(libs.compose.debug.ui.tooling)
}