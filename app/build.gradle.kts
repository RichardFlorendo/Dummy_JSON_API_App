import java.util.Properties

//Read from local.properties
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-parcelize") //Added to Parcelize, by serializing and de-serializing
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.dummyjson_app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.dummyjson_app"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        //Add PRODUCTS_BASE_URL from local.properties to BuildConfig
        val productApiBaseUrl: String = localProperties["PRODUCT_API_BASE_URL"] as String
        buildConfigField("String", "PRODUCT_API_BASE_URL", "\"$productApiBaseUrl\"")

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    //Room Database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    //Navigation
    implementation(libs.androidx.navigation.compose)

    //Compose ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    //Network Calls
    implementation(libs.retrofit)

    //Mapping JSON files to Kotlin Objects
    implementation(libs.converter.gson)

    //Image loading
    implementation(libs.coil.compose)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}