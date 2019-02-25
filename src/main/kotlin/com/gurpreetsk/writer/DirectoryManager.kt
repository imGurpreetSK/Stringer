package com.gurpreetsk.writer

import java.io.File

object DirectoryManager {
    fun createParentDirectory(path: String): File {
        return File(path).apply { mkdir() }
    }
}