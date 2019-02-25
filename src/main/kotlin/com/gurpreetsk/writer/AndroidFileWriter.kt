package com.gurpreetsk.writer

import java.io.File

class AndroidFileWriter : FileWriter {
    override fun write(parentDirectory: File, contents: String) {
        val androidStrings = File(parentDirectory, "strings.xml")
        androidStrings.writeText(contents)
        androidStrings.appendText("\n") // End file with a new line.
    }
}
