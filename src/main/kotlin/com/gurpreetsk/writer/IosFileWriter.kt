package com.gurpreetsk.writer

import java.io.File

class IosFileWriter : FileWriter {
    override fun write(parentDirectory: File, contents: String) {
        val iOSStrings = File(parentDirectory, "localizable.strings")
        iOSStrings.writeText(contents)
        iOSStrings.appendText("\n") // End file with a new line.
    }
}
