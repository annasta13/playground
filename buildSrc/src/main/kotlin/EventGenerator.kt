import org.gradle.api.Project

fun Project.createEventResource() {
    val printerLog = "EventGenerator"
    println("$printerLog: Creating event resources")
    val targetDir = "$rootDir/app/src/main/res/values/event_resources.xml"
    val targetFile = file(targetDir)
    val sourcePath = "$rootDir/buildSrc/src/main/resources/event_resource.csv"
    val source = file(sourcePath).readText()

    val builder = StringBuilder("<resources>")
    val lines = source.lines().filter { it.isNotBlank() }
    builder.appendLine()
    lines.forEachIndexed { index, s ->
        if (index > 0) {
            val resourceId = s.substringBefore(",")
            val resourceValue = s.substringAfter(",").substringBefore(",")
            val routeKey = "    <string name=\"${resourceId}\">"
            val routeValue = "$resourceValue</string>"
            builder.append("$routeKey$routeValue")
            builder.appendLine()
        }
    }
    builder.append("</resources>")
    targetFile.createNewFile()
    targetFile.writeText(builder.toString())
    println("$printerLog: Event resource file created successfully")
}
