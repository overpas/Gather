package by.overpass.gather.model.entity.forgot

sealed class ForgotStatus {

    data class Error(val throwable: Throwable) : ForgotStatus()

    object InvalidEmail : ForgotStatus()

    object Success : ForgotStatus()

    object Progress : ForgotStatus()
}
