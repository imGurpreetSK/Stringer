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

class AndroidForeignFormatter : AndroidFormatter() {
    override fun getAndroidStringResource(line: Line): String {

        val indentLevelInSpaces = "  "
        return when (line) {
            is Comment -> "\n$indentLevelInSpaces<!-- ${line.text.substring(1).trim()} -->\n"
            is Resource -> "$indentLevelInSpaces<string name=\"${line.key.text.clean()}\">\"${line.foreignValue.text.trim().convertToAndroidTemplate()}\"</string>\n"
        }

    }
}