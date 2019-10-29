package by.overpass.gather.model.data.entity.confirm

sealed class ConfirmEmailStatus {

    object Success : ConfirmEmailStatus()

    data class Error(val throwable: Throwable) : ConfirmEmailStatus()

    object Progress : ConfirmEmailStatus()
}
