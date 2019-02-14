package com.gurpreetsk

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CanaryTest {
    @Test fun `This should fail if setup isn't correct`() {
        assertThat(true).isTrue()
    }
}
