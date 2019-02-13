package com.gurpreetsk

import com.gurpreetsk.internal.Comment
import com.gurpreetsk.internal.FilePath
import com.gurpreetsk.internal.Line
import com.gurpreetsk.internal.Resource
import com.gurpreetsk.internal.ResourceKey
import com.gurpreetsk.internal.ResourceValue
import java.io.File

class CsvReader(private val path: FilePath) {
    fun parseCsv(): Set<Line> {
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
    ): Set<Line> {
        val set = linkedSetOf<Line>()
        file.useLines { sequence ->
            sequence.iterator().forEach { line ->
                if (line.startsWith("#")) {
                    set.add(Comment(line))
                } else {
                    val keyValuePairs = line.split(",")
                    set.add(Resource(ResourceKey(keyValuePairs[0]), ResourceValue(keyValuePairs[0].convertToAndroidTemplate())))
                }
            }
        }

        return set.toSet()
    }

    private fun String.convertToAndroidTemplate(): String {
        val regex = "<(.*?)>".toRegex()
        return this.replace(regex, "%s")
    }
}
