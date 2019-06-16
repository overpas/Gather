package com.github.overpass.gather.model.commons

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks

inline fun <T, R> Task<T>.map(crossinline mapper: (T?) -> R?): Task<R> {
    return onSuccessTask<R> {
        Tasks.forResult(mapper(it))
    }
}

inline fun <T, R> Task<T>.toLiveData(
        onStart: () -> R?,
        crossinline onSuccessMap: (value: T?) -> R?,
        crossinline onFailureMap: (exception: Exception) -> R?
): LiveData<R> {
    val liveData = MutableLiveData<R>()
    liveData.value = onStart()
    addOnSuccessListener { liveData.value = onSuccessMap(it) }
    addOnFailureListener { liveData.value = onFailureMap(it) }
    return liveData
}