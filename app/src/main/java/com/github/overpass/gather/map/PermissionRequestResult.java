package com.github.overpass.gather.map;

class PermissionRequestResult {

    private int requestCode;
    private String[] permissions;
    private int[] grantResults;

    PermissionRequestResult(int requestCode, String[] permissions, int[] grantResults) {
        this.requestCode = requestCode;
        this.permissions = permissions;
        this.grantResults = grantResults;
    }

    int getRequestCode() {
        return requestCode;
    }

    String[] getPermissions() {
        return permissions;
    }

    int[] getGrantResults() {
        return grantResults;
    }
}
