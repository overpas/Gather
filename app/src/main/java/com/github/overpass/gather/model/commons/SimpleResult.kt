package com.github.overpass.gather.model.commons

import com.github.overpass.gather.model.commons.exception.DefaultException
import java.lang.Exception

sealed class SimpleResult {

    object Success : SimpleResult()

    data class Error(val exception: Exception = DefaultException()) : SimpleResult()
}