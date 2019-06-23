package com.github.overpass.gather.screen.auth.register.signup

sealed class SignUpStatus {

    data class Error(val throwable: Throwable) : SignUpStatus()

    data class InvalidEmail(val message: String) : SignUpStatus()

    data class InvalidPassword(val message: String) : SignUpStatus()

    object Success : SignUpStatus()

    object Progress : SignUpStatus()
}