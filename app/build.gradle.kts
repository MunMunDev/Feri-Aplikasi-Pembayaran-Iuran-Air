plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    // Dagger Hilt
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.munmundev.feri_aplikasipembayaraniuranair"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.munmundev.feri_aplikasipembayaraniuranair"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures{
        viewBinding = true
    }

}


kapt {
    correctErrorTypes = true
}

dependencies {

    val retrofitVersion = "2.9.0"
    val lifecycleVersion = "2.6.2"
    val coroutinesVersion = "1.6.4"
    val activityKTXVersion = "1.8.1"
    val fragmentKTXVersion = "1.6.2"

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    // lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")

    // KTX
    implementation("androidx.activity:activity-ktx:$activityKTXVersion")
    implementation("androidx.fragment:fragment-ktx:$fragmentKTXVersion")

    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")

    // MIDTRANS
    implementation ("com.midtrans:uikit:2.0.0-SANDBOX")
//    implementation ("com.midtrans:uikit:2.0.0")

    // Firebase
    implementation("com.google.firebase:firebase-messaging:23.4.0")

}

