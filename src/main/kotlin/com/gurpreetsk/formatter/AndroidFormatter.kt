package com.gurpreetsk.formatter

import com.gurpreetsk.internal.Comment
import com.gurpreetsk.internal.Line
import com.gurpreetsk.internal.Resource
import com.gurpreetsk.internal.clean

class AndroidFormatter : Formatter {
    override fun format(rawContents: Set<Line>): String {
        StringBuilder()
            .apply {
                append("<resources>\n")
                rawContents.forEach { line -> append(getAndroidStringResource(line)) }
                append("</resources>\n")
            }
            .also { return it.toString() }
    }

    private fun getAndroidStringResource(
        line: Line
    ): String {
        val indentLevelInSpaces = "  "
        return when (line) {
            is Comment -> "\n$indentLevelInSpaces<!-- ${line.text.substring(1).trim()} -->\n"
            is Resource -> "$indentLevelInSpaces<string name=\"${line.key.text.clean()}\">\"${line.value.text.trim().convertToAndroidTemplate()}\"</string>\n"
        }
    }

    private fun String.convertToAndroidTemplate(): String {
        val regex = "<(.*?)>".toRegex()
        return this.replace(regex, "%s")
    }
}
