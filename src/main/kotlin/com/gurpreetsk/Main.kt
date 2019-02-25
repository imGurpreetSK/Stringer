package com.gurpreetsk

//ENTRY com.gurpreetsk.MainKt

import com.gurpreetsk.internal.FilePath
import com.gurpreetsk.internal.Utils.getAndroidStrings
import com.gurpreetsk.internal.Utils.getiOSStrings
import com.gurpreetsk.writer.AndroidFileWriter
import com.gurpreetsk.writer.DirectoryManager
import com.gurpreetsk.writer.IosFileWriter

private val androidFileWriter by lazy { AndroidFileWriter() }
private val iosFileWriter by lazy { IosFileWriter() }

fun main(args: Array<String>) {
    val homeDirectory = System.getenv("HOME")
    val fileByLines   = CsvReader(FilePath("$homeDirectory/Downloads/mobile-strings.csv")).parseCsv()

    val androidStringBuilder = getAndroidStrings(fileByLines)
    val iOSStringBuilder     = getiOSStrings(fileByLines)

    val destinationDirectory = "$homeDirectory/Desktop/StringerThings"
    DirectoryManager
        .createParentDirectory(destinationDirectory)
        .run {
            androidFileWriter.write(this, androidStringBuilder.toString())
            iosFileWriter.write(this, iOSStringBuilder.toString())
        }
        .also {
            println("Success! Files generated in directory \"$destinationDirectory\".")
        }
}
