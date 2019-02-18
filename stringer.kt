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
                        val key   = getResourceKey(splitLine)
                        val value = ResourceValue(splitLine[1].trim().removeBoundaryQuotes().escapeInternalQuotes())
                        set.add(Resource(key, value))
                    }
                }
            }
        }

        return set.toSet()
    }

    private fun getResourceKey(splitLine: List<String>): ResourceKey {
        val resourceText = splitLine[0].clean()
        val type         = try { splitLine[2].clean() } catch (e: IndexOutOfBoundsException) { null } // TODO(gs) 13/02/19 - Find a better way of doing this.
        val feature      = try { splitLine[3].clean() } catch (e: IndexOutOfBoundsException) { null }

        // Generate key of pattern "type_feature_name_text", eg: error_resource_details_connection
        val key     = "${if (!type.isNullOrBlank()) type + "_" else ""}${if (!feature.isNullOrBlank()) feature + "_" else ""}$resourceText"
        return ResourceKey(key)
    }

    private fun String.removeBoundaryQuotes(): String {
        // Remove quotes at start
        if (this[0] == '\"' || this[0] == '\'') {
            val first = this.substring(1)
            // Remove quotes at end
            if (first.isNotBlank() && (first.last() == '\"' || first.last() == '\'')) {
                return first.substring(0, first.lastIndex)
            }
            return first
        }
        return this
    }

    private fun String.escapeInternalQuotes(): String = this.replace("\"", "\\\"")
}

/* ------- File helpers ------- */
class FileWriter {
    fun createParentDirectory(path: String): File {
        return File(path).apply { mkdir() }
    }

    fun createAndroidStringsFile(parentDirectory: File, contents: String) {
        val androidStrings = File(parentDirectory, "strings.xml")
        androidStrings.writeText(contents)
    }

    fun createiOSStringsFile(parentDirectory: File, contents: String) {
        val iOSStrings = File(parentDirectory, "localizable.strings")
        iOSStrings.writeText(contents)
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
        val indentLevelInSpaces = "  "
        return when (line) {
            is Comment  -> "\n$indentLevelInSpaces<!-- ${line.text.substring(1).trim()} -->\n"
            is Resource -> "$indentLevelInSpaces<string name=\"${line.key.text.clean()}\">\"${line.value.text.trim().convertToAndroidTemplate()}\"</string>\n"
        }
    }

    private fun getiOSStringResource(
        line: Line
    ): String {
        return when (line) {
            is Comment  -> "\n// ${line.text.substring(1).trim()}\n"
            is Resource -> "\"${line.key.text.clean()}\" = \"${line.value.text.trim().convertToiOSTemplate()}\";\n"
        }
    }

    private fun String.convertToAndroidTemplate(): String {
        val regex = "<(.*?)>".toRegex()
        return this.replace(regex, "%s")
    }

    private fun String.convertToiOSTemplate(): String {
        val regex = "<(.*?)>".toRegex()
        return this.replace(regex, "%@")
    }
}

private fun String.clean(): String =
    this.trim().toLowerCase().replace(" ", "_")

/* ------- Inline and sealed classes ------- */
inline class FilePath(val value: String)
inline class ResourceKey(val text: String)
inline class ResourceValue(val text: String)

sealed class Line
class Comment(val text: String) : Line()
class Resource(val key: ResourceKey, val value: ResourceValue) : Line()

/* ------- Misc ------- */
