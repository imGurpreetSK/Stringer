package com.gurpreetsk

import com.gurpreetsk.internal.FilePath
import org.junit.Test
import java.io.File

class CsvReaderTests {
    @Test(expected = FileSystemException::class)
    fun `throw exception if file is not readable`() {
        val path = "/utils/teststrings.csv"
        val file = File(path)
        file.setReadable(false)

        val csvReader = CsvReader(FilePath(path))
        csvReader.parseCsv()
    }
}
