package com.github.overpass.gather.commons.abstractions

import com.github.overpass.gather.commons.exception.DefaultException

sealed class SimpleResult {

    object Success : SimpleResult()

    data class Error(val exception: Exception = DefaultException()) : SimpleResult()
}