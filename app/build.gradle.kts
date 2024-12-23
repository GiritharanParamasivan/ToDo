plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp") version "2.0.21-1.0.27"
}

android {
    namespace = "uk.ac.tees.mad.s3216191"
    compileSdk = 35

    defaultConfig {
        applicationId = "uk.ac.tees.mad.s3216191"
        minSdk = 26
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

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "2.0.0"
    }
}

dependencies {
    // Core Libraries
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.activity:activity-compose:1.9.3")

    // Jetpack Compose
    implementation(platform("androidx.compose:compose-bom:2024.11.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:2.7.4")
    implementation("androidx.compose.runtime:runtime-livedata:1.7.5")

    // Firebase
    implementation("com.google.firebase:firebase-auth-ktx:23.1.0")
    implementation("com.google.firebase:firebase-firestore-ktx:25.1.1")
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation("com.google.firebase:firebase-analytics")
    // Room Database
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.lifecycle.livedata.core.ktx)
    ksp("androidx.room:room-compiler:2.6.1")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.3")
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.3")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.5.3")


    implementation(libs.firebase.auth)
    implementation(libs.androidx.runtime.livedata)

    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    implementation ("androidx.activity:activity-compose:1.5.1")
    implementation ("io.coil-kt:coil-compose:2.2.2")
    implementation ("androidx.compose.material:material-icons-extended:1.5.1")


}