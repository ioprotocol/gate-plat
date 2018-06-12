package com.github.app.utils.stats;

import java.util.concurrent.TimeUnit;

/**
 * This interface handles logging of statistics related to each operation. (PUBLISH, CONSUME etc.)
 */
public interface OpStatsLogger {

    /**
     * Increment the failed op counter with the given eventLatency.
     * @param eventLatencyMillis The event latency
     * @param unit
     */
    void registerFailedEvent(long eventLatencyMillis, TimeUnit unit);

    /**
     * An operation succeeded with the given eventLatency. Update
     * stats to reflect the same
     * @param eventLatencyMillis The event latency
     * @param unit
     */
    void registerSuccessfulEvent(long eventLatencyMillis, TimeUnit unit);

    /**
     * An operation with the given value succeeded.
     * @param value
     */
    void registerSuccessfulValue(long value);

    /**
     * An operation with the given value failed.
     */
    void registerFailedValue(long value);

    /**
     * @return Returns an OpStatsData object with necessary values. We need this function
     * to support JMX exports. This should be deprecated sometime in the near future.
     * populated.
     */
    OpStatsData toOpStatsData();

    /**
     * Clear stats for this operation.
     */
    void clear();
}

