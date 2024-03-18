pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    val kotlinVersion = "1.9.21"
    val androidPluginVersion = "8.2.0"

    plugins {
        id("com.android.application") version androidPluginVersion
        id("org.jetbrains.kotlin.android") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
        kotlin("plugin.parcelize") version kotlinVersion
        id("com.android.library") version "8.2.0"
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }

    versionCatalogs {
        create("libs") {
            val koinVersion = "3.5.0"
            val coroutinesVersion = "1.7.3"
            val coilVersion = "2.5.0"
            val coreKtxVersion = "1.12.0"
            val lifecycleKtx = "2.6.2"
            val activityVersion = "1.8.2"
            val composeVersion = "1.5.4"
            val composeMaterial3Version = "1.1.2"
            val splashVersion = "1.0.0"
            val protoVersion = "1.0.0"
            val kotlinSerializationVersion = "1.6.0"
            val zoomableVersion = "1.5.3"
            val decomposeVersion = "2.2.2"
            val media3Version = "1.2.0"

            library("koin-core", "io.insert-koin:koin-core:$koinVersion")
            library("koin-android", "io.insert-koin:koin-androidx-compose:$koinVersion")
            bundle("koin", listOf("koin-core", "koin-android"))

            library("coroutines-core", "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
            library("coroutines-android", "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
            bundle("coroutines", listOf("coroutines-core", "coroutines-android"))

            library("coil-base", "io.coil-kt:coil-compose:$coilVersion")
            library("coil-video", "io.coil-kt:coil-video:$coilVersion")
            bundle("coil", listOf("coil-base", "coil-video"))

            library("android-core", "androidx.core:core-ktx:$coreKtxVersion")
            library("android-lifecycle", "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleKtx")
            library("android-activity", "androidx.activity:activity-compose:$activityVersion")
            bundle("android", listOf("android-core", "android-lifecycle", "android-activity"))

            library("compose-ui", "androidx.compose.ui:ui:$composeVersion")
            library("compose-ui-util", "androidx.compose.ui:ui-util:$composeVersion")
            library("compose-ui-graphics", "androidx.compose.ui:ui-graphics:$composeVersion")
            library("compose-ui-tooling-preview", "androidx.compose.ui:ui-tooling-preview:$composeVersion")
            library("compose-material3", "androidx.compose.material3:material3:$composeMaterial3Version")
            library("compose-icons", "androidx.compose.material:material-icons-extended:$composeVersion")
            bundle(
                "compose",
                listOf(
                    "compose-ui",
                    "compose-ui-util",
                    "compose-ui-graphics",
                    "compose-ui-tooling-preview",
                    "compose-material3",
                    "compose-icons"
                )
            )

            library("decompose-core", "com.arkivanov.decompose:decompose:$decomposeVersion")
            library("decompose-ext", "com.arkivanov.decompose:extensions-compose-jetpack:$decomposeVersion")
            bundle("decompose", listOf("decompose-core", "decompose-ext"))

            library("compose-debug-ui-tooling", "androidx.compose.ui:ui-tooling:$composeVersion")

            library("splash", "androidx.core:core-splashscreen:$splashVersion")

            library("proto", "androidx.datastore:datastore-preferences:$protoVersion")

            library(
                "kotlin-serialization",
                "org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinSerializationVersion"
            )

            library("zoomable", "net.engawapg.lib:zoomable:$zoomableVersion")

            library("exoplayer", "androidx.media3:media3-exoplayer:$media3Version")
            library("media3-ui", "androidx.media3:media3-ui:$media3Version")
            bundle("media3", listOf("exoplayer", "media3-ui"))
        }
    }
}

rootProject.name = "Gally"
include(":app")
include(":core")
include(":media")
include(":favorite")
include(":settings")
include(":design")
include(":folders")
