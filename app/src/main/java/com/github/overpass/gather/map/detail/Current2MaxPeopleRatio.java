package com.github.overpass.gather.map.detail;

public class Current2MaxPeopleRatio {

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
