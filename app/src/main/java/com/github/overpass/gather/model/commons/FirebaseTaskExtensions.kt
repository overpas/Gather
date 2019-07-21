package com.github.overpass.gather.model.commons

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.overpass.gather.model.commons.exception.DefaultException
import com.github.overpass.gather.model.data.entity.signin.SignInStatus
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks

inline fun <T, R> Task<T>.map(crossinline mapper: (T?) -> R?): Task<R> {
    return onSuccessTask<R> {
        Tasks.forResult(mapper(it))
    }
}

inline fun <T, R> Task<T>.map(
        crossinline successMapper: (T?) -> R?,
        crossinline failureMapper: (Exception) -> R?
): Task<R> {
    return continueWithTask(Continuation {
        return@Continuation when {
            it.isSuccessful -> Tasks.forResult(successMapper(it.result))
            else ->  Tasks.forResult(failureMapper(it.exception ?: DefaultException()))
        }
    })
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

inline fun <T, R> Task<T>.toLiveData(
        crossinline onSuccessMap: (value: T?) -> R?,
        crossinline onFailureMap: (exception: Exception) -> R?
): LiveData<R> {
    val liveData = MutableLiveData<R>()
    addOnSuccessListener { liveData.value = onSuccessMap(it) }
    addOnFailureListener { liveData.value = onFailureMap(it) }
    return liveData
}