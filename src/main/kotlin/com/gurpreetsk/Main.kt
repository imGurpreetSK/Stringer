package com.gurpreetsk

//ENTRY com.gurpreetsk.MainKt

import com.gurpreetsk.internal.FilePath
import com.gurpreetsk.internal.Utils.getAndroidStrings
import com.gurpreetsk.internal.Utils.getiOSStrings

fun main(args: Array<String>) {
    val homeDirectory = System.getenv("HOME")
    val fileByLines   = CsvReader(FilePath("$homeDirectory/Downloads/mobile-strings.csv")).parseCsv()

    val androidStringBuilder = getAndroidStrings(fileByLines)
    val iOSStringBuilder     = getiOSStrings(fileByLines)

    val destinationDirectory = "$homeDirectory/Desktop/StringerThings"
    FileWriter()
        .apply {
            val parentDirectory = createParentDirectory(destinationDirectory)
            createAndroidStringsFile(parentDirectory, androidStringBuilder.toString())
            createiOSStringsFile(parentDirectory, iOSStringBuilder.toString())
        }
        .also { println("Success! Files generated in directory \"$destinationDirectory\".") }
}
