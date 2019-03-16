/*
 * Copyright (C) 2019 Gurpreet Singh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gurpreetsk

//ENTRY com.gurpreetsk.MainKt

import com.gurpreetsk.formatter.*
import com.gurpreetsk.internal.FilePath
import com.gurpreetsk.writer.AndroidFileWriter
import com.gurpreetsk.writer.DirectoryManager
import com.gurpreetsk.writer.IosFileWriter

fun main(args: Array<String>) {
    val homeDirectory = System.getenv("HOME")
    val csvPath       = FilePath("$homeDirectory/Downloads/mobile-strings.csv")
    val fileByLines   = CsvReader(csvPath).parseCsv()

    val androidForeignFileContents = AndroidForeignFormatter().format(fileByLines)
    val androidEnglishFileContents = AndroidEngFormatter().format(fileByLines)
    val iOSFileEnglishContents     = IosEnglishFormatter().format(fileByLines)
    val iOSFileForeignContents     = IosForeignFormatter().format(fileByLines)

    val destinationDirectory = "$homeDirectory/Desktop/StringerThings"
    val foreignDestinationDirctory= "$homeDirectory/Desktop/StringerThings/foreign"
    DirectoryManager
        .createParentDirectory(destinationDirectory)
        .run {
            AndroidFileWriter().write(this, androidEnglishFileContents)

            IosFileWriter().write(this, iOSFileEnglishContents)
        }
        .also {
            println("Success! Files generated in directory \"$destinationDirectory\".")
        }
    DirectoryManager
        .createParentDirectory(foreignDestinationDirctory)
        .run {
            AndroidFileWriter().write(this, androidForeignFileContents)
            IosFileWriter().write(this, iOSFileForeignContents)
        }
        .also {
            println("Success! Files generated in directory \"$foreignDestinationDirctory\".")
        }


}
