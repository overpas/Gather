package by.overpass.gather.commons.android.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class LifecycleDisposable<T>(
        lifecycle: Lifecycle,
        value: T
) {

    var value: T? = value
        private set

    init {
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                this@LifecycleDisposable.value = null
            }
        })
    }
}