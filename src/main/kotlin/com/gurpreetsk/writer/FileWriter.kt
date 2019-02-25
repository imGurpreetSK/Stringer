package com.gurpreetsk.writer

import java.io.File

interface FileWriter {
    fun write(parentDirectory: File, contents: String)
}
