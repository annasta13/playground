import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.detekt)
    id("kotlin-kapt")
    id("download-tasks")
    jacoco
}


android {
    namespace = "com.annas.playground"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.annas.playground"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "ACCESS_TOKEN", readProperties("ACCESS_TOKEN"))
    }

    ksp {
        arg("room.schemaLocation", "$projectDir/app/src/schemas")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            enableAndroidTestCoverage = true
            enableUnitTestCoverage = true
            applyJacoco()
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
        dataBinding = true
        mlModelBinding = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }

    }
    @Suppress("UnstableApiUsage")
    testOptions {//support mockito
        unitTests.isReturnDefaultValues = true
    }

    aaptOptions {
        noCompress("tflite", "lite")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.paging.compose.android)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.security.crypto.ktx)
    implementation(libs.vision.common)
    implementation(libs.play.services.mlkit.text.recognition.common)
    implementation(libs.play.services.mlkit.text.recognition)
    implementation(libs.androidx.coordinatorlayout)
    implementation(libs.sceneform.ux)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    testImplementation(libs.mockito.kotlin)
    implementation(libs.hilt.work)
    ksp(libs.hilt.work.compiler)
    implementation(libs.hilt)
    testImplementation(libs.hilt.testing)
    ksp(libs.hilt.compiler)

    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    androidTestImplementation(libs.room.testing)

    implementation(libs.timber)
    implementation(libs.compose.coil)
    implementation(libs.compose.navigation)
    implementation(libs.compose.hilt.navigation)
    implementation(libs.converter.moshi)
    implementation(libs.accompanist.swiperefresh)
    implementation(libs.landscapist.glide)
    implementation(libs.mlkit.document.scanner)

    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.core.splashscreen)

    testImplementation(libs.test.truth)
    testImplementation(libs.test.coroutine)
    testImplementation(libs.test.pagination)
    implementation(libs.androidx.camera.core)
    implementation(libs.camera.camera2)
    implementation(libs.camera.lifecycle)
    implementation(libs.camera.view)
    implementation (libs.guava)

    //Object Detector
    implementation(libs.tensorflow.lite.gpu){
        exclude(group = "org.tensorflow.lite", module = "tensorflow-lite-api")
    }
    implementation(libs.tensorflow.lite.task.vision)
    implementation(libs.tensorflow.lite.api)
    implementation(libs.tensorflow.lite.support)

    //yolov9
    implementation(libs.tensorflow.lite.gpu.delegate.plugin)
    implementation(libs.tensorflow.lite.gpu.api)
    implementation(libs.arcore)

    //ARCore
    implementation (libs.javagl.obj)
    implementation (libs.material)
//    implementation ("io.github.sceneview:arsceneview:0.9.7")
    implementation ("androidx.media3:media3-exoplayer:1.1.1")
    implementation ("androidx.media3:media3-exoplayer-dash:1.1.1")
    implementation ("androidx.media3:media3-ui:1.1.1")
//    implementation("io.github.sceneview:sceneview:2.2.1")
}

fun readProperties(key: String): String {
    val properties = Properties().apply {
        file("${rootDir}/local.properties").inputStream().use { fis ->
            load(fis)
        }
    }
    return properties[key] as String
}

tasks.withType(Test::class) {
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

configurations.all {
    resolutionStrategy {
        resolutionStrategy.force(libs.androidx.core.ktx)
        force("org.tensorflow:tensorflow-lite-support:0.4.4")
    }
}
