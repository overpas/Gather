package by.overpass.gather.model.usecase.permission

import by.overpass.gather.ui.map.PermissionRequestResult
import com.hadilq.liveevent.LiveEvent

class LocationPermissionUseCase(
        val permissionRequestResultData: LiveEvent<PermissionRequestResult>
) {

    fun onRequestPermissionsResult(requestCode: Int,
                                   permissions: Array<String>,
                                   grantResults: IntArray) {
        permissionRequestResultData.value = PermissionRequestResult(requestCode,
                permissions, grantResults)
    }
}
