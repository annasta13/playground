// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.kotlin.ksp) apply false
    alias(libs.plugins.detekt)
    jacoco
}

tasks.register("generateEventResource") {
    group = "resource generator"
    createEventResource()
}

allprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    detekt {
        config.setFrom(file("$rootDir/detekt/compose-detekt-rule.yml"))
        buildUponDefaultConfig = true
    }
}

