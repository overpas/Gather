package by.overpass.gather.model.entity.confirm

sealed class ConfirmEmailStatus {

    object Success : ConfirmEmailStatus()

    data class Error(val throwable: Throwable) : ConfirmEmailStatus()

    object Progress : ConfirmEmailStatus()
}
