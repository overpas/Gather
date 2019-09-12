package com.github.overpass.gather.model.commons

import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

class WeakCached<T>(theValue: T?, private val fetchValue: () -> T) {

    private var valueReference = WeakReference(theValue)

    operator fun getValue(thisRef: Any?, prop: KProperty<*>): T {
        return valueReference.get() ?: fetchValue().also {
            setValue(thisRef, prop, it)
        }
    }

    operator fun setValue(thisRef: Any?, prop: KProperty<*>, value: T?) {
        valueReference = WeakReference(value)
    }
}

fun <T> weakCached(valueFetcher: () -> T): WeakCached<T> = weakCached(null, valueFetcher)

fun <T> weakCached(value: T?, fetcher: () -> T): WeakCached<T> = WeakCached(value, fetcher)