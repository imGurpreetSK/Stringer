package com.gurpreetsk

import com.google.common.truth.Truth.assertThat
import com.gurpreetsk.internal.*
import org.junit.Test

class UtilsTests {
    @Test fun `getiOSStrings returns properly formatted file content`() {
        val lines = setOf(
            Comment("# Just a random line."),
            Resource(ResourceKey("hello world"), ResourceValue("Hello, World!"))
        )
        val iosStrings = Utils.getiOSStrings(lines).toString()

        assertThat(iosStrings)
            .isEqualTo("\n// Just a random line.\n\"hello_world\" = \"Hello, World!\";\n")
    }

    @Test fun `getAndroidStrings returns properly formatted and indented file content`() {
        val lines = setOf(
            Comment("# Just a random line."),
            Resource(ResourceKey("hello world"), ResourceValue("Hello, World!"))
        )
        val iosStrings = Utils.getAndroidStrings(lines).toString()

        assertThat(iosStrings)
            .isEqualTo("<resources>\n\n  <!-- Just a random line. -->\n  <string name=\"hello_world\">\"Hello, World!\"</string>\n</resources>\n")
    }

    @Test fun `templates are handled properly by getiOSStrings`() {
        val lines = setOf(
            Comment("# Just a random line."),
            Resource(ResourceKey("hello world"), ResourceValue("Hello, <username>!"))
        )
        val iosStrings = Utils.getiOSStrings(lines).toString()

        assertThat(iosStrings)
            .isEqualTo("\n// Just a random line.\n\"hello_world\" = \"Hello, %@!\";\n")
    }

    @Test fun `templates are handled properly by getAndroidStrings`() {
        val lines = setOf(
            Comment("# Just a random line."),
            Resource(ResourceKey("hello world"), ResourceValue("Hello, <username>!"))
        )
        val iosStrings = Utils.getAndroidStrings(lines).toString()

        assertThat(iosStrings)
            .isEqualTo("<resources>\n\n  <!-- Just a random line. -->\n  <string name=\"hello_world\">\"Hello, %s!\"</string>\n</resources>\n")
    }
}
