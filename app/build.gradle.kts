import org.jetbrains.kotlin.gradle.utils.loadPropertyFromResources
import java.util.Properties


plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.bonghwan.mosquito"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.bonghwan.mosquito"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // 'local.properties' 파일을 읽어옴
    val localProperties = Properties()
    localProperties.load(rootProject.file("local.properties").inputStream())

    // 'local.properties' 파일에서 원하는 속성을 읽어옴
    val kakaoSdkKey = localProperties.getProperty("kakaoSdkKey")

    defaultConfig.manifestPlaceholders(mapOf("kakaoSdkKey" to kakaoSdkKey))

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // Retrofit2
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // OkHttp3
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    // Coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
    // SplashScreen API
    // implementation("androidx.core:core-splashscreen:1.0.1")
    // Room
    val roomVersion = "2.6.0"
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    androidTestImplementation("androidx.room:room-testing:2.6.0")
    implementation("androidx.room:room-ktx:2.6.0")
    implementation("androidx.activity:activity-ktx:1.8.0")
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    // LiveData
    implementation ("androidx.databinding:databinding-runtime:8.1.2")
    implementation ("androidx.activity:activity-ktx:1.8.0")
    // Chart
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    /* Kakao Login */
    implementation ("com.kakao.sdk:v2-user:2.17.0")
    implementation ("com.kakao.sdk:v2-user-rx:2.17.0")
    /* AndroidX Security */
    implementation("androidx.security:security-crypto-ktx:1.1.0-alpha06")
    /* Material CalendarView */
    implementation("com.github.prolificinteractive:material-calendarview:2.0.1")
    implementation("com.jakewharton.threetenabp:threetenabp:1.2.0")
    /* Firebase */
    implementation(platform("com.google.firebase:firebase-bom:32.5.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging-ktx:23.3.1")
    implementation("com.google.firebase:firebase-messaging-directboot:23.3.1")
}