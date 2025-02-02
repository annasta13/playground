
plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}
dependencies {
    implementation("de.undercouch:gradle-download-task:4.1.2")
    implementation(kotlin("script-runtime"))
}
