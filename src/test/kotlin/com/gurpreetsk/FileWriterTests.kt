package com.gurpreetsk

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.io.File

class FileWriterTests {
    @Test fun `write android strings-xml file correctly`() {
        val base = "src/test/kotlin/com/gurpreetsk"
        val fileWriter = FileWriter()
        val parentDirectory = fileWriter.createParentDirectory("$base/utils/parent")
        val expectedFileContent = "<!-- Here comes the comment -->\n<string name=\"hello_world\">Hello, World!</string>".trim()

        // Act
        fileWriter.createAndroidStringsFile(parentDirectory, expectedFileContent)

        // Assert
        val actualFileContents = File("$parentDirectory/strings.xml").readText().trim()
        assertThat(actualFileContents)
            .isEqualTo(expectedFileContent)
    }

    @Test fun `write iOS localizable-strings file correctly`() {
        val base = "src/test/kotlin/com/gurpreetsk"
        val fileWriter = FileWriter()
        val parentDirectory = fileWriter.createParentDirectory("$base/utils/parent")
        val expectedFileContent = "# Here comes the comment\n\"hello_world\" = \"Hello, World!\"".trim()

        // Act
        fileWriter.createiOSStringsFile(parentDirectory, expectedFileContent)

        // Assert
        val actualFileContents = File("$parentDirectory/localizable.strings").readText().trim()
        assertThat(actualFileContents)
            .isEqualTo(expectedFileContent)
    }
}
