package com.github.overpass.gather.screen.auth.login

sealed class SignInStatus {

    class Error(val throwable: Throwable) : SignInStatus()

    class InvalidEmail(val message: String) : SignInStatus()

    class InvalidPassword(val message: String) : SignInStatus()

    object Success : SignInStatus()

    object Progress : SignInStatus()
}
