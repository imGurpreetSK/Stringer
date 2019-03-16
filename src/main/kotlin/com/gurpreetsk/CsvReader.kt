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

import com.gurpreetsk.internal.*
import java.io.File

class CsvReader(private val path: FilePath) {
    fun parseCsv(): Set<Line> {
        val file = getReadableFile()
        return if (file.canRead()) {
            readFileContents(file)
        } else {
            throw FileSystemException(file, reason = "File not readable")
        }
    }

    private fun getReadableFile(): File {
        return File(path.value)
            .apply { setReadable(true) }
    }

    private fun readFileContents(
        file: File
    ): Set<Line> {
        val set = linkedSetOf<Line>()
        file.useLines { sequence ->
            sequence.iterator().forEach { line ->
                if (!line.isBlank()) {
                    if (line.startsWith("#")) {
                        set.add(Comment(line))
                    } else {
                        val splitLine = line.split(",")
                        val key = getResourceKey(splitLine)
                        val enValue =
                            ResourceEnglishValue(splitLine[1].trim().removeBoundaryQuotes().escapeInternalQuotes())
                        val foreignValue = ResourceForeignValue(splitLine[2].trim().removeBoundaryQuotes().escapeInternalQuotes())
                        set.add(Resource(key, enValue, foreignValue))
                    }
                }
            }
        }

        return set.toSet()
    }


    private fun getResourceKey(splitLine: List<String>): ResourceKey {
        val resourceText = splitLine[0].clean()
        // Generate key of pattern "type_feature_name_text", eg: error_resource_details_connection
        val key = resourceText
        return ResourceKey(key)
    }

    private fun String.removeBoundaryQuotes(): String {
        // Remove quotes at start
        if (this[0] == '\"' || this[0] == '\'') {
            val first = this.substring(1)
            // Remove quotes at end
            if (first.isNotBlank() && (first.last() == '\"' || first.last() == '\'')) {
                return first.substring(0, first.lastIndex)
            }
            return first
        }
        return this
    }

    private fun String.escapeInternalQuotes(): String = this.replace("\"", "\\\"")
}
