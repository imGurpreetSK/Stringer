package com.gurpreetsk.internal

object Utils {
    fun getiOSStrings(
        lines: Set<Line>
    ): StringBuilder {
        return StringBuilder()
            .apply { lines.forEach { line -> append(getiOSStringResource(line)) } }
    }

    fun getAndroidStrings(
        lines: Set<Line>
    ): StringBuilder = StringBuilder().apply {
        append("<resources>\n")
        lines.forEach { line -> append(getAndroidStringResource(line)) }
        return append("</resources>\n")
    }

    private fun getAndroidStringResource(
        line: Line
    ): String {
        val indentSpaces = "  "
        return when (line) {
            is Comment  -> "\n$indentSpaces<!-- ${line.text.substring(1).trim()} -->\n"
            is Resource -> "$indentSpaces<string name=\"${line.key.text.cleaned()}\">\"${line.value.text.trim()}\"</string>\n"
        }
    }

    private fun getiOSStringResource(
        line: Line
    ): String {
        return when (line) {
            is Comment  -> "\n// ${line.text.substring(1).trim()}\n"
            is Resource -> "\"${line.key.text.cleaned()}\" = \"${line.value.text.trim().replace("%s", "%@")}\";\n"
        }
    }

}

fun String.cleaned(): String =
    this.trim().toLowerCase().replace(" ", "_")
