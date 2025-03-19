import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.plugin.compose)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.wordgame.wordpuzzles"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.wordgame.wordpuzzles"
        minSdk = 23
        targetSdk = 35
        versionCode = 7
        versionName = "1.4.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val localProperties = Properties()
        val localFile = rootProject.file("local.properties")
        if (localFile.exists()) {
            localProperties.load(FileInputStream(localFile))
        }

        fun getLocalProperty(key: String): String {
            return localProperties.getProperty(key, "")
        }

        // Read properties securely
        buildConfigField("String", "ADMOB_APP_ID", "\"${getLocalProperty("ADMOB_APP_ID")}\"")
        buildConfigField("String", "BANNER_AD_ID", "\"${getLocalProperty("BANNER_AD_ID")}\"")
        buildConfigField("String", "REWARDED_AD_ID", "\"${getLocalProperty("REWARDED_AD_ID")}\"")
        buildConfigField("String", "INTERSTITIAL_AD_ID", "\"${getLocalProperty("INTERSTITIAL_AD_ID")}\"")
        buildConfigField("String", "APP_OPEN_AD_ID", "\"${getLocalProperty("APP_OPEN_AD_ID")}\"")

        manifestPlaceholders["ADMOB_APP_ID"] = getLocalProperty("ADMOB_APP_ID")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
//    composeOptions {
//        kotlinCompilerExtensionVersion = "1.5.1"
//    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.firebase.analytics)

    implementation(libs.androidx.core.splashscreen)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.process)

    implementation (libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(libs.dagger.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.dagger.hilt.compiler)

    // Security
    implementation(libs.androidx.security.crypto)

    // Animation
    implementation(libs.lottie.compose)
    implementation(libs.accompanist.navigation.animation)
    implementation(libs.spin.wheel.compose)
    implementation(libs.flippable)

    // AdMob
    implementation(libs.play.services.ads)
    implementation(libs.gson)

    // Billing Library
    implementation(libs.guava)
    implementation(libs.billing)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}