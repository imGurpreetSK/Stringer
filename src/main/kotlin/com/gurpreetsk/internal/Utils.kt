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
            is Resource -> {
                val type = if (line.type == null) { String.empty() } else { line.type.text.trim().toLowerCase() + "_" }
                "$indentSpaces<string name=\"$type${line.key.text.cleaned()}\">\"${line.value.text.trim()}\"</string>\n"
            }
        }
    }

    private fun getiOSStringResource(
        line: Line
    ): String {
        return when (line) {
            is Comment  -> "\n// ${line.text.substring(1).trim()}\n"
            is Resource -> {
                val type = if (line.type == null) { String.empty() } else { line.type.text.trim().toLowerCase() + "_" }
                "\"$type${line.key.text.cleaned()}\" = \"${line.value.text.trim().replace("%s", "%@")}\";\n"
            }
        }
    }

    private fun String.cleaned(): String = this.trim().toLowerCase().replace(" ", "_")
    private fun String.Companion.empty(): String = ""
}
