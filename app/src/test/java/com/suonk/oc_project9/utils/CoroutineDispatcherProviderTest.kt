package com.suonk.oc_project9.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.coroutines.Dispatchers
import org.junit.Test

class CoroutineDispatcherProviderTest {

    @Test
    fun `verify coroutine dispatchers`() {
        // WHEN
        val result = CoroutineDispatcherProvider()

        // THEN
        assertThat(result.main).isEqualTo(Dispatchers.Main)
        assertThat(result.io).isEqualTo(Dispatchers.IO)
    }
}