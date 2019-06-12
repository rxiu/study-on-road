package com.rxiu.zkui.core.curator.pool;

import com.rxiu.zkui.core.curator.factory.Factory;
import com.rxiu.zkui.core.curator.validator.Validator;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author shenyuhang
 * @date 2019/5/28
 **/
public class BoundedPool<T> extends AbstractPool<T> {

    private final AtomicInteger poolSize;
    private final Queue<T> queue;
    private final Validator<T> validator;
    private final Factory<T> factory;
    private boolean running;
    private Semaphore semaphore;

    public BoundedPool(int size, Validator validator) {
        this(size, validator, null);
    }

    public BoundedPool(int size, Validator validator, Factory factory) {
        this.poolSize = new AtomicInteger(size);
        semaphore = new Semaphore(size);
        this.queue = new LinkedList<>();
        this.validator = validator;
        this.factory = factory;
        this.running = true;
    }

    @Override
    public synchronized void put(T t) {
        if (poolSize.get() >= queue.size()) {
            if (validator.validate(t) && queue.add(t)) {
                semaphore.release();
            }
        }
    }

    @Override
    public T get() {
        if (running) {
            T t = null;
            if (semaphore.tryAcquire()) {
                t = queue.poll();
            }
            return t;
        }
        throw new IllegalStateException("Bounded Pool is already shutdown.");
    }

    @Override
    protected boolean validate(T t) {
        return validator.validate(t);
    }

    @Override
    protected void doRelease(T t) {
        if (queue.add(t)) {
            semaphore.release();
        }
    }

    @Override
    protected void destroy(T t) {
        validator.invalidate(t);
        if (queue.remove(t)) {
            semaphore.release();
        }
    }

    @Override
    public void shutdown() {
        running = false;
        for (T t : queue) {
            destroy(t);
        }
    }
}
