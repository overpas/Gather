package com.github.overpass.gather.model.commons

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class LifecycleDisposable<T>(
        lifecycle: Lifecycle,
        value: T
) {

    var value: T? = null
        private set

    init {
        this.value = value
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                this@LifecycleDisposable.value = null
            }
        })
    }
}