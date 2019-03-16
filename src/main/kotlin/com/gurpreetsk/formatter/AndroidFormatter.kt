package com.gurpreetsk.formatter

import com.gurpreetsk.internal.Comment
import com.gurpreetsk.internal.Line
import com.gurpreetsk.internal.Resource
import com.gurpreetsk.internal.clean

abstract class AndroidFormatter : Formatter {
    override fun format(rawContents: Set<Line>): String {
        StringBuilder()
            .apply {
                append("<resources>\n")
                rawContents.forEach { line -> append(getAndroidStringResource(line)) }
                append("</resources>\n")
            }
            .also { return it.toString() }
    }

    abstract fun getAndroidStringResource(
        line: Line
    ): String

    fun String.convertToAndroidTemplate(): String {
        val regex = "<(.*?)>".toRegex()
        return this.replace(regex, "%s")
    }
}
