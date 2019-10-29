package by.overpass.gather.commons.abstractions

import by.overpass.gather.commons.exception.DefaultException

sealed class SimpleResult {

    object Success : SimpleResult()

    data class Error(val exception: Exception = DefaultException()) : SimpleResult()
}