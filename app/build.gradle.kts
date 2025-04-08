plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.exe1"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.exe1"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}


dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    implementation(libs.recyclerview)
    implementation(libs.volley)

    implementation(libs.square.retrofit)
    implementation(libs.square.retrofit.gson)
    implementation(libs.square.okhttp.logging)

    implementation(libs.glide)




}