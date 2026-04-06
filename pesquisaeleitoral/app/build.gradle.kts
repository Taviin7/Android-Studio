plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.pesquisa_eleitoral"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.pesquisa_eleitoral"
        minSdk = 24
        targetSdk = 36
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.play.services.location)
    implementation(libs.room.common.jvm)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    //implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    //Implementação do Room - BD
    val room_version = "2.7.0-alpha13" // Updated to support Kotlin 2.1 metadata
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
}