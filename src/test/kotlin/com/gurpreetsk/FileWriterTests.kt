package com.gurpreetsk

import com.google.common.truth.Truth.assertThat
import com.gurpreetsk.writer.AndroidFileWriter
import com.gurpreetsk.writer.DirectoryManager
import com.gurpreetsk.writer.IosFileWriter
import org.junit.Before
import org.junit.Test
import java.io.File

class FileWriterTests {
    private val base = "src/test/kotlin/com/gurpreetsk"
    private lateinit var parentDirectory: File

    @Before fun setup() {
        parentDirectory = DirectoryManager.createParentDirectory("$base/utils/parent")
    }

    @Test fun `write android strings-xml file correctly`() {
        val fileWriter = AndroidFileWriter()
        val expectedFileContent = "<!-- Here comes the comment -->\n<string name=\"hello_world\">Hello, World!</string>".trim()

        // Act
        fileWriter.write(parentDirectory, expectedFileContent)

        // Assert
        val actualFileContents = File("$parentDirectory/strings.xml").readText().trim()
        assertThat(actualFileContents)
            .isEqualTo(expectedFileContent)
    }

    @Test fun `write iOS localizable-strings file correctly`() {
        val fileWriter = IosFileWriter()
        val expectedFileContent = "# Here comes the comment\n\"hello_world\" = \"Hello, World!\"".trim()

        // Act
        fileWriter.write(parentDirectory, expectedFileContent)

        // Assert
        val actualFileContents = File("$parentDirectory/localizable.strings").readText().trim()
        assertThat(actualFileContents)
            .isEqualTo(expectedFileContent)
    }
}
