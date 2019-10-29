package com.github.overpass.gather.ui.map;

public class PermissionRequestResult {

    private int requestCode;
    private String[] permissions;
    private int[] grantResults;

    public PermissionRequestResult(int requestCode, String[] permissions, int[] grantResults) {
        this.requestCode = requestCode;
        this.permissions = permissions;
        this.grantResults = grantResults;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public String[] getPermissions() {
        return permissions;
    }

    public int[] getGrantResults() {
        return grantResults;
    }
}
