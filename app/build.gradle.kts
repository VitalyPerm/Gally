plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("plugin.serialization")
    kotlin("plugin.parcelize")
}

android {
    signingConfigs {
        create("release") {
            storeFile = file("/Users/vitalya/StudioProjects/Gally/new_key.jks")
            storePassword = "123456"
            keyAlias = "new_alias"
            keyPassword = "123456"
        }
        // keytool -genkey -v -keystore new_keystore.jks -alias new_alias -keyalg RSA -keysize 2048 -validity 10000
    }
    val minSdkVersion: Int by rootProject.extra
    namespace = "ru.kvf.gally"
    compileSdk = 34

    defaultConfig {
        applicationId = "ru.kvf.gally"
        minSdk = minSdkVersion
        targetSdk = 34
        versionCode = 3
        versionName = "0.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }

        debug {
            applicationIdSuffix = ".debug"
            resValue("string", "app_name", "GalDebug")
        }    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        if (project.findProperty("composeCompilerReports") == "true") {
            freeCompilerArgs += listOf(
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${project.buildDir.absolutePath}/compose_compiler"
            )
        }
        if (project.findProperty("composeCompilerMetrics") == "true") {
            freeCompilerArgs += listOf(
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${project.buildDir.absolutePath}/compose_compiler"
            )
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
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
    implementation(project(":core"))
    implementation(project(":feature"))
    implementation(project(":folders"))
    implementation(project(":settings"))
    implementation(libs.splash)
}
