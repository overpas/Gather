package com.github.overpass.gather.model.commons

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.overpass.gather.model.commons.exception.DefaultException
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import java.util.concurrent.Executor

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

inline fun <T> Task<T>.take(count: Int): Task<T> {
    var invocations = 0
    return object : Task<T>() {
        override fun isComplete(): Boolean = this@take.isComplete

        override fun getException(): java.lang.Exception? = this@take.exception

        override fun addOnFailureListener(p0: OnFailureListener): Task<T> =
                this@take.addOnFailureListener(p0)

        override fun addOnFailureListener(p0: Executor, p1: OnFailureListener): Task<T> =
                this@take.addOnFailureListener(p0, p1)

        override fun addOnFailureListener(p0: Activity, p1: OnFailureListener): Task<T> =
                this@take.addOnFailureListener(p0, p1)

        override fun getResult(): T? = this@take.result

        override fun <X : Throwable?> getResult(p0: Class<X>): T? = this@take.getResult(p0)

        override fun addOnSuccessListener(onSuccessListener: OnSuccessListener<in T>): Task<T> {
            this@take.addOnSuccessListener{
                checkNumberOfInvocations(onSuccessListener, it)
            }
            return this
        }

        override fun addOnSuccessListener(
                executor: Executor,
                onSuccessListener: OnSuccessListener<in T>
        ): Task<T> {
            this@take.addOnSuccessListener(executor, OnSuccessListener {
                checkNumberOfInvocations(onSuccessListener, it)
            })
            return this
        }

        override fun addOnSuccessListener(
                activity: Activity,
                onSuccessListener: OnSuccessListener<in T>
        ): Task<T> {
            this@take.addOnSuccessListener(activity) {
                checkNumberOfInvocations(onSuccessListener, it)
            }
            return this
        }

        override fun isSuccessful(): Boolean = this@take.isSuccessful

        override fun isCanceled(): Boolean = this@take.isCanceled

        private fun checkNumberOfInvocations(listener: OnSuccessListener<in T>, value: T) {
            if (invocations < count) {
                listener.onSuccess(value)
                invocations++
            }
        }
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

inline fun <T, R> Task<T>.toLiveData(
        crossinline onSuccessMap: (value: T?) -> R?,
        crossinline onFailureMap: (exception: Exception) -> R?
): LiveData<R> {
    val liveData = MutableLiveData<R>()
    addOnSuccessListener { liveData.value = onSuccessMap(it) }
    addOnFailureListener { liveData.value = onFailureMap(it) }
    return liveData
}