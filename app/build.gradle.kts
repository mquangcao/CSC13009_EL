plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.kapt")
    id("kotlin-kapt")

}

android {
    namespace = "com.android_ai.csc13009"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.android_ai.csc13009"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // drag views
    buildFeatures{
        viewBinding = true
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.ktx)
    //implementation(libs.androidx.games.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    //kapt(libs.room.compiler)
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)

    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("com.mikhaellopez:circularprogressbar:3.1.0")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
}

