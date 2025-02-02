import de.undercouch.gradle.tasks.download.Download
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.register

class DownloadTasksPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val assetDir = "${project.projectDir}/src/main/assets"
        val testAssetDir = "${project.projectDir}/src/androidTest/assets"
        val urlPath =
            "https://storage.googleapis.com/download.tensorflow.org/models/tflite/task_library/object_detection/android"
        project.tasks.register<Download>("downloadSsdMobileNetMetadataV1Model") {
            src("$urlPath/lite-model_ssd_mobilenet_v1_1_metadata_2.tflite")
            dest(project.file("$assetDir/mobilenetv1.tflite"))
            overwrite(false)
        }

        project.tasks.register<Download>("downloadEfficientdetV0Model") {
            src("$urlPath/lite-model_efficientdet_lite0_detection_metadata_1.tflite")
            dest(project.file("$assetDir/efficientdet-lite0.tflite"))
            overwrite(false)
        }

        project.tasks.register<Download>("downloadEfficientdetV1Model") {
            src("$urlPath/lite-model_efficientdet_lite1_detection_metadata_1.tflite")
            dest(project.file("$assetDir/efficientdet-lite1.tflite"))
            overwrite(false)
        }

        project.tasks.register<Download>("downloadEfficientdetV2Model") {
            src("$urlPath/lite-model_efficientdet_lite2_detection_metadata_1.tflite")
            dest(project.file("$assetDir/efficientdet-lite2.tflite"))
            overwrite(false)
        }

        project.tasks.register<Copy>("copyTestModel") {
            dependsOn("downloadSsdMobileNetMetadataV1Model")
            from(project.file("$assetDir/mobilenetv1.tflite"))
            into(project.file(testAssetDir))
        }

        project.tasks.named("preBuild") {
            dependsOn(
                "downloadSsdMobileNetMetadataV1Model",
                "downloadEfficientdetV0Model",
                "downloadEfficientdetV1Model",
                "downloadEfficientdetV2Model",
                "copyTestModel"
            )
        }
    }
}
