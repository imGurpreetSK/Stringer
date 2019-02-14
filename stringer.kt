package com.gurpreetsk

//DEPS com.github.holgerbrandl:kscript:1.2

import java.io.File

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("No CSV file path provided.")
        return
    }

    val homeDirectory = System.getenv("HOME")
    val fileByLines   = CsvReader(FilePath(args[0])).parseCsv()

    val androidStringBuilder = Utils.getAndroidStrings(fileByLines)
    val iOSStringBuilder     = Utils.getiOSStrings(fileByLines)

    val destinationDirectory = "StringerThings"
    FileWriter()
        .apply {
            val parentDirectory = createParentDirectory(destinationDirectory)
            createAndroidStringsFile(parentDirectory, androidStringBuilder.toString())
            createiOSStringsFile(parentDirectory, iOSStringBuilder.toString())
        }
        .also { println("Success! Files generated in directory \"$destinationDirectory\".") }
}


/* ------- CSV parsing ------- */
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

/* ------- File helpers ------- */
class FileWriter {
    fun createParentDirectory(path: String): File {
        return File(path).apply { mkdir() }
    }

    fun createAndroidStringsFile(parentDirectory: File, contents: String) {
        val androidStrings = File(parentDirectory, "strings.xml")
        androidStrings.writeText(contents)
        androidStrings.appendText("\n") // End file with a new line.
    }

    fun createiOSStringsFile(parentDirectory: File, contents: String) {
        val iOSStrings = File(parentDirectory, "localizable.strings")
        iOSStrings.writeText(contents)
        iOSStrings.appendText("\n") // End file with a new line.
    }
}

/* ------- Utility functions ------- */
object Utils {
    fun getiOSStrings(
        lines: Set<Line>
    ): StringBuilder {
        return StringBuilder()
            .apply { lines.forEach { line -> append(getiOSStringResource(line)) } }
    }

    fun getAndroidStrings(
        lines: Set<Line>
    ): StringBuilder = StringBuilder().apply {
        append("<resources>\n")
        lines.forEach { line -> append(getAndroidStringResource(line)) }
        return append("</resources>\n")
    }

    private fun getAndroidStringResource(
        line: Line
    ): String {
        val indentSpaces = "  "
        return when (line) {
            is Comment  -> "\n$indentSpaces<!-- ${line.text.substring(1).trim()} -->\n"
            is Resource -> "$indentSpaces<string name=\"${line.key.text.cleaned()}\">\"${line.value.text.trim()}\"</string>\n"
        }
    }

    private fun getiOSStringResource(
        line: Line
    ): String {
        return when (line) {
            is Comment  -> "\n// ${line.text.substring(1).trim()}\n"
            is Resource -> "\"${line.key.text.cleaned()}\" = \"${line.value.text.trim().replace("%s", "%@")}\";\n"
        }
    }

}

private fun String.cleaned(): String =
    this.trim().toLowerCase().replace(" ", "_")

/* ------- Inline and sealed classes ------- */
inline class FilePath(val value: String)
inline class ResourceKey(val text: String)
inline class ResourceValue(val text: String)

sealed class Line
class Comment(val text: String) : Line()
class Resource(val key: ResourceKey, val value: ResourceValue) : Line()

/* ------- Misc ------- */
