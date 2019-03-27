package com.github.overpass.gather.map;

import com.github.overpass.gather.SingleLiveEvent;

class LocationPermissionUseCase {

    private final SingleLiveEvent<PermissionRequestResult> permissionRequestResultData;

    LocationPermissionUseCase() {
        this.permissionRequestResultData = new SingleLiveEvent<>();
    }

    public SingleLiveEvent<PermissionRequestResult> getPermissionRequestResultData() {
        return permissionRequestResultData;
    }

    void onRequestPermissionsResult(int requestCode,
                                    String[] permissions,
                                    int[] grantResults) {
        permissionRequestResultData.setValue(new PermissionRequestResult(requestCode,
                permissions, grantResults));
    }
}
