package com.github.app.utils.stats;

/**
 * A guage is a value that has only one value at a specific point in time.
 * An example is the number of elements in a queue. The value of T must be
 * some numeric type.
 */
public interface Gauge<T extends Number> {
    T getDefaultValue();
    T getSample();
}
