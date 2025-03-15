package com.joe.data.utils

sealed class Resource<T>(open val data : T ?= null, open val error: String ?= null) {
    data class Success<T>(override val data: T) : Resource<T>()
    data class Failure<T>(override val error: String) : Resource<T>()
    data class Loading<T>(override val data : T ?= null) : Resource<T>()
}