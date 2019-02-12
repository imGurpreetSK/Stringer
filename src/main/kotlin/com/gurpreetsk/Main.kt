package com.gurpreetsk

import com.gurpreetsk.internal.FilePath
import com.gurpreetsk.internal.Utils.getAndroidStrings
import com.gurpreetsk.internal.Utils.getiOSStrings

fun main(args: Array<String>) {
    val homeDirectory = System.getenv("HOME")
    val keyValueMap   = CsvReader(FilePath("$homeDirectory/Downloads/errors.csv")).parseCsv()

    val androidStringBuilder = getAndroidStrings(keyValueMap)
    val iOSStringBuilder     = getiOSStrings(keyValueMap)

    val destinationDirectory = "$homeDirectory/Desktop/StringerThings"
    FileWriter()
        .apply {
            val parentDirectory = createParentDirectory(destinationDirectory)
            createAndroidStringsFile(parentDirectory, androidStringBuilder.toString())
            createiOSStringsFile(parentDirectory, iOSStringBuilder.toString())
        }
        .also { println("Success! Files generated in directory \"$destinationDirectory\".") }
}
