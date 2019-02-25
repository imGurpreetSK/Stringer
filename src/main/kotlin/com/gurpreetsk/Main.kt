package com.gurpreetsk

//ENTRY com.gurpreetsk.MainKt

import com.gurpreetsk.formatter.AndroidFormatter
import com.gurpreetsk.formatter.IosFormatter
import com.gurpreetsk.internal.FilePath
import com.gurpreetsk.writer.AndroidFileWriter
import com.gurpreetsk.writer.DirectoryManager
import com.gurpreetsk.writer.IosFileWriter

fun main(args: Array<String>) {
    val homeDirectory = System.getenv("HOME")
    val csvPath       = FilePath("$homeDirectory/Downloads/mobile-strings.csv")
    val fileByLines   = CsvReader(csvPath).parseCsv()

    val androidFileContents = AndroidFormatter().format(fileByLines)
    val iOSFileContents     = IosFormatter().format(fileByLines)

    val destinationDirectory = "$homeDirectory/Desktop/StringerThings"
    DirectoryManager
        .createParentDirectory(destinationDirectory)
        .run {
            AndroidFileWriter().write(this, androidFileContents)
            IosFileWriter().write(this, iOSFileContents)
        }
        .also {
            println("Success! Files generated in directory \"$destinationDirectory\".")
        }
}
