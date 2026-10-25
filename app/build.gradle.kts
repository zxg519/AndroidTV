plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.streamhub"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.streamhub"
        minSdk = 19
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
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

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // 核心
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.preference:preference-ktx:1.2.1")

    // Android TV 官方框架（遥控器焦点 / 分栏 / 卡片）
    implementation("androidx.leanback:leanback:1.0.0")

    // 播放内核：Media3 / ExoPlayer（支持 HLS、DASH、渐进式直播流）
    implementation("androidx.media3:media3-exoplayer:1.2.0")
    implementation("androidx.media3:media3-exoplayer-hls:1.2.0")
    implementation("androidx.media3:media3-ui:1.2.0")
    implementation("androidx.media3:media3-common:1.2.0")
    implementation("androidx.media3:media3-datasource:1.2.0")

    // 网络拉取 M3U 文本（使用系统 HttpURLConnection，兼容 API 19，故不再依赖 OkHttp）

    // 频道 logo 图片加载
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // 协程
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
}
