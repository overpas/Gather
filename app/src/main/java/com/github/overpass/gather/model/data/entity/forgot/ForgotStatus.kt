package com.github.overpass.gather.model.data.entity.forgot

sealed class ForgotStatus {

    data class Error(val throwable: Throwable) : ForgotStatus()

    object InvalidEmail : ForgotStatus()

    object Success : ForgotStatus()

    object Progress : ForgotStatus()
}
