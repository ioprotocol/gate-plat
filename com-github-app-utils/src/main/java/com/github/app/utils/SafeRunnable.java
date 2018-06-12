package com.github.app.utils;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.function.Consumer;

/**
 * A runnable that catches runtime exceptions.
 */
@FunctionalInterface
public interface SafeRunnable extends Runnable {

    Logger LOGGER = LoggerFactory.getLogger(SafeRunnable.class);

    @Override
    default void run() {
        try {
            safeRun();
        } catch (Throwable t) {
            LOGGER.error("Unexpected throwable caught ", t);
        }
    }

    void safeRun();

    /**
     * Utility method to use SafeRunnable from lambdas.
     *
     * <p>Eg:
     * <pre>
     * <code>
     * executor.submit(SafeRunnable.safeRun(() -> {
     *    // My not-safe code
     * });
     * </code>
     * </pre>
     */
    static SafeRunnable safeRun(Runnable runnable) {
        return new SafeRunnable() {
            @Override
            public void safeRun() {
                runnable.run();
            }
        };
    }

    /**
     * Utility method to use SafeRunnable from lambdas with
     * a custom exception handler.
     *
     * <p>Eg:
     * <pre>
     * <code>
     * executor.submit(SafeRunnable.safeRun(() -> {
     *    // My not-safe code
     * }, exception -> {
     *    // Handle exception
     * );
     * </code>
     * </pre>
     *
     * @param runnable
     * @param exceptionHandler
     *            handler that will be called when there are any exception
     * @return
     */
    static SafeRunnable safeRun(Runnable runnable, Consumer<Throwable> exceptionHandler) {
        return () -> {
            try {
                runnable.run();
            } catch (Throwable t) {
                exceptionHandler.accept(t);
                throw t;
            }
        };
    }
}

