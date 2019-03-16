package com.gurpreetsk

import com.google.common.truth.Truth.assertThat
import com.gurpreetsk.formatter.AndroidEngFormatter
import com.gurpreetsk.formatter.IosFormatter
import com.gurpreetsk.internal.*
import org.junit.Test

class FormatterTests {
    @Test fun `getAndroidStrings returns properly formatted and indented file content`() {
        val lines = setOf(
            Comment("# Just a random line."),
            Resource(ResourceKey("hello world"), ResourceEnglishValue("Hello, World!"))
        )
        val androidFormattedStrings = AndroidEngFormatter().format(lines)

        assertThat(androidFormattedStrings)
            .isEqualTo("<resources>\n\n  <!-- Just a random line. -->\n  <string name=\"hello_world\">\"Hello, World!\"</string>\n</resources>\n")
    }

    @Test fun `templates are handled properly by getAndroidStrings`() {
        val lines = setOf(
            Comment("# Just a random line."),
            Resource(ResourceKey("hello world"), ResourceEnglishValue("Hello, <username>!"))
        )
        val androidFormattedStrings = AndroidEngFormatter().format(lines)

        assertThat(androidFormattedStrings)
            .isEqualTo("<resources>\n\n  <!-- Just a random line. -->\n  <string name=\"hello_world\">\"Hello, %s!\"</string>\n</resources>\n")
    }

    @Test fun `getiOSStrings returns properly formatted file content`() {
        val lines = setOf(
            Comment("# Just a random line."),
            Resource(ResourceKey("hello world"), ResourceEnglishValue("Hello, World!"))
        )
        val iosStrings = IosFormatter().format(lines)

        assertThat(iosStrings)
            .isEqualTo("\n// Just a random line.\n\"hello_world\" = \"Hello, World!\";\n")
    }

    @Test fun `templates are handled properly by getiOSStrings`() {
        val lines = setOf(
            Comment("# Just a random line."),
            Resource(ResourceKey("hello world"), ResourceEnglishValue("Hello, <username>!"))
        )
        val iosStrings = IosFormatter().format(lines)

        assertThat(iosStrings)
            .isEqualTo("\n// Just a random line.\n\"hello_world\" = \"Hello, %@!\";\n")
    }
}
