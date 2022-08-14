package com.quangpv.punk.helper.network

import com.quangpv.punk.helper.testing.OpenForTesting
import kotlinx.coroutines.delay

@OpenForTesting
class MockAsync<T>(private val value: T) : Async<T> {
    override suspend fun awaitNullable(): T? {
        delay(1000)
        return value
    }

    override suspend fun clone(): Async<T> {
        return MockAsync(value)
    }
}