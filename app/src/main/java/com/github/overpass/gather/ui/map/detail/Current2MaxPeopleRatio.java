package com.github.overpass.gather.ui.map.detail;

public class Current2MaxPeopleRatio {

    public static final Current2MaxPeopleRatio FAILED = new Current2MaxPeopleRatio(0, 0);
    private final int current;
    private final int max;

    public Current2MaxPeopleRatio(int current, int max) {
        this.current = current;
        this.max = max;
    }

    public int getCurrent() {
        return current;
    }

    public int getMax() {
        return max;
    }
}
