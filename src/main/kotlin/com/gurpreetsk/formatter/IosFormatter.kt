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
package com.gurpreetsk.formatter

import com.gurpreetsk.internal.Comment
import com.gurpreetsk.internal.Line
import com.gurpreetsk.internal.Resource
import com.gurpreetsk.internal.clean

class IosFormatter : Formatter {
    override fun format(rawContents: Set<Line>): String {
        StringBuilder()
            .apply { rawContents.forEach { line -> append(getiOSStringResource(line)) } }
            .also  { return it.toString() }
    }

    private fun getiOSStringResource(
        line: Line
    ): String {
        return when (line) {
            is Comment -> "\n// ${line.text.substring(1).trim()}\n"
            is Resource -> "\"${line.key.text.clean()}\" = \"${line.value.text.trim().convertToiOSTemplate()}\";\n"
        }
    }


    private fun String.convertToiOSTemplate(): String {
        val regex = "<(.*?)>".toRegex()
        return this.replace(regex, "%@")
    }
}
