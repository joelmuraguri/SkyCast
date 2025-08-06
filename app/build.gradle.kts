import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.serialization)
//    alias(libs.plugins.google.gms.google.services)
//    alias(libs.plugins.google.firebase.crashlytics)

}

val localProps = Properties().apply {
    val localFile = rootProject.file("local.properties")
    if (localFile.exists()) {
        load(FileInputStream(localFile))
    }
}

android {
    namespace = "com.joe.skycast"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.joe.skycast"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "beta-1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }


    }

    signingConfigs {
        create("release") {
            storeFile = file(localProps["RELEASE_STORE_FILE"] as String)
            storePassword = localProps["RELEASE_STORE_PASSWORD"] as String
            keyAlias = localProps["RELEASE_KEY_ALIAS"] as String
            keyPassword = localProps["RELEASE_KEY_PASSWORD"] as String
        }
    }



//    signingConfigs {
//        create("release") {
//            storeFile = file("release-key.jks")
//            storePassword = "joel2025"
//            keyAlias = "my-key-alias"
//            keyPassword = "joel2025"
//        }
//    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(project(":core:sync"))
    implementation(project(":core:network"))
    implementation(project(":core:models"))
    implementation(project(":core:supabase"))
    implementation(project(":presentation:home"))
    implementation(project(":presentation:locations"))
    implementation(project(":presentation:settings"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.play.services.auth)
    implementation(libs.androidx.lifecycle.viewmodel.android)
    implementation(libs.androidx.lifecycle.viewmodel.android)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //hilt
    implementation(libs.hilt)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    //hilt-worker
    ksp(libs.hilt.ext.compiler)
    implementation(libs.hilt.ext.work)

    //WorkManager
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.common)
    androidTestImplementation(libs.androidx.work.testing)

    implementation(libs.kotlinx.serialization.json)

    //supabase
    implementation(libs.ktor.client.android)
    implementation(platform("io.github.jan-tennert.supabase:bom:3.1.3"))
    implementation("io.github.jan-tennert.supabase:auth-kt")
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    implementation("io.github.jan-tennert.supabase:storage-kt")

    //Coil
    implementation("io.coil-kt:coil-compose:2.2.2")
}