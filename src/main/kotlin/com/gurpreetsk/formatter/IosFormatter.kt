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
