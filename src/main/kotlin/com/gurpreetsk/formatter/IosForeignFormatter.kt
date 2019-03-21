package com.gurpreetsk.formatter

import com.gurpreetsk.internal.Comment
import com.gurpreetsk.internal.Line
import com.gurpreetsk.internal.Resource
import com.gurpreetsk.internal.clean

class IosForeignFormatter : IosFormatter() {

    override fun getiOSStringResource(
        line: Line
    ): String {
        return when (line) {
            is Comment -> "\n// ${line.text.substring(1).trim()}\n"
            is Resource -> "\"${line.key.text.clean()}\" = \"${line.foreignValue.text.trim().convertToiOSTemplate()}\";\n"
        }
    }

}