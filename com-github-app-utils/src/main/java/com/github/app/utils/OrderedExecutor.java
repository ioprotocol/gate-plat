package com.github.app.utils;

import com.github.app.utils.stats.Gauge;
import com.github.app.utils.stats.NullStatsLogger;
import com.github.app.utils.stats.OpStatsLogger;
import com.github.app.utils.stats.StatsLogger;
import com.google.common.util.concurrent.ForwardingExecutorService;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * This class provides 2 things over the java {@link ExecutorService}.
 *
 * <p>1. It takes {@link SafeRunnable objects} instead of plain Runnable objects.
 * This means that exceptions in scheduled tasks wont go unnoticed and will be
 * logged.
 *
 * <p>2. It supports submitting tasks with an ordering key, so that tasks submitted
 * with the same key will always be executed in order, but tasks across
 * different keys can be unordered. This retains parallelism while retaining the
 * basic amount of ordering we want (e.g. , per ledger handle). Ordering is
 * achieved by hashing the key objects to threads by their {@link #hashCode()}
 * method.
 */
public class OrderedExecutor implements ExecutorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderedExecutor.class);
    public static final int NO_TASK_LIMIT = -1;
    protected static final long WARN_TIME_MICRO_SEC_DEFAULT = TimeUnit.SECONDS.toMicros(1);

    final String name;
    final ExecutorService threads[];
    final long threadIds[];
    final Random rand = new Random();
    final OpStatsLogger taskExecutionStats;
    final OpStatsLogger taskPendingStats;
    final boolean traceTaskExecution;
    final long warnTimeMicroSec;
    final int maxTasksInQueue;

    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * A builder class for an OrderedExecutor.
     */
    public static class Builder extends AbstractBuilder<OrderedExecutor> {

        @Override
        public OrderedExecutor build() {
            if (null == threadFactory) {
                threadFactory = new DefaultThreadFactory("bookkeeper-ordered-safe-executor");
            }
            return new OrderedExecutor(name, numThreads, threadFactory, statsLogger,
                    traceTaskExecution, warnTimeMicroSec, maxTasksInQueue);
        }
    }

    /**
     * Abstract builder class to build {@link }.
     */
    public abstract static class AbstractBuilder<T extends OrderedExecutor> {
        protected String name = getClass().getSimpleName();
        protected int numThreads = Runtime.getRuntime().availableProcessors();
        protected ThreadFactory threadFactory = null;
        protected StatsLogger statsLogger = NullStatsLogger.INSTANCE;
        protected boolean traceTaskExecution = false;
        protected long warnTimeMicroSec = WARN_TIME_MICRO_SEC_DEFAULT;
        protected int maxTasksInQueue = NO_TASK_LIMIT;

        public AbstractBuilder<T> name(String name) {
            this.name = name;
            return this;
        }

        public AbstractBuilder<T> numThreads(int num) {
            this.numThreads = num;
            return this;
        }

        public AbstractBuilder<T> maxTasksInQueue(int num) {
            this.maxTasksInQueue = num;
            return this;
        }

        public AbstractBuilder<T> threadFactory(ThreadFactory threadFactory) {
            this.threadFactory = threadFactory;
            return this;
        }

        public AbstractBuilder<T> statsLogger(StatsLogger statsLogger) {
            this.statsLogger = statsLogger;
            return this;
        }

        public AbstractBuilder<T> traceTaskExecution(boolean enabled) {
            this.traceTaskExecution = enabled;
            return this;
        }

        public AbstractBuilder<T> traceTaskWarnTimeMicroSec(long warnTimeMicroSec) {
            this.warnTimeMicroSec = warnTimeMicroSec;
            return this;
        }

        @SuppressWarnings("unchecked")
        public T build() {
            if (null == threadFactory) {
                threadFactory = new DefaultThreadFactory(name);
            }
            return (T) new OrderedExecutor(
                    name,
                    numThreads,
                    threadFactory,
                    statsLogger,
                    traceTaskExecution,
                    warnTimeMicroSec,
                    maxTasksInQueue);
        }
    }

    /**
     * Decorator class for a runnable that measure the execution time.
     */
    protected class TimedRunnable implements Runnable {
        final Runnable runnable;
        final long initNanos;

        TimedRunnable(Runnable runnable) {
            this.runnable = runnable;
            this.initNanos = MathUtils.nowInNano();
        }

        @Override
        public void run() {
            taskPendingStats.registerSuccessfulEvent(MathUtils.elapsedNanos(initNanos), TimeUnit.NANOSECONDS);
            long startNanos = MathUtils.nowInNano();
            this.runnable.run();
            long elapsedMicroSec = MathUtils.elapsedMicroSec(startNanos);
            taskExecutionStats.registerSuccessfulEvent(elapsedMicroSec, TimeUnit.MICROSECONDS);
            if (elapsedMicroSec >= warnTimeMicroSec) {
                LOGGER.warn("Runnable {}:{} took too long {} micros to execute.", runnable, runnable.getClass(),
                        elapsedMicroSec);
            }
        }
    }

    protected ThreadPoolExecutor createSingleThreadExecutor(ThreadFactory factory) {
        return new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), factory);
    }

    protected ExecutorService getBoundedExecutor(ThreadPoolExecutor executor) {
        return new BoundedExecutorService(executor, this.maxTasksInQueue);
    }

    /**
     * Constructs Safe executor.
     *
     * @param numThreads
     *            - number of threads
     * @param baseName
     *            - base name of executor threads
     * @param threadFactory
     *            - for constructing threads
     * @param statsLogger
     *            - for reporting executor stats
     * @param traceTaskExecution
     *            - should we stat task execution
     * @param warnTimeMicroSec
     *            - log long task exec warning after this interval
     * @param maxTasksInQueue
     *            - maximum items allowed in a thread queue. -1 for no limit
     */
    protected OrderedExecutor(String baseName, int numThreads, ThreadFactory threadFactory,
                              StatsLogger statsLogger, boolean traceTaskExecution,
                              long warnTimeMicroSec, int maxTasksInQueue) {
        checkArgument(numThreads > 0);
        checkArgument(!StringUtils.isBlank(baseName));

        this.maxTasksInQueue = maxTasksInQueue;
        this.warnTimeMicroSec = warnTimeMicroSec;
        name = baseName;
        threads = new ExecutorService[numThreads];
        threadIds = new long[numThreads];
        for (int i = 0; i < numThreads; i++) {
            ThreadPoolExecutor thread = createSingleThreadExecutor(
                    new ThreadFactoryBuilder().setNameFormat(name + "-" + getClass().getSimpleName() + "-" + i + "-%d")
                            .setThreadFactory(threadFactory).build());
            threads[i] = getBoundedExecutor(thread);

            final int idx = i;
            try {
                threads[idx].submit(() -> {
                    threadIds[idx] = Thread.currentThread().getId();
                }).get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Couldn't start thread " + i, e);
            } catch (ExecutionException e) {
                throw new RuntimeException("Couldn't start thread " + i, e);
            }

            // Register gauges
            statsLogger.registerGauge(String.format("%s-queue-%d", name, idx), new Gauge<Number>() {
                @Override
                public Number getDefaultValue() {
                    return 0;
                }

                @Override
                public Number getSample() {
                    return thread.getQueue().size();
                }
            });
            statsLogger.registerGauge(String.format("%s-completed-tasks-%d", name, idx), new Gauge<Number>() {
                @Override
                public Number getDefaultValue() {
                    return 0;
                }

                @Override
                public Number getSample() {
                    return thread.getCompletedTaskCount();
                }
            });
            statsLogger.registerGauge(String.format("%s-total-tasks-%d", name, idx), new Gauge<Number>() {
                @Override
                public Number getDefaultValue() {
                    return 0;
                }

                @Override
                public Number getSample() {
                    return thread.getTaskCount();
                }
            });
        }

        // Stats
        this.taskExecutionStats = statsLogger.scope(name).getOpStatsLogger("task_execution");
        this.taskPendingStats = statsLogger.scope(name).getOpStatsLogger("task_queued");
        this.traceTaskExecution = traceTaskExecution;
    }

    /**
     * Schedules a one time action to execute with an ordering guarantee on the key.
     * @param orderingKey
     * @param r
     */
    public void executeOrdered(Object orderingKey, SafeRunnable r) {
        chooseThread(orderingKey).execute(timedRunnable(r));
    }

    /**
     * Schedules a one time action to execute with an ordering guarantee on the key.
     * @param orderingKey
     * @param r
     */
    public void executeOrdered(long orderingKey, SafeRunnable r) {
        chooseThread(orderingKey).execute(timedRunnable(r));
    }

    /**
     * Schedules a one time action to execute with an ordering guarantee on the key.
     * @param orderingKey
     * @param r
     */
    public void executeOrdered(int orderingKey, SafeRunnable r) {
        chooseThread(orderingKey).execute(timedRunnable(r));
    }

    public <T> ListenableFuture<T> submitOrdered(long orderingKey, Callable<T> task) {
        SettableFuture<T> future = SettableFuture.create();
        executeOrdered(orderingKey, () -> {
            try {
                T result = task.call();
                future.set(result);
            } catch (Throwable t) {
                future.setException(t);
            }
        });

        return future;
    }


    public long getThreadID(long orderingKey) {
        // skip hashcode generation in this special case
        if (threadIds.length == 1) {
            return threadIds[0];
        }

        return threadIds[MathUtils.signSafeMod(orderingKey, threadIds.length)];
    }

    public ExecutorService chooseThread() {
        // skip random # generation in this special case
        if (threads.length == 1) {
            return threads[0];
        }

        return threads[rand.nextInt(threads.length)];
    }

    public ExecutorService chooseThread(Object orderingKey) {
        // skip hashcode generation in this special case
        if (threads.length == 1) {
            return threads[0];
        }

        return threads[MathUtils.signSafeMod(orderingKey.hashCode(), threads.length)];
    }

    /**
     * skip hashcode generation in this special case.
     *
     * @param orderingKey long ordering key
     * @return the thread for executing this order key
     */
    public ExecutorService chooseThread(long orderingKey) {
        if (threads.length == 1) {
            return threads[0];
        }

        return threads[MathUtils.signSafeMod(orderingKey, threads.length)];
    }

    private Runnable timedRunnable(Runnable r) {
        if (traceTaskExecution) {
            return new TimedRunnable(r);
        } else {
            return r;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return chooseThread().submit(task);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return chooseThread().submit(timedRunnable(task), result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<?> submit(Runnable task) {
        return chooseThread().submit(timedRunnable(task));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
            throws InterruptedException {
        return chooseThread().invokeAll(tasks);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks,
                                         long timeout,
                                         TimeUnit unit)
            throws InterruptedException {
        return chooseThread().invokeAll(tasks, timeout, unit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks)
            throws InterruptedException, ExecutionException {
        return chooseThread().invokeAny(tasks);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        return chooseThread().invokeAny(tasks, timeout, unit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(Runnable command) {
        chooseThread().execute(command);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdown() {
        for (int i = 0; i < threads.length; i++) {
            threads[i].shutdown();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Runnable> shutdownNow() {
        List<Runnable> runnables = new ArrayList<Runnable>();
        for (ExecutorService executor : threads) {
            runnables.addAll(executor.shutdownNow());
        }
        return runnables;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isShutdown() {
        for (ExecutorService executor : threads) {
            if (!executor.isShutdown()) {
                return false;
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        boolean ret = true;
        for (int i = 0; i < threads.length; i++) {
            ret = ret && threads[i].awaitTermination(timeout, unit);
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTerminated() {
        for (ExecutorService executor : threads) {
            if (!executor.isTerminated()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Force threads shutdown (cancel active requests) after specified delay,
     * to be used after shutdown() rejects new requests.
     */
    public void forceShutdown(long timeout, TimeUnit unit) {
        for (int i = 0; i < threads.length; i++) {
            try {
                if (!threads[i].awaitTermination(timeout, unit)) {
                    threads[i].shutdownNow();
                }
            } catch (InterruptedException exception) {
                threads[i].shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    public static class MathUtils {

        private static final long NANOSECONDS_PER_MILLISECOND = 1000000;

        public static int signSafeMod(long dividend, int divisor) {
            int mod = (int) (dividend % divisor);

            if (mod < 0) {
                mod += divisor;
            }

            return mod;
        }

        public static int findNextPositivePowerOfTwo(final int value) {
            return 1 << (32 - Integer.numberOfLeadingZeros(value - 1));
        }

        /**
         * Current time from some arbitrary time base in the past, counting in
         * milliseconds, and not affected by settimeofday or similar system clock
         * changes. This is appropriate to use when computing how much longer to
         * wait for an interval to expire.
         *
         * <p>NOTE: only use it for measuring.
         * http://docs.oracle.com/javase/1.5.0/docs/api/java/lang/System.html#nanoTime%28%29
         *
         * @return current time in milliseconds.
         */
        public static long now() {
            return System.nanoTime() / NANOSECONDS_PER_MILLISECOND;
        }

        /**
         * Current time from some arbitrary time base in the past, counting in
         * nanoseconds, and not affected by settimeofday or similar system clock
         * changes. This is appropriate to use when computing how much longer to
         * wait for an interval to expire.
         *
         * <p>NOTE: only use it for measuring.
         * http://docs.oracle.com/javase/1.5.0/docs/api/java/lang/System.html#nanoTime%28%29
         *
         * @return current time in nanoseconds.
         */
        public static long nowInNano() {
            return System.nanoTime();
        }

        /**
         * Milliseconds elapsed since the time specified, the input is nanoTime
         * the only conversion happens when computing the elapsed time.
         *
         * @param startNanoTime the start of the interval that we are measuring
         * @return elapsed time in milliseconds.
         */
        public static long elapsedMSec(long startNanoTime) {
            return (System.nanoTime() - startNanoTime) / NANOSECONDS_PER_MILLISECOND;
        }

        /**
         * Microseconds elapsed since the time specified, the input is nanoTime
         * the only conversion happens when computing the elapsed time.
         *
         * @param startNanoTime the start of the interval that we are measuring
         * @return elapsed time in milliseconds.
         */
        public static long elapsedMicroSec(long startNanoTime) {
            return TimeUnit.NANOSECONDS.toMicros(System.nanoTime() - startNanoTime);
        }

        /**
         * Nanoseconds elapsed since the time specified, the input is nanoTime
         * the only conversion happens when computing the elapsed time.
         *
         * @param startNanoTime the start of the interval that we are measuring
         * @return elapsed time in milliseconds.
         */
        public static long elapsedNanos(long startNanoTime) {
            return System.nanoTime() - startNanoTime;
        }
    }

    public class BoundedExecutorService extends ForwardingExecutorService {
        private final BlockingQueue<Runnable> queue;
        private final ThreadPoolExecutor thread;
        private final int maxTasksInQueue;

        public BoundedExecutorService(ThreadPoolExecutor thread, int maxTasksInQueue) {
            this.queue = thread.getQueue();
            this.thread = thread;
            this.maxTasksInQueue = maxTasksInQueue;
        }

        @Override
        protected ExecutorService delegate() {
            return this.thread;
        }

        private void checkQueue(int numberOfTasks) {
            if (maxTasksInQueue > 0 && (queue.size() + numberOfTasks) > maxTasksInQueue) {
                throw new RejectedExecutionException("Queue at limit of " + maxTasksInQueue + " items");
            }
        }

        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
            checkQueue(tasks.size());
            return super.invokeAll(tasks);
        }

        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
                throws InterruptedException {
            checkQueue(tasks.size());
            return super.invokeAll(tasks, timeout, unit);
        }

        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
            checkQueue(tasks.size());
            return super.invokeAny(tasks);
        }

        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
                throws InterruptedException, ExecutionException, TimeoutException {
            checkQueue(tasks.size());
            return super.invokeAny(tasks, timeout, unit);
        }

        @Override
        public void execute(Runnable command) {
            checkQueue(1);
            super.execute(command);
        }

        @Override
        public <T> Future<T> submit(Callable<T> task) {
            checkQueue(1);
            return super.submit(task);
        }

        @Override
        public Future<?> submit(Runnable task) {
            checkQueue(1);
            return super.submit(task);
        }

        @Override
        public <T> Future<T> submit(Runnable task, T result) {
            checkQueue(1);
            return super.submit(task, result);
        }
    }
}
