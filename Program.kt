package com.gurpreetsk

//DEPS com.github.holgerbrandl:kscript:1.2

import java.io.File

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("No CSV file path provided.")
        return
    }

    val homeDirectory = System.getenv("HOME")
    val keyValueMap   = parseCsv(FilePath(args[0]))

    val androidStringBuilder = getAndroidStrings(keyValueMap)
    val iOSStringBuilder     = getiOSStrings(keyValueMap)

    val destinationDirectory = "StringerThings"
    val parentDirectory = createParentDirectory(destinationDirectory)
    createAndroidStringsFile(parentDirectory, androidStringBuilder.toString())
    createiOSStringsFile(parentDirectory, iOSStringBuilder.toString())
    println("Bravo! Files generated successfully at \"$destinationDirectory\".")
}


/* -------- CSV Parsing -------- */
fun parseCsv(path: FilePath): Map<ResourceKey, ResourceValue> {
    val file = getReadableFile(path)
    return if (file.canRead()) {
        readFileContents(file)
    } else {
        throw FileSystemException(file, reason = "File not readable")
    }
}

private fun getReadableFile(path: FilePath): File {
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
            map[ResourceKey(keyValuePairs[0])] = ResourceValue(keyValuePairs[1])
        }
    }

    return map.toMap()
}


/* -------- File Helpers -------- */
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

fun getiOSStrings(
    keyValueMap: Map<ResourceKey, ResourceValue>
): StringBuilder {
    return StringBuilder()
        .apply {
            keyValueMap.forEach { key, value -> append(getiOSStringResource(key, value)) }
        }
}

fun getAndroidStrings(
    keyValueMap: Map<ResourceKey, ResourceValue>
): StringBuilder = StringBuilder().apply {
    append("<resources>\n")
    keyValueMap.forEach { key, value -> append(getAndroidStringResource(key, value)) }
    return append("</resources>\n")
}

private fun getAndroidStringResource(
    key: ResourceKey,
    value: ResourceValue
): String = "  <string name=\"${key.key.trim()}\">\"${value.value.trim().replace(" ", "_")}\"</string>\n"

private fun getiOSStringResource(
    key: ResourceKey,
    value: ResourceValue
): String = "${key.key}=${value.value}\n"


/* -------- Inline classes for type safety -------- */
inline class FilePath(val value: String)
inline class ResourceKey(val key: String)
inline class ResourceValue(val value: String)
