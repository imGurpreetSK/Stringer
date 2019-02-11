package com.gurpreetsk

import java.io.File

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
