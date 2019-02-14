package com.gurpreetsk

import com.gurpreetsk.internal.*
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
                if (!line.isBlank()) {
                    if (line.startsWith("#")) {
                        set.add(Comment(line))
                    } else {
                        val splitLine = line.split(",")
                        val key = getResourceKey(splitLine)
                        val value = ResourceValue(splitLine[1].convertToAndroidTemplate())
                        set.add(Resource(key, value))
                    }
                }
            }
        }

        return set.toSet()
    }

    private fun getResourceKey(splitLine: List<String>): ResourceKey {
        val resourceText = splitLine[0].cleaned()
        val type         = try { splitLine[2].cleaned() } catch (e: IndexOutOfBoundsException) { null } // TODO(gs) 13/02/19 - Find a better way of doing this.
        val feature      = try { splitLine[3].cleaned() } catch (e: IndexOutOfBoundsException) { null }

        // Generate key of pattern "type_feature_name_text", eg: error_resource_details_connection
        val key     = "${if (!type.isNullOrBlank()) type + "_" else ""}${if (!feature.isNullOrBlank()) feature + "_" else ""}$resourceText"
        return ResourceKey(key)
    }

    private fun String.convertToAndroidTemplate(): String {
        val regex = "<(.*?)>".toRegex()
        return this.replace(regex, "%s")
    }
}
