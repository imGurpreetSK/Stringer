package com.gurpreetsk

import com.gurpreetsk.internal.FilePath
import com.gurpreetsk.internal.ResourceKey
import com.gurpreetsk.internal.ResourceValue
import java.io.File

class CsvReader(private val path: FilePath) {
    fun parseCsv(): Map<ResourceKey, ResourceValue> {
        val file = getReadableFile()
        return if (file.canRead()) {
            readFileContents(file)
        } else {
            throw FileSystemException(file, reason = "File not readable")
        }
    }

    private fun getReadableFile(): File {
        return File(path.value)
            .apply { setReadable(true) }
    }

    private fun readFileContents(
        file: File
    ): Map<ResourceKey, ResourceValue> {
        val map = mutableMapOf<ResourceKey, ResourceValue>()
        file.useLines { sequence ->
            sequence.iterator().forEach { line ->
                val keyValuePairs = line.split(",")
                map[ResourceKey(keyValuePairs[0])] = ResourceValue(keyValuePairs[1].convertToAndroidTemplate())
            }
        }

        return map.toMap()
    }
}

private fun String.convertToAndroidTemplate(): String {
    val regex = "<(.*?)>".toRegex()
    return this.replace(regex, "%s")
}
