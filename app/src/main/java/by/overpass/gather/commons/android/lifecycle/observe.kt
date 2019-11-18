package by.overpass.gather.commons.android.lifecycle

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LifecycleOwner.on(liveData: LiveData<T>, callback: (T) -> Unit) {
    liveData.observe(this, Observer { it?.let { callback(it) } })
}

fun <T> Fragment.on(liveData: LiveData<T>, callback: (T) -> Unit) {
    viewLifecycleOwner.on(liveData, callback)
}

fun <T> LifecycleOwner.onMaybeNull(liveData: LiveData<T>, callback: (T?) -> Unit) {
    liveData.observe(this, Observer { callback(it) })
}

fun <T> Fragment.onMaybeNull(liveData: LiveData<T>, callback: (T?) -> Unit) {
    viewLifecycleOwner.onMaybeNull(liveData, callback)
}

