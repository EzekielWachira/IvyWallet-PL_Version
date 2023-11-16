package com.ivy.core.domain.utils

import com.ivy.core.domain.pure.util.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class TestDispatchers(
    val testDispatchers: TestDispatcher = StandardTestDispatcher()
) : DispatcherProvider {

    override val main: CoroutineDispatcher
        get() = testDispatchers
    override val default: CoroutineDispatcher
        get() = testDispatchers
    override val io: CoroutineDispatcher
        get() = testDispatchers
    override val unconfined: CoroutineDispatcher
        get() = testDispatchers
}