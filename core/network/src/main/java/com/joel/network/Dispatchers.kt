package com.joel.network

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val appDispatchers: SkyCastDispatchers)

enum class SkyCastDispatchers {
    IO,
    MAIN,
    DEFAULT
}