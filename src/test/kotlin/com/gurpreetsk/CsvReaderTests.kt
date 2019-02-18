package com.gurpreetsk

import com.google.common.truth.Truth.assertThat
import com.gurpreetsk.internal.*
import org.junit.Test
import java.io.File

class CsvReaderTests {
    private val base = "src/test/kotlin/com/gurpreetsk"

    @Test(expected = FileSystemException::class)
    fun `throw exception if file is not readable`() {
        val path = "utils/teststrings.csv"
        val file = File(path)
        file.setReadable(false)

        val csvReader = CsvReader(FilePath(path))
        csvReader.parseCsv()
    }

    @Test fun `comments are parsed correctly`() {
        val path = FilePath("$base/utils/comment.csv")
        val csvReader = CsvReader(path)
        assertThat(csvReader.parseCsv())
            .containsExactly(
                Comment("# This is a comment"),
                Comment("# This is another comment")
            )
    }

    @Test fun `string resources are parsed correctly`() {
        val path = FilePath("$base/utils/teststrings.csv")
        val csvReader = CsvReader(path)
        assertThat(csvReader.parseCsv())
            .containsExactly(
                Resource(ResourceKey("error_hello1"), ResourceValue("Hello")),
                Resource(ResourceKey("hello2"), ResourceValue("Hi")),
                Resource(ResourceKey("template_hello3"), ResourceValue("Hi <username>"))
            )
    }

    @Test fun `files with both comments and resources are correctly parsed`() {
        val path = FilePath("$base/utils/compoundstrings.csv")
        val csvReader = CsvReader(path)
        assertThat(csvReader.parseCsv())
            .containsExactly(
                Comment("# KEY, VALUE, TYPE"),
                Resource(ResourceKey("error_hello1"), ResourceValue("Hello")),
                Resource(ResourceKey("hello2"), ResourceValue("Hi")),
                Resource(ResourceKey("error_actor_details_no_connection"), ResourceValue("Not connected to Internet")),
                Resource(ResourceKey("template_hello4"), ResourceValue("Howdy Yo")),
                Comment("# This is a comment"),
                Resource(ResourceKey("template_sample"), ResourceValue("<some random contextual text> should replace this."))
            )
    }

    @Test fun `escape quotes for a given value string, leave key untouched`() {
        val path = FilePath("$base/utils/stringswithquotes.csv")
        val csvReader = CsvReader(path)
        assertThat(csvReader.parseCsv())
            .containsExactly(
                Resource(ResourceKey("error_hello1"), ResourceValue("Hello")),
                Resource(ResourceKey("hello2"), ResourceValue("Hi")),
                Resource(ResourceKey("error_actor_details_no_connection"), ResourceValue("Not connected to Internet")),
                Comment("# This is a comment"),
                Resource(ResourceKey("template_sample"), ResourceValue("<some random contextual text> should replace this.")),
                Resource(ResourceKey("sample_with_quotes"), ResourceValue("hello \\\"stranger\\\"")),
                Resource(ResourceKey("sample_with_quotes_2"), ResourceValue("hello stranger"))
            )
    }

    @Test fun `remove quotes at extreme ends in a given value string`() {
        val path = FilePath("$base/utils/stringswithquotes.csv")
        val csvReader = CsvReader(path)
        assertThat(csvReader.parseCsv())
            .containsExactly(
                Resource(ResourceKey("error_hello1"), ResourceValue("Hello")),
                Resource(ResourceKey("hello2"), ResourceValue("Hi")),
                Resource(ResourceKey("error_actor_details_no_connection"), ResourceValue("Not connected to Internet")),
                Comment("# This is a comment"),
                Resource(ResourceKey("template_sample"), ResourceValue("<some random contextual text> should replace this.")),
                Resource(ResourceKey("sample_with_quotes"), ResourceValue("hello \\\"stranger\\\"")),
                Resource(ResourceKey("sample_with_quotes_2"), ResourceValue("hello stranger"))
            )
    }
}
