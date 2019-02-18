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
        val indentLevelInSpaces = "  "
        return when (line) {
            is Comment  -> "\n$indentLevelInSpaces<!-- ${line.text.substring(1).trim()} -->\n"
            is Resource -> "$indentLevelInSpaces<string name=\"${line.key.text.clean()}\">\"${line.value.text.trim().convertToAndroidTemplate()}\"</string>\n"
        }
    }

    private fun getiOSStringResource(
        line: Line
    ): String {
        return when (line) {
            is Comment  -> "\n// ${line.text.substring(1).trim()}\n"
            is Resource -> "\"${line.key.text.clean()}\" = \"${line.value.text.trim().convertToiOSTemplate()}\";\n"
        }
    }

    private fun String.convertToAndroidTemplate(): String {
        val regex = "<(.*?)>".toRegex()
        return this.replace(regex, "%s")
    }

    private fun String.convertToiOSTemplate(): String {
        val regex = "<(.*?)>".toRegex()
        return this.replace(regex, "%@")
    }
}

fun String.clean(): String = this.trim().toLowerCase().replace(" ", "_")
