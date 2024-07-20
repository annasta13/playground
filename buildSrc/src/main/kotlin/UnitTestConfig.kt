import org.gradle.api.Project
import org.gradle.kotlin.dsl.register
import org.gradle.testing.jacoco.tasks.JacocoReport

val unitTestExclusions = listOf(
    "**/R.class",
    "**/R\$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/*MainActivity*.*",
    "**/*Test*.*",
    "**/hilt**/",
    "**/*hilt*.*",
    "**/App.class",
    "jdk.internal.*",
    "**/build/**",
    "**/model/**",
    "**/*Builder.class",
    "**/*Interceptor.class",
    "**/*Dto.class",
    "**/*Dao.class",
    "**/*Dao_Impl*.class",
    "**/ui/components/",
    "**/di/",
    "**/db/",
    "**/helper/",
    "**/constants/",
    "**/ui/graph/",
    "**/ui/theme/",
    "**/*ScreenKt.class",
    "**/*ScreenKt\$*.class",
    "**/*SheetKt.class",
    "**/*SheetKt\$*.class",
    "**/*KeyModule*.class",
    "**/*.xml"
)


fun Project.applyJacoco() {
    // Register a JacocoReport task for code coverage analysis
    tasks.register<JacocoReport>("JacocoDebugCodeCoverage") {
        // Depend on unit tests and Android tests tasks
        dependsOn(listOf("testDebugUnitTest"))
        // Set task grouping and description
        group = "Reporting"
        description = "Execute unit tests, generate and combine Jacoco coverage report"
        // Configure reports to generate both XML and HTML formats
        reports {
            xml.required.set(true)
            html.required.set(true)
        }
        // Set source directories to the main source directory
        sourceDirectories.setFrom(
            fileTree(layout.buildDirectory.dir("src/main")) {
                exclude(unitTestExclusions)
            }
        )
        // Set class directories to compiled Java and Kotlin classes, excluding specified exclusions
        classDirectories.setFrom(files(
            fileTree(layout.buildDirectory.dir("intermediates/javac/")) {
                exclude(unitTestExclusions)
            },
            fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/")) {
                exclude(unitTestExclusions)
            }
        ))
        // Collect execution data from .exec and .ec files generated during test execution
        executionData.setFrom(files(
            fileTree(layout.buildDirectory) { include(listOf("**/*.exec", "**/*.ec")) }
        ))
    }
}
