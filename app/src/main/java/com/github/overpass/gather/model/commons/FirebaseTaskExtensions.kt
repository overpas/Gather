package com.github.overpass.gather.model.commons

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.overpass.gather.model.commons.exception.DefaultException
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.storage.CancellableTask
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

inline fun <T, R> Task<T>.map(crossinline successMapper: (T?) -> R?): Task<R> {
    return continueWithTask<R> {
        when {
            it.isSuccessful -> Tasks.forResult(successMapper(it.result))
            else -> Tasks.forException(it.exception ?: DefaultException())
        }
    }
}

inline fun <T, R> Task<T>.flatMap(crossinline mapper: (T?) -> Task<R>): Task<R> {
    return continueWithTask<R> {
        mapper(it.result)
    }
}

inline fun <T, R> Task<T>.mapToSuccess(
        crossinline successMapper: (T?) -> R?,
        crossinline failureMapper: (Exception) -> R?
): Task<R> {
    return continueWithTask<R> {
        when {
            it.isSuccessful -> Tasks.forResult(successMapper(it.result))
            else -> Tasks.forResult(failureMapper(it.exception ?: DefaultException()))
        }
    }
}

@ExperimentalCoroutinesApi
inline fun <T, R> Task<T>.asFlow(
        crossinline onSuccess: (value: T?) -> R,
        crossinline onFailure: (exception: Exception) -> R
): Flow<R> = callbackFlow {
    addOnSuccessListener { offer(onSuccess(it)) }
    addOnFailureListener { offer(onFailure(it)) }
}

@ExperimentalCoroutinesApi
inline fun <T, R> Task<T>.asFlow(
        crossinline onStart: () -> R,
        crossinline onSuccess: (value: T) -> R,
        crossinline onFailure: (exception: Exception) -> R
): Flow<R> = callbackFlow {
    send(onStart())
    addOnSuccessListener { offer(onSuccess(it)) }
    addOnFailureListener { offer(onFailure(it)) }
}

@ExperimentalCoroutinesApi
fun <T> Task<T>.asResultFlow(): Flow<Result<Unit>> = callbackFlow {
    send(Result.Loading)
    addOnSuccessListener { offer(Result.Success(Unit)) }
    addOnFailureListener { offer(Result.Error(it)) }
    awaitClose()
}

@ExperimentalCoroutinesApi
inline fun <T, R> CancellableTask<T>.asFlow(
        crossinline onStart: () -> R,
        crossinline onProgress: (value: T) -> R,
        crossinline onSuccess: (value: T?) -> R,
        crossinline onFailure: (exception: Exception) -> R
): Flow<R> = callbackFlow {
    send(onStart())
    addOnProgressListener { offer(onProgress(it)) }
    addOnSuccessListener { offer(onSuccess(it)) }
    addOnFailureListener { offer(onFailure(it)) }
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