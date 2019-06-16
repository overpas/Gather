package com.github.overpass.gather.model.commons

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations

inline fun <T> LiveData<T>.then(crossinline action: () -> Unit): LiveData<T> {
    return Transformations.map(this) {
        action()
        it
    }
}