package com.github.overpass.gather.commons.abstractions;

/**
 * Workaround to do without sealed classes in kotlin
 */
public abstract class Sealed {

    public abstract String tag();

    public <T extends Sealed> T as(Class<T> klass) {
        if (this.getClass().equals(klass)) {
            return klass.cast(this);
        } else {
            throw new ClassCastException("Cannot cast");
        }
    }
}
