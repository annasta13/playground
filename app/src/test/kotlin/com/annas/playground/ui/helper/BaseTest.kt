package com.annas.playground.ui.helper

import kotlinx.coroutines.Dispatchers
import org.junit.Rule
import org.mockito.Mockito

abstract class BaseTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    val dispatcher = Dispatchers.Unconfined

    inline fun <reified T> anyMock(type: Class<T>): T = Mockito.any(type)
}
