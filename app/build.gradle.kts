plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.circularscroll"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.circularscroll"
        minSdk = 30
        targetSdk = 37
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
    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.all {
            // Robolectric needs access to JDK internals for FileDescriptor handling
            it.jvmArgs("--add-exports=java.base/jdk.internal.access=ALL-UNNAMED")
        }
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.recyclerview)
    implementation(libs.material)
    testImplementation(libs.junit)
    testImplementation(libs.androidx.junit)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.robolectric)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}