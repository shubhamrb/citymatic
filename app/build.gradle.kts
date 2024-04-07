plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.mamits.citymatic"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mamits.citymatic"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
        android.buildFeatures.buildConfig = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = false
            buildConfigField("String", "BASE_URL", "\"https://zini24.com/citymatic/\"")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
            buildConfigField("String", "BASE_URL", "\"https://zini24.com/citymatic/\"")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    dataBinding {
        enable = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    configurations {
        all {
            exclude(group = "org.bytedeco", module = "javacpp-presets")
        }
    }

    lint {
        checkReleaseBuilds = false
        abortOnError = false
    }
    packaging {
        resources.excludes.add("META-INF/INDEX.LIST")
        resources.excludes.add("META-INF/DEPENDENCIES")
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")

    implementation("com.google.dagger:dagger-android:2.35.1")
    implementation("com.google.dagger:dagger-android-support:2.35.1")
    implementation("androidx.navigation:navigation-fragment:2.3.5")

    annotationProcessor("com.google.dagger:dagger-android-processor:2.35.1")
    implementation("com.google.dagger:dagger:2.35.1")
    annotationProcessor("com.google.dagger:dagger-compiler:2.35.1")

    implementation("androidx.multidex:multidex:2.0.1")

    // parser
    implementation("com.google.code.gson:gson:2.8.6")
    /*retrofit*/
    implementation("com.squareup.retrofit2:retrofit:2.6.1")
    implementation("com.squareup.retrofit2:retrofit-converters:2.6.1")
    implementation("com.squareup.retrofit2:retrofit-adapters:2.6.1")
    implementation("com.squareup.retrofit2:converter-gson:2.6.1")
    implementation("com.squareup.retrofit2:converter-scalars:2.6.1")
    implementation("com.jakewharton.retrofit:retrofit2-rxjava2-adapter:1.0.0")
    implementation("com.amitshekhar.android:rx2-android-networking:1.0.1")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    /*okhttp3*/
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.9.1"))
    implementation("com.squareup.okhttp3:okhttp")              // No version!
    implementation("com.squareup.okhttp3:okhttp-urlconnection") // No version!
    implementation("com.squareup.okhttp3:logging-interceptor")
    // reactive
    implementation("io.reactivex.rxjava2:rxjava:2.1.8")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")


    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")

    /*ssp*/
    implementation("com.intuit.ssp:ssp-android:1.0.6")

    /*firebase*/
    implementation(
        platform("com.google.firebase:firebase-bom:29.2.0")
    )
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-analytics")

    /*glide*/
    implementation("com.github.bumptech.glide:glide:4.11.0")

    /*file pick*/
    implementation("com.github.HBiSoft:PickiT:0.1.14")

    /*navigation*/
    implementation("androidx.navigation:navigation-fragment:2.3.5")
    implementation("androidx.navigation:navigation-ui:2.3.5")

    implementation("com.google.android.libraries.places:places:2.5.0")

    implementation("com.tbuonomo.andrui:viewpagerdotsindicator:4.1.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.10.3")

    /*cashfree*/
    implementation("com.cashfree.pg:android-sdk:1.7.11")

    /*paytm*/
    implementation("com.paytm.appinvokesdk:appinvokesdk:1.6.0")

    implementation("com.ncorti:slidetoact:0.9.0")

    /*ripple*/
    implementation("com.github.realpacific:click-shrink-effect:2.0")

    /*flex*/
    implementation("com.google.android:flexbox:2.0.1")

    implementation("com.github.basusingh:BeautifulProgressDialog:1.001")
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    implementation("com.google.android.exoplayer:exoplayer-core:2.18.7")
    implementation("com.google.android.exoplayer:exoplayer-dash:2.18.7")
    implementation("com.google.android.exoplayer:exoplayer-ui:2.18.7")
    implementation("com.google.android.exoplayer:exoplayer:2.18.7")
    implementation("com.google.android.exoplayer:exoplayer-hls:2.18.7")

    implementation("androidx.camera:camera-camera2:1.2.3")
    implementation("androidx.camera:camera-lifecycle:1.2.3")
    implementation("androidx.camera:camera-view:1.3.0-beta02")
    implementation("com.github.krtkush:LinearTimer:v2.1.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}