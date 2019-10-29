package by.overpass.gather.model.usecase.permission

import by.overpass.gather.commons.android.lifecycle.SingleLiveEvent
import by.overpass.gather.ui.map.PermissionRequestResult

class LocationPermissionUseCase(
        val permissionRequestResultData: SingleLiveEvent<PermissionRequestResult>
) {

    fun onRequestPermissionsResult(requestCode: Int,
                                   permissions: Array<String>,
                                   grantResults: IntArray) {
        permissionRequestResultData.value = PermissionRequestResult(requestCode,
                permissions, grantResults)
    }
}
