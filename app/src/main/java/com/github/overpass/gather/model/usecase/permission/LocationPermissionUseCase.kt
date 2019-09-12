package com.github.overpass.gather.model.usecase.permission

import com.github.overpass.gather.model.commons.SingleLiveEvent
import com.github.overpass.gather.screen.map.PermissionRequestResult

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
