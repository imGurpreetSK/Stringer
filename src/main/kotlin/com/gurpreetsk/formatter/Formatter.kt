package com.gurpreetsk.formatter

import com.gurpreetsk.internal.Line

interface Formatter {
    fun format(rawContents: Set<Line>): String
}
