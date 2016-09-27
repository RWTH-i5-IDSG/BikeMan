package de.rwth.idsg.bikeman.utils;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

/**
 * Processes items with a producer/consumer pattern.
 *
 * The clients of this class are producers which put the items in the queue in their current thread
 * {@link #add(T item)}. A consumer runs in the background thread, watches the queue and processes the items.
 *
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 12.08.2016
 */
@Slf4j
public class QueueProcessor<T> implements Runnable {

    private final ExecutorService executorService;
    private final LinkedBlockingQueue<T> queue;
    private final Consumer<T> consumer;
    private final String queueName;

    private Consumer<T> queueAdder = this::addInternal;
    private Future task;

    // These locks are only relevant for interrupt case. During the consumer impl change,
    // since this is not that immediate, incoming items are still accepted and added to
    // the queue. We do want to prevent that.
    //
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    /**
     * @param executorService Used to spawn background thread for the consumer.
     * @param consumer        Actual logic to process the items in the queue.
     * @param queueName       The name for this processor instance. Currently only used for logging and is helpful to
     *                        distinguish between the behaviors of multiple instances of QueueProcessor (if there are
     *                        any). This should better be something unique, but it's not a hard rule.
     */
    public QueueProcessor(ExecutorService executorService, Consumer<T> consumer, String queueName) {
        Objects.requireNonNull(executorService, "executorService may not be null!");
        Objects.requireNonNull(consumer, "consumer may not be null!");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(queueName), "queueName may not be null!");

        this.executorService = executorService;
        this.consumer = consumer;
        this.queueName = "name=" + queueName;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void start() {
        task = executorService.submit(this);
    }

    public void stop() {
        if (task != null) {
            task.cancel(true);
        }
    }

    // -------------------------------------------------------------------------
    // Producer stuff
    // -------------------------------------------------------------------------

    public void add(final T item) {
        readLock.lock();
        try {
            queueAdder.accept(item);
        } finally {
            readLock.unlock();
        }
    }

    private void addInternal(final T item) {
        log.debug("[{}] Adding {}", queueName, item);

        boolean success = queue.offer(item);
        if (!success) {
            // Should not happen because we do NOT use a "capacity-restricted" LinkedBlockingQueue.
            log.warn("[{}] Failed to put the item {} in queue", queueName, item);
        }
    }

    private void addAfterInterrupt(final T item) {
        log.warn("[{}] Consumer is stopped. Will not add the item to queue", queueName);
    }

    // -------------------------------------------------------------------------
    // Consumer stuff
    // -------------------------------------------------------------------------

    @Override
    public void run() {
        while (true) {
            try {
                consumeInternal();
            } catch (InterruptedException e) {
                handleInterrupt();
                return;
            }
        }
    }

    private void consumeInternal() throws InterruptedException {
        // If there are no items, blocks the thread and waits until there is an element to take
        T item = queue.take();
        // Delegate
        consumer.accept(item);
    }

    private void consumeRemaining() {
        int counter = 0;
        while (queue.size() != 0) {
            try {
                consumeInternal();
                counter++;
            } catch (InterruptedException e) {
                log.error("[{}] Error occurred", queueName, e);
            }
        }
        log.info("[{}] Finished processing of all {} remaining item(s)", queueName, counter);
    }

    // -------------------------------------------------------------------------
    // Other helpers
    // -------------------------------------------------------------------------

    private void handleInterrupt() {
        writeLock.lock();
        try {
            // Stop accepting/adding new items to queue
            queueAdder = this::addAfterInterrupt;
        } finally {
            writeLock.unlock();
        }

        if (queue.isEmpty()) {
            log.info("[{}] Interrupted. Queue is empty. Stopping consumer", queueName);
        } else {
            log.warn("[{}] Interrupted. There are still {} item(s) in queue. Will process these and THEN stop the consumer", queueName, queue.size());
            // Consume the items that are already in the queue
            consumeRemaining();
        }
    }
}
