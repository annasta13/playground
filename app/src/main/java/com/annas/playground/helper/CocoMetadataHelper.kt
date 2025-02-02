package com.annas.playground.helper


import android.content.Context
import com.annas.playground.utils.ContextExtension.toast
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

object CocoMetadataHelper {

    fun loadLabels(context: Context, fileName: String): List<String> {
        var labels = listOf<String>()
        var inputStream: InputStream?
        runCatching {
            inputStream = context.assets.open(fileName)
            val reader = BufferedReader(InputStreamReader(inputStream))
            labels = reader.readLines()
            inputStream?.close()
        }.onFailure {
            context.toast("Error loading metadata")
        }
        return labels
    }
}
